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

import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.pdfextractor.db.domain.dictionary.PaymentFieldTypeEditor;
import org.pdfextractor.dto.CollectionNames;
import org.pdfextractor.dto.domain.PhraseTypeDTO;
import org.pdfextractor.db.exception.AppResourceNotFoundException;
import org.pdfextractor.services.service.PhraseTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/" + PhraseTypeController.URL_PART)
public class PhraseTypeController {

	public static final String URL_PART = CollectionNames.PHRASE_TYPES;

	private static Logger log = LoggerFactory.getLogger(PhraseTypeController.class);

	@Autowired
	private PhraseTypeService phraseTypeService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(PaymentFieldType.class, new PaymentFieldTypeEditor());
	}

	@RequestMapping(value = "/{locale}/{paymentFieldType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PhraseTypeDTO>> getAll(@PathVariable("locale") final String locale, @PathVariable("paymentFieldType") final PaymentFieldType paymentFieldType) {
		List<PhraseTypeDTO> dtos = phraseTypeService.findAllByLocaleAndType(locale, paymentFieldType);
		ResponseEntity ret = new ResponseEntity<>(dtos, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PhraseTypeDTO> get(@PathVariable("id") final Integer id) throws IOException, AppResourceNotFoundException {
		PhraseTypeDTO dto = phraseTypeService.findOneById(id);
		ResponseEntity<PhraseTypeDTO> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{locale}/{paymentFieldType}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PhraseTypeDTO> post(@PathVariable("locale") final String locale, @PathVariable("paymentFieldType") final PaymentFieldType paymentFieldType, @RequestBody final PhraseTypeDTO phraseTypeDTO) {
		PhraseTypeDTO dto = phraseTypeService.createOne(locale, paymentFieldType, phraseTypeDTO);
		ResponseEntity<PhraseTypeDTO> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PhraseTypeDTO> put(@PathVariable("id") final Integer id, @RequestBody final PhraseTypeDTO phraseTypeDTO) throws IOException, AppResourceNotFoundException {
		PhraseTypeDTO dto = phraseTypeService.updateOne(id, phraseTypeDTO);
		ResponseEntity<PhraseTypeDTO> ret = new ResponseEntity<>(dto, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> delete(@PathVariable("id") final Integer id) throws IOException, AppResourceNotFoundException {
		phraseTypeService.deleteOne(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
