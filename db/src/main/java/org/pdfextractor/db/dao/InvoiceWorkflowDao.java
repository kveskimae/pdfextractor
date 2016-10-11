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

package org.pdfextractor.db.dao;

import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceWorkflowDao extends JpaRepository<InvoiceWorkflow, Integer>, JpaSpecificationExecutor<InvoiceWorkflow> {

	List<InvoiceWorkflow> findByState(InvoiceWorkflowState state);

	List<InvoiceWorkflow> findByStateAndLocale(InvoiceWorkflowState state, String locale);

	InvoiceWorkflow findByFileName(String fileName);

	List<InvoiceWorkflow> findByStateAndLocaleAndInsertTimeAfter(InvoiceWorkflowState state, String locale, LocalDateTime date);

	List<InvoiceWorkflow> findBySecurityUserAndStateAndLocaleAndInsertTimeAfter(SecurityUser securityUser, InvoiceWorkflowState state, String locale, LocalDateTime date);

	// List<InvoiceWorkflow> findByStateAndLocaleAndCompleteTimeAfter(InvoiceWorkflowState state, String locale, LocalDateTime date);

	List<InvoiceWorkflow> findBySecurityUserAndStateAndLocaleAndCompleteTimeAfter(SecurityUser securityUser, InvoiceWorkflowState state, String locale, LocalDateTime date);

	Long countByStateAndLocaleAndCompleteTimeAfter(InvoiceWorkflowState state, String locale, LocalDateTime date);

	Long countBySecurityUserAndCompleteTimeBetween(SecurityUser securityUser, LocalDateTime startDate, LocalDateTime endDate);

	Long countBySecurityUserAndState(SecurityUser securityUser, InvoiceWorkflowState state);

	Long countBySecurityUserAndCompleteTimeAfter(SecurityUser securityUser, LocalDateTime date);

	Long countByStateAndLocale(InvoiceWorkflowState state, String locale);
}