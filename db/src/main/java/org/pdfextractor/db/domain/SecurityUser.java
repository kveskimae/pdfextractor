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

package org.pdfextractor.db.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.pdfextractor.db.domain.dictionary.AccountType;
import org.pdfextractor.db.domain.dictionary.AppAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(schema = "extraction")
public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	private String password;

	private String email;

	private boolean enabled;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private AccountType accountType;

	private Integer trialLimit;

	private Collection<SecurityAuthority> authorities = new ArrayList<>();

	// private Collection<InvoiceWorkflow> invoiceWorkflows = new ArrayList<>();

	public SecurityUser() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Id
	@SequenceGenerator(name = "SECURITYUSERID_GENERATOR", sequenceName = "SECURITY_USER_ID_SEQ", schema = "extraction", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECURITYUSERID_GENERATOR")
	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	@Override
	@Column(unique = true, nullable = false)
	@Pattern(regexp = "^\\w{3,20}$")
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Override
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{5,}$")
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
			+ "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+ "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
			message = "Email is invalid" // "{invalid.email}"
	)
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(final boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(final boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(final boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, targetEntity = SecurityAuthority.class)
	@ElementCollection(targetClass = SecurityAuthority.class)
	@JoinTable(name = "security_user_security_authority", joinColumns = {@JoinColumn(name = "id_security_user")}, inverseJoinColumns = {@JoinColumn(name = "id_security_authority")}, schema = "extraction")
	public Collection<SecurityAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(final Collection<SecurityAuthority> authorities) {
		this.authorities = authorities;
	}

	// @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "securityUser")
	// public Collection<InvoiceWorkflow> getInvoiceWorkflows() {
	//	return invoiceWorkflows;
	// }

	// public void setInvoiceWorkflows(Collection<InvoiceWorkflow> invoiceWorkflows) {
	// 	this.invoiceWorkflows = invoiceWorkflows;
	// }

	private boolean hasAuthority(final AppAuthority authority) {
		if (authority == null) {
			throw new NullPointerException("Parameter authority is null");
		}
		for (SecurityAuthority sa : getAuthorities()) {
			if (authority.toString().equals(sa.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@Enumerated(EnumType.STRING)
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
	}

	public Integer getTrialLimit() {
		return trialLimit;
	}

	public void setTrialLimit(final Integer trialLimit) {
		this.trialLimit = trialLimit;
	}

	@Transient
	public boolean isAdmin() {
		return hasAuthority(AppAuthority.ADMIN);
	}

	@Transient
	public boolean isReviewer() {
		return hasAuthority(AppAuthority.REVIEWER);
	}

	@Transient
	public boolean isClient() {
		return hasAuthority(AppAuthority.CLIENT);
	}

	@Transient
	public boolean isTrialAccount() {
		return AccountType.TRIAL.equals(getAccountType());
	}

}
