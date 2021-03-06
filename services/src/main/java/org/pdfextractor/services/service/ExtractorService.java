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

import org.pdfextractor.algorithm.finder.FinderFactory;
import org.pdfextractor.algorithm.finder.FinderResult;
import org.pdfextractor.db.domain.dictionary.SupportedLocales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

@Service
public class ExtractorService {

	private static Logger log = LoggerFactory.getLogger(ExtractorService.class);

	@Autowired
	private FinderFactory finderFactory;

	public FinderResult extractPaymentData(final byte[] bytes, final Locale locale) {
		InputStream pdfContentStream = new ByteArrayInputStream(bytes);
		switch (locale.getLanguage()) {
			case SupportedLocales.ESTONIAN_LANG_CODE:
				FinderResult finderResultEt = finderFactory.extractEstonian(pdfContentStream);
				return finderResultEt;
			case SupportedLocales.ITALIAN_LANG_CODE:
				FinderResult finderResultIt = finderFactory.extractItalian(pdfContentStream);
				return finderResultIt;
			default:
				throw new IllegalArgumentException("Unsupported locale: '" + locale.getLanguage() + "'");
		}
	}

}
