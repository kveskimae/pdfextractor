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

package org.pdfextractor.dto.domain;

import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class PhraseTypeDTO {

	private Integer id;

	private String locale;

	private PaymentFieldType paymentFieldType;

	private String keyPhrase;

	private Integer comparisonPart;

	public PhraseTypeDTO() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public PaymentFieldType getPaymentFieldType() {
		return paymentFieldType;
	}

	public void setPaymentFieldType(PaymentFieldType paymentFieldType) {
		this.paymentFieldType = paymentFieldType;
	}

	public String getKeyPhrase() {
		return keyPhrase;
	}

	public void setKeyPhrase(String keyPhrase) {
		this.keyPhrase = keyPhrase;
	}

	public Integer getComparisonPart() {
		return comparisonPart;
	}

	public void setComparisonPart(Integer comparisonPart) {
		this.comparisonPart = comparisonPart;
	}
}
