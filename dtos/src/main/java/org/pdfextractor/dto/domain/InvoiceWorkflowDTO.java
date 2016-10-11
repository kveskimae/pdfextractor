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

import org.pdfextractor.db.domain.ExtractedField;
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.pdfextractor.dto.CollectionNames;
import org.pdfextractor.dto.link.LinksDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.pdfextractor.db.domain.dictionary.SupportedLocales;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.dozer.Mapper;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceWorkflowDTO extends LinksDTO {

	private Integer id;

	private String locale;

	private InvoiceWorkflowState state;

	private String fileName;

	private Map<PaymentFieldType, Object> extractedData = new HashMap<>();

	public InvoiceWorkflowDTO() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Integer getId() {
		return id;
	}

	@Override
	@JsonIgnore
	public String getCollectionName() {
		return CollectionNames.INVOICE_WORKFLOWS;
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

	public InvoiceWorkflowState getState() {
		return state;
	}

	public void setState(InvoiceWorkflowState state) {
		this.state = state;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<PaymentFieldType, Object> getExtractedData() {
		return extractedData;
	}

	public void setExtractedData(Map<PaymentFieldType, Object> extractedData) {
		this.extractedData = extractedData;
	}

	public static InvoiceWorkflowDTO mapToDTO(final InvoiceWorkflow workflow, Mapper dozerBeanMapper, String hostName) {
		InvoiceWorkflowDTO ret = dozerBeanMapper.map(workflow, InvoiceWorkflowDTO.class);
		addExtractedFieldsToDTO(workflow, ret);
		ret.afterMapped(hostName);
		return ret;
	}

	public static List<InvoiceWorkflowDTO> mapToDtos(final List<InvoiceWorkflow> workflows, final Mapper dozerBeanMapper, final String hostName) {
		List<InvoiceWorkflowDTO> dtos = new ArrayList<>();
		for (InvoiceWorkflow workflow : workflows) {
			InvoiceWorkflowDTO invoiceWorkflowDTO = dozerBeanMapper.map(workflow, InvoiceWorkflowDTO.class);
			addExtractedFieldsToDTO(workflow, invoiceWorkflowDTO);
			invoiceWorkflowDTO.afterMapped(hostName);
			dtos.add(invoiceWorkflowDTO);
		}
		return dtos;
	}

	public static void addExtractedFieldsToDTO(final InvoiceWorkflow workflow, final InvoiceWorkflowDTO invoiceWorkflowDTO) {
		for (ExtractedField ef : workflow.getExtractedFields()) {
			String value = ef.getValue();
			if ((PaymentFieldType.TOTAL.equals(ef.getPaymentFieldType()) || PaymentFieldType.TOTAL_BEFORE_TAXES.equals(ef.getPaymentFieldType())) && ef.getValue() != null) {
				Double valueAsDouble = Double.parseDouble(value);
				invoiceWorkflowDTO.getExtractedData().put(ef.getPaymentFieldType(), valueAsDouble);
			} else if (PaymentFieldType.VATIN.equals(ef.getPaymentFieldType())) {
				if (value != null && value.length() > 0) {
					String[] tokens = value.split(",");
					List<String> vatinValues = Arrays.asList(tokens);
					invoiceWorkflowDTO.getExtractedData().put(ef.getPaymentFieldType(), vatinValues);
				} else {
					invoiceWorkflowDTO.getExtractedData().put(ef.getPaymentFieldType(), new ArrayList<>());
				}
			} else {
				invoiceWorkflowDTO.getExtractedData().put(ef.getPaymentFieldType(), ef.getValue());
			}
		}
		if (SupportedLocales.ITALIAN_LANG_CODE.equals(workflow.getLocale()) && invoiceWorkflowDTO.getExtractedData().get(PaymentFieldType.VATIN) == null) {
			invoiceWorkflowDTO.getExtractedData().put(PaymentFieldType.VATIN, new ArrayList<>());
		}
	}
}
