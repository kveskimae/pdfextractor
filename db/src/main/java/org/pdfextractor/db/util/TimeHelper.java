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

package org.pdfextractor.db.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeHelper {

	public static final String FORMAT = "yyyy-MM-dd";

	private static final Pattern PATTERN_FORMAT = Pattern.compile("[\\d]{4,4}-[\\d]{2,2}-[\\d]{2,2}");
	private static Long UTCEpochSeconds;

	public static String format(final Date value) {
		if (value == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
		String ret = formatter.format(value);
		return ret;
	}

	public static String extractFormat(final String s) {
		if (s == null) {
			return null;
		}
		Matcher matcher = PATTERN_FORMAT.matcher(s);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

	public static LocalDateTime convertUTCEpochSecondsToLocalDateTime(final long utcEpochSeconds) {
		if (utcEpochSeconds == 0) return null;
		return LocalDateTime.ofEpochSecond(utcEpochSeconds, 0, ZoneOffset.UTC);
	}

	public static long getUTCEpochSecondsForNow() {
		return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}
}
