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
import org.pdfextractor.db.domain.TestDomainObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class GenericDaoJpaTest extends AbstractInMemoryRunner {

	@Autowired
	protected TestDomainObjectDao testDomainObjectDao;

	@Before
	public void clear() {
		List<TestDomainObject> all = testDomainObjectDao.findAll();
		for (TestDomainObject tdo : all) {
			testDomainObjectDao.delete(tdo);
		}
	}

	@Test
	public void testGetWithNonExistingId() {
		TestDomainObject fromDB = testDomainObjectDao.findOne(555);
		assertNull(fromDB);
	}

	@Test
	public void testInsertMakesObjectPersistent() {
		TestDomainObject tdo = createTestDomainObject();
		testDomainObjectDao.save(tdo);
		assertNotNull(tdo.getId());
	}

	@Test
	public void testGetWithExistingId() {
		TestDomainObject tdo = createTestDomainObject();
		testDomainObjectDao.saveAndFlush(tdo);
		assertNotNull(tdo.getId());
		TestDomainObject fromDB = testDomainObjectDao.findOne(tdo.getId());
		assertEquals(tdo.getWord(), fromDB.getWord());
	}

	@Test
	public void testUpdateWithManagedObject() {
		TestDomainObject tdo = createTestDomainObject();
		testDomainObjectDao.save(tdo);
		Integer id = tdo.getId();
		String newWord = "word_after_change";
		tdo.setWord(newWord);
		TestDomainObject updated = testDomainObjectDao.saveAndFlush(tdo);
		Integer updatedId = updated.getId();
		assertNotNull(updatedId);
		assertEquals(id, updatedId);
		assertEquals(newWord, updated.getWord());
	}

	@Test
	public void testUpdateWithDetachedObject() {
		TestDomainObject tdo = createTestDomainObject();
		testDomainObjectDao.saveAndFlush(tdo);
		TestDomainObject detachedDto = new TestDomainObject();
		detachedDto.setId(tdo.getId());
		String newWord = "word_after_change";
		detachedDto.setWord(newWord);
		TestDomainObject updated = testDomainObjectDao.saveAndFlush(detachedDto);
		Integer updatedId = updated.getId();
		assertNotNull(updatedId);
		assertEquals(tdo.getId(), updatedId);
		assertEquals(newWord, updated.getWord());
	}

	@Test
	public void testDelete() {
		TestDomainObject ct = createTestDomainObject();
		testDomainObjectDao.saveAndFlush(ct);
		testDomainObjectDao.delete(ct);
		TestDomainObject fromDBAfterDeleting = testDomainObjectDao.findOne(ct.getId());
		assertNull(fromDBAfterDeleting);

	}

	@Test
	public void testGetAll() {
		testDomainObjectDao.saveAndFlush(createTestDomainObject());
		testDomainObjectDao.saveAndFlush(createTestDomainObject());
		testDomainObjectDao.saveAndFlush(createTestDomainObject());
		List<TestDomainObject> fromDBAfterDeleting = testDomainObjectDao.findAll();
		assertTrue(fromDBAfterDeleting.size() == 3);
	}

	private TestDomainObject createTestDomainObject() {
		TestDomainObject ret = new TestDomainObject();
		String word = "ct_word_" + System.currentTimeMillis();
		ret.setWord(word);
		return ret;
	}

}
