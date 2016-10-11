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

import org.junit.Before;
import org.junit.Test;
import org.pdfextractor.db.domain.Fingerprint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class FingerprintDaoJpaTest extends AbstractWithInitializedObjectsRunner {

	@Autowired
	protected FingerprintDao fingerprintDao;

	@Before
	public void clear() {
		List<Fingerprint> all = fingerprintDao.findAll();
		for (Fingerprint fp : all) {
			fingerprintDao.delete(fp);
		}
	}

	private void saveEntry() {
		Fingerprint fingerprint = new Fingerprint();
		fingerprint.setpId("123");
		fingerprint.setX(1);
		fingerprint.setY(1);
		fingerprintDao.save(fingerprint);
	}

	@Test
	public void testEntryExistsWithEntries() {
		saveEntry();
		List<Fingerprint> fingerprints = fingerprintDao.findAll();
		assertTrue(fingerprints.size() == 1);
	}

	@Test
	public void testEntryDoesNotExists() {
		List<Fingerprint> notifications = fingerprintDao.findAll();
		assertTrue(notifications.size() == 0);
	}

}
