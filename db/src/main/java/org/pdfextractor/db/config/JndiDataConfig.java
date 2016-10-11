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

package org.pdfextractor.db.config;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@ProductionEnvironment
public class JndiDataConfig implements DataConfig {

	public static final String JNDI_NAME = "java:comp/env/jdbc/pdfextractorDataSource";

	public DataSource dataSource() {
		JndiTemplate jndi = new JndiTemplate();
		DataSource dataSource;
		try {
			dataSource = (DataSource) jndi.lookup(JNDI_NAME);
		} catch (NamingException e) {
			throw new BeanCreationException("Failed to find JDNI " + JNDI_NAME, e);
		}
		return dataSource;
	}

}
