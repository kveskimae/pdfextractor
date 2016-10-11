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

package org.pdfextractor.db.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@Table(schema = "extraction")
public class PhraseType {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String locale;

	private PaymentFieldType paymentFieldType;

	private String keyPhrase;

	private Integer comparisonPart;

	private Pattern pattern;

	public PhraseType() {
	}

	public PhraseType(final String locale, final PaymentFieldType paymentFieldType, final String keyPhrase, final Integer comparisonPart) {
		setKeyPhrase(keyPhrase);
		setLocale(locale);
		setComparisonPart(comparisonPart);
		setPaymentFieldType(paymentFieldType);
	}

	public PhraseType(final String keyPhrase, final Integer comparisonPart) {
		setKeyPhrase(keyPhrase);
		setComparisonPart(comparisonPart);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Id
	@SequenceGenerator(name = "PHRASETYPEID_GENERATOR", sequenceName = "PHRASE_TYPE_ID_SEQ", schema = "extraction", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHRASETYPEID_GENERATOR")
	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(final String locale) {
		this.locale = locale;
	}

	@Enumerated(EnumType.STRING)
	public PaymentFieldType getPaymentFieldType() {
		return paymentFieldType;
	}

	public void setPaymentFieldType(final PaymentFieldType paymentFieldType) {
		this.paymentFieldType = paymentFieldType;
	}

	public String getKeyPhrase() {
		return keyPhrase;
	}

	public void setKeyPhrase(final String keyPhrase) {
		this.keyPhrase = keyPhrase;
		this.pattern = Pattern.compile("^(.*)(" + keyPhrase + ")(.*)$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	}

	public Integer getComparisonPart() {
		return comparisonPart;
	}

	public void setComparisonPart(final Integer comparisonPart) {
		this.comparisonPart = comparisonPart;
	}

	@Transient
	public Pattern getPattern() {
		return pattern;
	}

}
