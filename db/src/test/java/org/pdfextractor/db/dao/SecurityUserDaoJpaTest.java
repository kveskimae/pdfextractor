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

package org.pdfextractor.db.dao;

import org.junit.Test;
import org.pdfextractor.db.domain.SecurityAuthority;
import org.pdfextractor.db.domain.SecurityUser;

import static org.junit.Assert.*;

public class SecurityUserDaoJpaTest extends AbstractWithInitializedObjectsRunner {

	@Test
	public void testGetWithNonExistingId() {
		SecurityUser shouldBeNull = securityUserDao.findOne(555);
		assertNull(shouldBeNull);
	}

	@Test
	public void testInsertMakesObjectPersistent() {
		SecurityUser su = generateSecurityUser();
		securityUserDao.save(su);
		assertNotNull(su.getId());
	}

	@Test
	public void testGetWithExistingId() {
		SecurityUser su = generateSecurityUser();
		securityUserDao.saveAndFlush(su);
		SecurityUser fromDB = securityUserDao.findOne(su.getId());
		assertEquals(su.getUsername(), fromDB.getUsername());
	}

	@Test
	public void testDelete() {
		SecurityUser su = generateSecurityUser();
		securityUserDao.saveAndFlush(su);
		securityUserDao.delete(su);
		SecurityUser fromDBAfterDeleting = securityUserDao.findOne(su.getId());
		assertNull(fromDBAfterDeleting);
	}

	@Test
	public void testLoadingWithAuthorities() {
		SecurityUser su = generateSecurityUser();
		su.getAuthorities().add(securityAuthority1);
		su.getAuthorities().add(securityAuthority2);
		securityUserDao.saveAndFlush(su);
		SecurityUser fromDB = securityUserDao.findOne(su.getId());
		boolean foundFirst = false;
		boolean foundSecond = false;
		for (SecurityAuthority sa : fromDB.getAuthorities()) {
			if (sa.getAuthority().equals(securityAuthority1.getAuthority())) {
				foundFirst = true;
			}
			if (sa.getAuthority().equals(securityAuthority2.getAuthority())) {
				foundSecond = true;
			}
		}
		assertTrue(foundFirst);
		assertTrue(foundSecond);
	}

}
