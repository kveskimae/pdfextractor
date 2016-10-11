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

package org.pdfextractor.restapi;

import org.pdfextractor.db.config.JpaConfig;
import org.pdfextractor.restapi.config.RestApiConfig;
import org.pdfextractor.restapi.config.SecurityConfig;
import org.pdfextractor.services.config.BaseServicesConfig;
import org.pdfextractor.services.config.ServicesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.TimeZone;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static Logger log = LoggerFactory.getLogger(AppInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.info("Initializing REST API started");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		super.onStartup(servletContext);
		log.info("Initializing REST API completed");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{

				// First priority in loading
				JpaConfig.class,
				BaseServicesConfig.class,

				// Second priority in loading (dependants)
				ServicesConfig.class,
				SecurityConfig.class

		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{
				RestApiConfig.class
		};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/rest/*"};
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[]{new DelegatingFilterProxy("springSecurityFilterChain")};
	}

}