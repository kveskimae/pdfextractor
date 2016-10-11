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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class FileSaver {

	private static Logger log = LoggerFactory.getLogger(FileSaver.class);

	private final String folder;

	private final boolean saveInvoices;

	private AtomicLong counter;

	public FileSaver(final boolean saveInvoices, final String folder, final Long counterStart) {
		this.saveInvoices = saveInvoices;
		this.folder = folder;
		this.counter = new AtomicLong(counterStart);
	}

	public String save(final byte[] bytes) throws IOException {
		if (saveInvoices) { // && FileTypeResolver.isPDFFile(file)) {
			String fileName = ""+ counter.getAndIncrement() +".pdf";
			Path fileAbsolutePath = Paths.get(folder, fileName);
			Path createdPath = Files.write(fileAbsolutePath, bytes);
			log.info("Saved received PDF to file " + createdPath.toString());
			return fileName;
		}
		return null;
	}

	public byte[] getFileByName(String fileName) throws IOException {
		// TODO verify fileName for ill data
		Path fileAbsolutePath = Paths.get(folder, fileName);
		log.info("Retrieving file " + fileAbsolutePath);
		byte[] ret = Files.readAllBytes(fileAbsolutePath);
		return ret;
	}
}
