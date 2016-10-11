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

import org.pdfextractor.db.dao.PhraseTypeDao;
import org.pdfextractor.db.domain.PhraseType;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.pdfextractor.dto.domain.PhraseTypeDTO;
import org.pdfextractor.db.exception.AppResourceNotFoundException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PhraseTypeService {

	private static Logger log = LoggerFactory.getLogger(PhraseTypeService.class);

	@Autowired
	private PhraseTypeDao phraseTypeDao;

	@Autowired
	private Mapper dozerBeanMapper;

	public PhraseTypeDTO findOneById(final Integer id) throws AppResourceNotFoundException {
		PhraseType phraseType = findById(id);
		PhraseTypeDTO ret = dozerBeanMapper.map(phraseType, PhraseTypeDTO.class);
		return ret;
	}

	private PhraseType findById(final Integer id) throws AppResourceNotFoundException {
		PhraseType ret = phraseTypeDao.findOne(id);
		if (ret == null) {
			throw new AppResourceNotFoundException("No phrase type found for provided id: " + id);
		}
		return ret;
	}

	public List<PhraseTypeDTO> findAllByLocaleAndType(final String locale, final PaymentFieldType paymentFieldType) {
		List<PhraseType> phraseTypes = phraseTypeDao.findByLocaleAndPaymentFieldType(locale, paymentFieldType);
		List<PhraseTypeDTO> dtos = new ArrayList<>();
		for (PhraseType phraseType : phraseTypes) {
			PhraseTypeDTO phraseTypeDTO = dozerBeanMapper.map(phraseType, PhraseTypeDTO.class);
			dtos.add(phraseTypeDTO);
		}
		return dtos;
	}

	public PhraseTypeDTO createOne(final String locale, final PaymentFieldType paymentFieldType, final PhraseTypeDTO phraseTypeDTO) {
		PhraseType phraseType = dozerBeanMapper.map(phraseTypeDTO, PhraseType.class);
		phraseType.setPaymentFieldType(paymentFieldType);
		phraseType.setLocale(locale);
		phraseTypeDao.saveAndFlush(phraseType);
		PhraseTypeDTO responsePhraseTypeDTO = dozerBeanMapper.map(phraseType, PhraseTypeDTO.class);
		return responsePhraseTypeDTO;
	}

	public PhraseTypeDTO updateOne(final Integer id, final PhraseTypeDTO phraseTypeDTO) throws AppResourceNotFoundException {
		PhraseType phraseType = findById(id);
		phraseType.setKeyPhrase(phraseTypeDTO.getKeyPhrase());
		phraseType.setComparisonPart(phraseTypeDTO.getComparisonPart());
		phraseTypeDao.saveAndFlush(phraseType);
		PhraseTypeDTO ret = dozerBeanMapper.map(phraseType, PhraseTypeDTO.class);
		return ret;
	}

	public void deleteOne(final Integer id) throws AppResourceNotFoundException {
		PhraseType phraseType = findById(id);
		phraseTypeDao.delete(phraseType);
		phraseTypeDao.flush();
	}
}
