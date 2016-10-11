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

package org.pdfextractor.services.util;

import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.exception.AppUnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class SecurityUtil {

	public static String findUsername(final Principal principal) {
		SecurityUser securityUser = findSecurityUser(principal);
		String ret = securityUser.getUsername();
		return ret;
	}

	public static SecurityUser findSecurityUser(final Principal principal) {
		Authentication authentication = findAuthentication(principal);
		Object identity = authentication.getPrincipal();
		SecurityUser ret = (SecurityUser) identity;
		return ret;
	}

	private static Authentication findAuthentication(final Principal principal) {
		Authentication authentication = (Authentication) principal;
		if (authentication == null) {
			authentication = SecurityContextHolder.getContext().getAuthentication();
		}
		if (!authentication.isAuthenticated()) {
			throw new AppUnauthenticatedException("User is not logged in");
		}
		return authentication;
	}

}
