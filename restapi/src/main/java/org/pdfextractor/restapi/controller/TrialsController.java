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

import org.pdfextractor.dto.composite.PollAnswerDTO;
import org.pdfextractor.services.service.InvoiceWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/" + TrialsController.URL_PART)
public class TrialsController {

	public static final String URL_PART = "trials";

	private static Logger log = LoggerFactory.getLogger(TrialsController.class);

	@Autowired
	private InvoiceWorkflowService invoiceWorkflowService;

	@PreAuthorize("hasAuthority('CLIENT')")
	@RequestMapping(value = "/{locale}/{posixTime}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PollAnswerDTO> getAll(@PathVariable("locale") final String locale, @PathVariable("posixTime") final Long lastPollPosixTime, final Principal principal) {
		PollAnswerDTO pollAnswerDTO = invoiceWorkflowService.findForClientByLocaleAndLastPollTime(locale, lastPollPosixTime, principal);
		ResponseEntity<PollAnswerDTO> ret = new ResponseEntity<>(pollAnswerDTO, HttpStatus.OK);
		return ret;
	}

}