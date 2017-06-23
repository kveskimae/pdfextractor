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

import candidate.Candidate;
import finder.FinderResult;
import org.pdfextractor.db.dao.ExtractedFieldDao;
import org.pdfextractor.db.domain.ExtractedField;
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.pdfextractor.db.util.TimeHelper;
import org.pdfextractor.db.domain.dictionary.SupportedLocales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.JavaConverters;
import scala.collection.convert.Decorators;
import scala.collection.convert.WrapAsJava;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ExtractedFieldService {

	private static Logger log = LoggerFactory.getLogger(ExtractedFieldService.class);

	@Autowired
	private ExtractedFieldDao extractedFieldDao;

	public void saveOrUpdateExtractedField(final PaymentFieldType fieldType, final Object formatFromClient, final InvoiceWorkflow invoiceWorkflow) {
		ExtractedField extractedField = invoiceWorkflow.getExtractedFieldForType(fieldType);
		Object newValue = formatFromClient;
		if (PaymentFieldType.ISSUE_DATE.equals(fieldType)) {
			newValue = TimeHelper.extractFormat((String)newValue);
		}
		if (PaymentFieldType.VATIN.equals(fieldType)) {
			Iterator<String> it = ((List<String>)formatFromClient).iterator();
			StringBuilder sb = new StringBuilder();
			while (it.hasNext()) {
				sb.append(it.next());
				if (it.hasNext()) {
					sb.append(',');
				}
			}
			newValue = sb.toString();
		}
		if (extractedField == null) {
			extractedField = new ExtractedField();
			extractedField.setPaymentFieldType(fieldType);
			extractedField.setInvoiceWorkflow(invoiceWorkflow);
		}
		extractedField.setValue(newValue.toString());
		extractedFieldDao.saveAndFlush(extractedField);
	}

	public void saveExtractedFields(final FinderResult finderResult, final InvoiceWorkflow workflow) {
		if (finderResult.hasValuesForType(PaymentFieldType.TOTAL)) {
			Double total = finderResult.<Double>getValue(PaymentFieldType.TOTAL);
			ExtractedField totalEF = new ExtractedField();
			totalEF.setInvoiceWorkflow(workflow);
			totalEF.setPaymentFieldType(PaymentFieldType.TOTAL);
			totalEF.setValue("" + total);
			workflow.getExtractedFields().add(totalEF);
			// extractedFieldDao.saveAndFlush(totalEF);
		}
		if (finderResult.hasValuesForType(PaymentFieldType.TOTAL_BEFORE_TAXES)) {
			Double totalBeforeTaxes = finderResult.<Double>getValue(PaymentFieldType.TOTAL_BEFORE_TAXES);
			ExtractedField totalBeforeTaxesEF = new ExtractedField();
			totalBeforeTaxesEF.setInvoiceWorkflow(workflow);
			totalBeforeTaxesEF.setPaymentFieldType(PaymentFieldType.TOTAL_BEFORE_TAXES);
			totalBeforeTaxesEF.setValue("" + totalBeforeTaxes);
			workflow.getExtractedFields().add(totalBeforeTaxesEF);
			// extractedFieldDao.saveAndFlush(totalBeforeTaxesEF);
		}
		if (finderResult.hasValuesForType(PaymentFieldType.NAME)) {
			String name = finderResult.<String>getValue(PaymentFieldType.NAME);
			ExtractedField nameEF = new ExtractedField();
			nameEF.setInvoiceWorkflow(workflow);
			nameEF.setPaymentFieldType(PaymentFieldType.NAME);
			nameEF.setValue(name);
			workflow.getExtractedFields().add(nameEF);
			// extractedFieldDao.saveAndFlush(nameEF);
		}
		if (finderResult.hasValuesForType(PaymentFieldType.INVOICE_ID)) {
			String invoiceID = finderResult.<String>getValue(PaymentFieldType.INVOICE_ID);
			ExtractedField invoiceIDEF = new ExtractedField();
			invoiceIDEF.setInvoiceWorkflow(workflow);
			invoiceIDEF.setPaymentFieldType(PaymentFieldType.INVOICE_ID);
			invoiceIDEF.setValue(invoiceID);
			workflow.getExtractedFields().add(invoiceIDEF);
			// extractedFieldDao.saveAndFlush(invoiceIDEF);
		}
		if (finderResult.hasValuesForType(PaymentFieldType.ISSUE_DATE)) {
			Date issue = finderResult.<Date>getValue(PaymentFieldType.ISSUE_DATE);
			String issueAsString = TimeHelper.format(issue);
			ExtractedField issueEF = new ExtractedField();
			issueEF.setInvoiceWorkflow(workflow);
			issueEF.setPaymentFieldType(PaymentFieldType.ISSUE_DATE);
			issueEF.setValue(issueAsString);
			workflow.getExtractedFields().add(issueEF);
			// extractedFieldDao.saveAndFlush(issueEF);
		}
		if (finderResult.hasValuesForType(PaymentFieldType.VATIN)) {
			StringBuilder sb = new StringBuilder();
			Iterator<? extends Candidate> it = JavaConverters.asJavaIterator(finderResult.getCandidates(PaymentFieldType.VATIN).iterator());
			while (it.hasNext()) {
				Candidate candidate = it.next();
				sb.append((String)candidate.getValue());
				if (it.hasNext()) {
					sb.append(',');
				}
			}
			ExtractedField issueEF = new ExtractedField();
			issueEF.setInvoiceWorkflow(workflow);
			issueEF.setPaymentFieldType(PaymentFieldType.VATIN);
			issueEF.setValue(sb.toString());
			workflow.getExtractedFields().add(issueEF);
			// extractedFieldDao.saveAndFlush(issueEF);
		} else if (SupportedLocales.ITALIAN_LANG_CODE.equals(workflow.getLocale())) {
			ExtractedField issueEF = new ExtractedField();
			issueEF.setInvoiceWorkflow(workflow);
			issueEF.setPaymentFieldType(PaymentFieldType.VATIN);
			issueEF.setValue("");
			workflow.getExtractedFields().add(issueEF);
		}
	}

}
