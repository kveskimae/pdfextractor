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
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(schema = "extraction")
public class SecurityAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String authority;

	private Collection<SecurityUser> securityUsers = new ArrayList<SecurityUser>();

	public SecurityAuthority() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Id
	@SequenceGenerator(name = "SECURITYAUTHORITY_GENERATOR", sequenceName = "SECURITY_AUTHORITY_ID_SEQ", schema = "extraction", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECURITYAUTHORITY_GENERATOR")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(final String authority) {
		this.authority = authority;
	}

	@OneToMany(fetch = FetchType.EAGER, targetEntity = SecurityUser.class) // cascade= {CascadeType.ALL}
	@ElementCollection(targetClass = SecurityUser.class)
	@JoinTable(name = "security_user_security_authority", joinColumns = {@JoinColumn(name = "id_security_authority")}, inverseJoinColumns = {@JoinColumn(name = "id_security_user")}, schema = "extraction")
	public Collection<SecurityUser> getSecurityUsers() {
		return securityUsers;
	}

	public void setSecurityUsers(Collection<SecurityUser> securityUsers) {
		this.securityUsers = securityUsers;
	}
}