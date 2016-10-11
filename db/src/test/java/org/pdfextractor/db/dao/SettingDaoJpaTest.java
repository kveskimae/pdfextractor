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
import org.pdfextractor.db.domain.Setting;
import org.pdfextractor.db.domain.dictionary.SettingType;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class SettingDaoJpaTest extends AbstractWithInitializedObjectsRunner {

	@Autowired
	protected SettingDao settingDao;

	@Before
	public void clear() {
		settingDao.deleteAllInBatch();
		assertEquals(0, settingDao.findAll().size());
	}

	private void saveEntry() {
		Setting ef = new Setting();
		ef.setSettingType(SettingType.API_EMAIL_USERNAME);
		ef.setValue("test@test.com");
		settingDao.saveAndFlush(ef);
	}

	@Test
	public void testEntryExistsWithEntries() {
		saveEntry();
		assertEquals(1, settingDao.findAll().size());
	}

	@Test
	public void testEntryExistsWithNoEntries() {
		assertEquals(0, settingDao.findAll().size());
	}

}
