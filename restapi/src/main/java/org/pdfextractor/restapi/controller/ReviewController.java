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

import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.pdfextractor.dto.composite.PollAnswerDTO;
import org.pdfextractor.dto.domain.InvoiceWorkflowDTO;
import org.pdfextractor.services.service.InvoiceWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/" + ReviewController.URL_PART)
public class ReviewController {

	private static Logger log = LoggerFactory.getLogger(ReviewController.class);

	public static final String URL_PART = "review", POLLER_URL_PART = "poller";

	@Autowired
	private InvoiceWorkflowService reviewService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvoiceWorkflowDTO> get(@PathVariable("id") final Integer id, final Principal principal) {
		InvoiceWorkflowDTO dto = reviewService.findById(id, principal);
		ResponseEntity<InvoiceWorkflowDTO> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

	@PreAuthorize("hasAuthority('REVIEWER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvoiceWorkflowDTO> put(@PathVariable("id") final Integer id, @RequestBody final InvoiceWorkflowDTO invoiceWorkflowDTO) {
		InvoiceWorkflowDTO dto = reviewService.updateOne(id, invoiceWorkflowDTO);
		ResponseEntity<InvoiceWorkflowDTO> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

	@PreAuthorize("hasAuthority('REVIEWER')")
	@RequestMapping(value = ReviewController.POLLER_URL_PART + "/{locale}/{state}/{posixTime}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PollAnswerDTO> getAll(@PathVariable("locale") final String locale, @PathVariable("state") final InvoiceWorkflowState state, @PathVariable("posixTime") final Long lastPollPosixTime) {
		PollAnswerDTO pollAnswerDTO = reviewService.findAllByLocaleAndStateAndLastPollTime(locale, state, lastPollPosixTime);
		ResponseEntity<PollAnswerDTO> ret = new ResponseEntity<>(pollAnswerDTO, HttpStatus.OK);
		return ret;
	}

}