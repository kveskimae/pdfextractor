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

import org.pdfextractor.db.dao.InvoiceWorkflowDao;
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.exception.AppAccessForbiddenException;
import org.pdfextractor.db.exception.AppIOException;
import org.pdfextractor.db.exception.AppResourceNotFoundException;
import org.pdfextractor.services.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;


@Service
public class InvoiceFileService {

	private static Logger log = LoggerFactory.getLogger(InvoiceFileService.class);

	@Autowired
	private InvoiceWorkflowDao invoiceWorkflowDao;

	@Autowired
	private FileSaver fileSaver;

	public byte[] findById(final String fileName, final Principal principal) {
		checkAccessRights(fileName, principal);
		try {
			byte[] bytes = fileSaver.getFileByName(fileName + ".pdf");
			return bytes;
		} catch (final IOException e) {
			throw new AppIOException("Error while retrieving invoice file " + fileName, e);
		}
	}

	private void checkAccessRights(String fileName, Principal principal) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		if (!securityUser.isReviewer()) {
			InvoiceWorkflow workflow = invoiceWorkflowDao.findByFileName(fileName + ".pdf");
			if (workflow == null) {
				throw new AppResourceNotFoundException("No invoice workflow found for invoice file " + fileName);
			}
			Integer principalId = securityUser.getId();
			if (workflow.getSecurityUser().getId() != principalId) {
				throw new AppAccessForbiddenException("User " + SecurityUtil.findUsername(principal) + " has no right to see invoice file " + fileName);
			}
		}
	}

}
