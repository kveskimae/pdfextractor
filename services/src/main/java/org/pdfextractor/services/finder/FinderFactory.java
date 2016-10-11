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

package org.pdfextractor.services.finder;

import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.pdfextractor.db.domain.dictionary.SupportedLocales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Locale;
import java.util.Set;

import static org.pdfextractor.db.domain.dictionary.PaymentFieldType.*;

@Service
public class FinderFactory {

	static Logger log = LoggerFactory.getLogger(FinderFactory.class);

	public FinderFactory() {
	}

	private FinderResult findCandidates(final Locale lang, final PaymentFieldType... fieldTypes) {
		FinderResult ret = new FinderResult();
		for (PaymentFieldType fieldType : fieldTypes) {
			Set foundCandidates = ret.getCandidates(fieldType);
		}
		return ret;
	}

	public FinderResult extractEstonian(final InputStream pdfContentStream) {
		FinderResult finderResult = findCandidates(SupportedLocales.ESTONIA,
				IBAN,
				INVOICE_ID,
				NAME,
				REFERENCE_NUMBER,
				TOTAL);
		return finderResult;
	}

	public FinderResult extractItalian(final InputStream pdfContentStream) {
		FinderResult finderResult = findCandidates(SupportedLocales.ITALY,
				PaymentFieldType.NAME,
				PaymentFieldType.TOTAL,
				INVOICE_ID,
				PaymentFieldType.ISSUE_DATE,
				PaymentFieldType.VATIN,
				PaymentFieldType.TOTAL_BEFORE_TAXES);
		return finderResult;
	}

}
