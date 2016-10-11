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
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;

import java.util.List;

import static org.junit.Assert.*;

public class InvoiceWorkflowDaoJpaTest extends AbstractWithInitializedObjectsRunner {

	@Test
	public void testSave() {
		InvoiceWorkflow generated = generateWorkflow();
		invoiceWorkflowDao.saveAndFlush(generated);
		int currentIdx = generated.getId();
		String expectedFileName = generated.getFileName();
		invoiceWorkflowDao.saveAndFlush(generateWorkflow());
		InvoiceWorkflow workflow = invoiceWorkflowDao.findOne(currentIdx);
		assertEquals(expectedFileName, workflow.getFileName());
	}

	@Test
	public void testFindByState() {
		invoiceWorkflowDao.saveAndFlush(generateWorkflow());
		List<InvoiceWorkflow> user1 = invoiceWorkflowDao.findByState(InvoiceWorkflowState.pending_review);
		assertNotNull(user1);
		assertFalse(user1.isEmpty());
	}

	@Test
	public void testUpdate() {
		int currentIdx = idx;
		invoiceWorkflowDao.saveAndFlush(generateWorkflow());
		InvoiceWorkflow workflow = invoiceWorkflowDao.findByFileName("file" + currentIdx);
		assertNotNull(workflow);
		assertEquals("file" + currentIdx, workflow.getFileName());
		String newName = workflow.getFileName() + "_updated";
		workflow.setFileName(newName);
		invoiceWorkflowDao.saveAndFlush(workflow);
		assertEquals(newName, workflow.getFileName());
	}

}
