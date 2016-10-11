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

package org.pdfextractor.db.domain.dictionary;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public abstract class SupportedLocales {

	public static final String

			ESTONIAN_LANG_CODE = "et",

	ITALIAN_LANG_CODE = "it";

	public static final Locale

			ESTONIA = new Locale.Builder().setLanguage(ESTONIAN_LANG_CODE).setRegion("EE").build(),

	ITALY = Locale.ITALY;

	public static Locale findLocaleByLanguage(final String language) {
		if (language == null) {
			throw new NullPointerException("Parameter language is null");
		}
		if (StringUtils.isEmpty(language)) {
			throw new IllegalArgumentException("Parameter language is an empty string: '" + language + "'");
		}
		switch (language) {
			case ESTONIAN_LANG_CODE:
				return ESTONIA;
			case ITALIAN_LANG_CODE:
				return ITALY;
			default:
				throw new IllegalArgumentException("Parameter language is unsupported: '" + language + "'");
		}
	}

}
