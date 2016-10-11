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
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(schema = "extraction")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "extractor_cache_region")
public class InvoiceWorkflow implements DomainObject {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private InvoiceWorkflowState state;

	private String locale;

	private String fileName;

	private LocalDateTime insertTime;

	private LocalDateTime completeTime;

	// TODO Do we need index on ExtractedField?
	private Collection<ExtractedField> extractedFields = new ArrayList<ExtractedField>();

	private SecurityUser securityUser;

	public InvoiceWorkflow() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Id
	@SequenceGenerator(name = "INVOICEWORKFLOWID_GENERATOR", sequenceName = "INVOICE_WORKFLOW_ID_SEQ", schema = "extraction", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICEWORKFLOWID_GENERATOR")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public InvoiceWorkflowState getState() {
		return state;
	}

	public void setState(InvoiceWorkflowState state) {
		this.state = state;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// @Temporal(TemporalType.TIMESTAMP)
	public LocalDateTime getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(LocalDateTime insertTime) {
		this.insertTime = insertTime;
	}

	// @Temporal(TemporalType.TIMESTAMP)
	public LocalDateTime getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(LocalDateTime completeTime) {
		this.completeTime = completeTime;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "invoiceWorkflow")
	public Collection<ExtractedField> getExtractedFields() {
		return extractedFields;
	}

	public void setExtractedFields(Collection<ExtractedField> extractedFields) {
		this.extractedFields = extractedFields;
	}

	public ExtractedField getExtractedFieldForType(final PaymentFieldType fieldType) {
		if (fieldType == null) {
			throw new NullPointerException("Parameter payment field type is null");
		}
		for (ExtractedField extractedField : getExtractedFields()) {
			if (fieldType.equals(extractedField.getPaymentFieldType())) {
				return extractedField;
			}
		}
		return null;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_security_user")
	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

}