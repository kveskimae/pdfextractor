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

package org.pdfextractor.restapi.controller;

import org.pdfextractor.dto.CollectionNames;
import org.pdfextractor.dto.domain.InvoiceWorkflowDTO;
import org.pdfextractor.db.exception.AppAbstractException;
import org.pdfextractor.db.exception.AppBadInputException;
import org.pdfextractor.services.service.InvoiceWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/" + InvoiceWorkflowController.URL_PART)
public class InvoiceWorkflowController {

	private static Logger log = LoggerFactory.getLogger(InvoiceWorkflowController.class);

	public static final String URL_PART = CollectionNames.INVOICE_WORKFLOWS;

	@Autowired
	private InvoiceWorkflowService invoiceWorkflowService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvoiceWorkflowDTO> get(@PathVariable("id") final Integer id, final Principal principal) throws IOException, AppAbstractException {
		InvoiceWorkflowDTO dto = invoiceWorkflowService.findById(id, principal);
		ResponseEntity<InvoiceWorkflowDTO> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{locale}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> post(final Principal principal, @PathVariable("locale") final String locale, final @RequestParam("file") MultipartFile file) {
		if (StringUtils.isEmpty(file.getContentType())) {
			throw new AppBadInputException("Content type for file is unset");
		}
		log.info("Converting from content type '" + file.getContentType() + "'");
		if (file.isEmpty()) {
			throw new AppBadInputException("File is empty");
		}
		byte[] bytes;
		try {
			bytes = file.getBytes();
		} catch (IOException e) {
			throw new AppBadInputException("File seems corrupt");
		}
		Object dto = invoiceWorkflowService.handleInvoice(principal, locale, bytes);
		ResponseEntity<Object> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

}
