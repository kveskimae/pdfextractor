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
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * Note: Natural orderings in subclass implementations of Comparable are not consistent with equals.
 * 
 * Also rearranges more likely candidates to the start of the collection.
 *
 */
public class Candidate implements Comparable<Candidate> {

	//<editor-fold desc="Properties">

	public static enum PropertyType {

		ESTONIAN_IS_PANK_PRESENT,

		DOUBLE_NUMBER,

		EURO_SIGN_FOUND,

		NORMAL_LINE,

		PHRASE_TYPE;

	}

	private Map<PropertyType, Object> properties = new HashMap<>();

	//</editor-fold>
	
	private Object value;

	private PaymentFieldType paymentFieldType;

	private Locale locale;

	public Candidate(final Object value, final Locale locale, final PaymentFieldType paymentFieldType) {
		this.value = value;
		this.paymentFieldType = paymentFieldType;
		this.locale = locale;
	}

	public Object getValue() {
		return value;
	}

	public Map<PropertyType, Object> getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Candidate) {
			@SuppressWarnings("unchecked")
			Candidate otherAsAbstractCandidate = (Candidate)other;
			if (otherAsAbstractCandidate.value.equals(this.value)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this.value);
	}

	public PaymentFieldType getFieldType() {
		return paymentFieldType;
	}

	public Locale getLocale() {
		return locale;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compareTo(final Candidate other) {
		return 0;
	}

}
