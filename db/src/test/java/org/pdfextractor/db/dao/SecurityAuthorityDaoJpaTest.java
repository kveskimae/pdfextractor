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

public class SecurityAuthorityDaoJpaTest extends AbstractWithInitializedObjectsRunner {

	@Test
	public void testGetWithNonExistingId() {
		SecurityAuthority fromDB = securityAuthorityDao.findOne(555);
		assertNull(fromDB);
	}

	@Test
	public void testInsertMakesObjectPersistent() {
		SecurityAuthority sa = generateSecurityAuthority();
		securityAuthorityDao.save(sa);
		assertNotNull(sa.getId());
	}

	@Test
	public void testGetWithExistingId() {
		SecurityAuthority sa = generateSecurityAuthority();
		securityAuthorityDao.saveAndFlush(sa);
		SecurityAuthority fromDB = securityAuthorityDao.findOne(sa.getId());
		assertEquals(sa.getAuthority(), fromDB.getAuthority());
	}

	@Test
	public void testDelete() {
		SecurityAuthority sa = generateSecurityAuthority();
		securityAuthorityDao.saveAndFlush(sa);
		securityAuthorityDao.delete(sa);
		SecurityAuthority fromDBAfterDeleting = securityAuthorityDao.findOne(sa.getId());
		assertNull(fromDBAfterDeleting);
	}

	@Test
	public void testFindByAuthority() {
		SecurityAuthority securityAuthority = securityAuthorityDao.findByAuthority(securityAuthority1.getAuthority());
		assertFalse(securityAuthority.getSecurityUsers().isEmpty());
		boolean found = false;
		for (SecurityUser user : securityAuthority.getSecurityUsers()) {
			if (user.getId().equals(su.getId())) {
				found = true;
			}
		}
		assertTrue(found);
	}

}
