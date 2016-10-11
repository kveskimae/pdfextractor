/*
 * Copyright (c) 2016 Kristjan Veskimae
 *
 *     Permission is hereby granted, free of charge, to any person obtaining
 *     a copy of this software and associated documentation files (the "Software"),
 *     to deal in the Software without restriction, including without limitation
 *     the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *     and/or sell copies of the Software, and to permit persons to whom the Software
 *     is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *     EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *     OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *     IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *     CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *     TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 *     OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.pdfextractor.restapi.config;

import org.pdfextractor.db.domain.dictionary.AppAuthority;
import org.pdfextractor.restapi.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements AuthenticationFailureHandler {

	static Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	public static final String LOGIN_URL = "/admin/#/login";

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	// @Autowired
	// private PasswordEncoder passwordEncoder;

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Autowired
	public void registerAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		// .passwordEncoder(passwordEncoder)
		;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
		String username = httpServletRequest.getParameter("j_username");
		log.info("Failed login attempt by user '" + username + "'");
	}

	/**
	 * For back office app
	 */
	@Configuration
	@Order(30)
	public static class FormConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(final HttpSecurity http) throws Exception {
			log.info("Configuring REST API security started");
			// SavedRequestAwareAuthenticationSuccessHandler sraush = new SavedRequestAwareAuthenticationSuccessHandler();
			// sraush.setDefaultTargetUrl("/admin/");
			// @formatter:off
		http
				.formLogin()
					// .failureHandler(this)
					//.failureHandler(new SimpleUrlAuthenticationFailureHandler())
					// .failureUrl("/login.html?error")
					// .successForwardUrl("redirect:/admin/")
					.successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {
						@Override
						public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
							clearAuthenticationAttributes(request);
							getRedirectStrategy().sendRedirect(request, response, "/rest/bootstrapdata");
						}
					})
					.loginPage(LOGIN_URL)
					.loginProcessingUrl("/rest/security_check")
					// .defaultSuccessUrl("/admin/")
					.failureUrl(LOGIN_URL)
					.usernameParameter("j_username")
					.passwordParameter("j_password")
					.permitAll()
					.and()
				.logout()
					.logoutUrl("/rest/logout")
					.deleteCookies("remember-me")
					.logoutSuccessUrl(LOGIN_URL)
					.invalidateHttpSession(true)
					.permitAll()
					.and()
				.authorizeRequests()
					.antMatchers("/rest/" + BootstrapDataController.URL_PART + "/**").hasAnyAuthority(AppAuthority.REVIEWER.toString(), AppAuthority.CLIENT.toString())
					.antMatchers("/rest/" + InvoiceFileController.URL_PART + "/**").hasAnyAuthority(AppAuthority.REVIEWER.toString(), AppAuthority.CLIENT.toString())
					.antMatchers("/rest/" + PhraseTypeController.URL_PART + "/**").hasAnyAuthority(AppAuthority.REVIEWER.toString())
					.antMatchers("/rest/" + ProfileController.URL_PART + "/**").authenticated()
					.antMatchers("/rest/" + ReloadController.URL_PART + "/**").hasAnyAuthority(AppAuthority.REVIEWER.toString())
					.antMatchers("/rest/" + ReviewController.URL_PART + "/**").hasAnyAuthority(AppAuthority.REVIEWER.toString())
					.antMatchers("/rest/" + SettingController.URL_PART + "/**").hasAnyAuthority(AppAuthority.ADMIN.toString())
					.antMatchers("/rest/" + StatisticsController.URL_PART + "/**").hasAnyAuthority(AppAuthority.ADMIN.toString())
					.antMatchers("/rest/" + TrialsController.URL_PART + "/**").hasAnyAuthority(AppAuthority.CLIENT.toString())
					.antMatchers("/rest/" + UserController.URL_PART + "/**").hasAnyAuthority(AppAuthority.ADMIN.toString())
					.antMatchers("/rest/" + TestController.URL_PART).permitAll()
					.anyRequest().denyAll()
					.and()
				.csrf().disable()
				.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable()
				.xssProtection().disable();
		// @formatter:on
			log.info("Configuring REST API security finished");
		}

	}

	/**
	 *
	 * Configuration for machine-to-machine communication
	 *
	 * The value for <tt>@Order</tt> must be smaller than the one for form authentication.
	 *
	 */
	@Configuration
	@Order(20)
	public static class HttpBasicConfiguration extends WebSecurityConfigurerAdapter {

		protected void configure(final HttpSecurity http) throws Exception {
			log.info("Configuring REST API M2M communication security started");
			// @formatter:off
			http
					.antMatcher("/rest/" + InvoiceWorkflowController.URL_PART + "/**")
					.authorizeRequests()
						.antMatchers("/rest/" + InvoiceWorkflowController.URL_PART + "/**").hasAnyAuthority(AppAuthority.CLIENT.toString())
						// anyRequest().denyAll().
						.and()
					.httpBasic()
						.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(SecurityConfig.LOGIN_URL) {
							@Override
							public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
								if (authException instanceof InsufficientAuthenticationException) {
									SecurityConfig.submitError(request, response, HttpStatus.FORBIDDEN, "You are not logged in");
								} else if (authException instanceof BadCredentialsException) {
									SecurityConfig.submitError(request, response, HttpStatus.UNAUTHORIZED,"You are lacking valid authentication credentials");
								} else {
									super.commence(request, response, authException);
								}
							}
						})
						.and()
					.csrf().disable();
			// @formatter:on
			log.info("Configuring REST API M2M communication security finished");
		}

	}

	private static void submitError(final HttpServletRequest request, final HttpServletResponse response, final HttpStatus httpStatus, final String errorMsg) throws IOException {
		response.setStatus(httpStatus.value());
		response.setContentType("application/json;charset=UTF-8");
		response.resetBuffer();
		response.getWriter().print("{\"url\": \""+request.getRequestURL()+"\", \"errorMsg\": \"" +errorMsg+ "\"}");
	}

}
