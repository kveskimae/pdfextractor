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

package org.pdfextractor.services.service;

import org.pdfextractor.db.dao.SecurityUserDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class AppUserDetailsService implements UserDetailsService {

	private static Logger log = LoggerFactory.getLogger(AppUserDetailsService.class);

	@Autowired
	private SecurityUserDao securityUserDao;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		log.info("Loading username '" + username + "'");
		if (username == null) {
			throw new NullPointerException("Parameter username is null");
		} else if (StringUtils.isEmpty(username)) {
			throw new IllegalArgumentException("Parameter username is empty string");
		}
		UserDetails ret = securityUserDao.findByUsername(username);
		if(ret == null) {
			throw new UsernameNotFoundException("Username not found: '" + username + "'");
		}
		// setPassword(new BCryptPasswordEncoder().encode(password));
		return ret;
	}

}
