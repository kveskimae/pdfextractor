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
import org.pdfextractor.db.domain.ExtractedField;
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ExtractedFieldDaoJpaTest extends AbstractWithInitializedObjectsRunner {

	@Autowired
	protected ExtractedFieldDao extractedFieldDao;

	@Before
	public void clear() {
		extractedFieldDao.deleteAllInBatch();
		// extractedFieldDao.flush();
		assertEquals(0, extractedFieldDao.findAll().size());
	}

	private void saveEntry() {
		ExtractedField ef = new ExtractedField();
		ef.setPaymentFieldType(PaymentFieldType.TOTAL);
		ef.setInvoiceWorkflow(workflow1);
		ef.setValue("13.4");
		extractedFieldDao.saveAndFlush(ef);
	}

	@Test
	public void testEntryExistsWithEntries() {
		saveEntry();
		assertEquals(1, extractedFieldDao.findAll().size());
	}

	@Test
	public void testEntryExistsWithNoEntries() {
		assertEquals(0, extractedFieldDao.findAll().size());
	}

	@Test
	public void testEntryExistsInWorkflow() {
		saveEntry();
		InvoiceWorkflow retrieved = invoiceWorkflowDao.findOne(workflow1.getId());
		assertFalse(retrieved.getExtractedFields().isEmpty());
	}

}
