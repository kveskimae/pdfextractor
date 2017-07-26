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

import org.pdfextractor.algorithm.finder.FinderResult;
import org.pdfextractor.db.dao.InvoiceWorkflowDao;
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.pdfextractor.db.domain.dictionary.PaymentFieldType;
import org.pdfextractor.db.exception.*;
import org.pdfextractor.db.util.TimeHelper;
import org.pdfextractor.services.config.InitializationAttributes;
import org.pdfextractor.dto.composite.PollAnswerDTO;
import org.pdfextractor.dto.domain.InvoiceWorkflowDTO;
import org.pdfextractor.services.util.SecurityUtil;
import org.pdfextractor.db.domain.dictionary.SupportedLocales;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class InvoiceWorkflowService {

	private static Logger log = LoggerFactory.getLogger(InvoiceWorkflowService.class);

	@Autowired
	private FileSaver fileSaver;

	@Autowired
	private ExtractedFieldService extractedFieldService;

	@Autowired
	private ExtractorService extractorService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private InvoiceWorkflowDao invoiceWorkflowDao;

	@Autowired
	private UserService userService;

	// @Autowired
	// private FingerprintDao fingerprintDao; TODO

	@Autowired
	private Mapper dozerBeanMapper;

	@Value( "${" + InitializationAttributes.SELF_HOST_ADDRESS + "}" )
	private String hostName;

	@PostConstruct
	public void init(){
		if (StringUtils.isEmpty(hostName)) {
			throw new BeanInitializationException("Property " + InitializationAttributes.SELF_HOST_ADDRESS + " specifying host machine location for REST resources is empty.");
		}
	}

	public static InvoiceWorkflow createNew(final SecurityUser securityUser, final String fileName, final Locale locale) {
		InvoiceWorkflow workflow = new InvoiceWorkflow();
		workflow.setFileName(fileName);
		workflow.setLocale(locale.getLanguage());
		workflow.setState(InvoiceWorkflowState.pending_review);
		workflow.setInsertTime(LocalDateTime.now());
		workflow.setSecurityUser(securityUser);
		// securityUser.getInvoiceWorkflows().add(workflow);
		// invoiceWorkflowDao.save(workflow);
		return workflow;
	}

	public static boolean isForbiddenToSee(final Principal principal, final InvoiceWorkflow workflow) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		Integer principalId = securityUser.getId();
		boolean notReviewer = !securityUser.isReviewer();
		return notReviewer && (workflow.getSecurityUser().getId() != principalId);
	}

	public Object handleInvoice(final Principal principal, final String locale, final byte[] file) {
		SecurityUser principalAsSecurityUser = SecurityUtil.findSecurityUser(principal);
		checkTrialsLimit(principalAsSecurityUser);
		switch (locale) {
			case SupportedLocales.ITALIAN_LANG_CODE:
				emailService.notifyReviewersOfNewInvoice(principalAsSecurityUser.getUsername());
				Object ret = handleInvoiceUpload(principalAsSecurityUser, file, SupportedLocales.ITALY);
				return ret;
			case SupportedLocales.ESTONIAN_LANG_CODE:
				return handleInvoiceUpload(principalAsSecurityUser, file, SupportedLocales.ESTONIA);
			default:
				return new AppBadInputException("Unsupported locale: " + locale);
		}
	}

	private void checkTrialsLimit(final SecurityUser securityUser) {
		if (securityUser.isTrialAccount() && userService.countNumberOfSubmittedInvoices(securityUser) >= securityUser.getTrialLimit()) {
			throw new AppTrialLimitExceededException();
		}
	}

	public Object handleInvoiceUpload(final SecurityUser securityUser, final byte[] file, final Locale locale) throws AppIOException {
		// Save invoice file
		String fileName = null;
		try {
			fileName = fileSaver.save(file);
		} catch (IOException ioe) {
			throw new AppIOException("Failure while saving invoice file", ioe);
		}
		// Create invoice workflow
		InvoiceWorkflow workflow = createNew(securityUser, fileName, locale);
		// Extract data
		FinderResult finderResult = extractorService.extractPaymentData(file, locale);
		// Create DTO
		// TODO findBypIdAndXAndY fingerprint and verify if corrected layout
		// TODO use Spring transactions
		extractedFieldService.saveExtractedFields(finderResult, workflow);
		invoiceWorkflowDao.saveAndFlush(workflow);
		InvoiceWorkflowDTO dto = dozerBeanMapper.map(workflow, InvoiceWorkflowDTO.class);
		dto.afterMapped(hostName);
		// InvoiceWorkflowDTOMapper.fillDTOWithFinderResult(dto, finderResult);
		dto.setExtractedData(null);
		return dto;
	}

	public InvoiceWorkflowDTO findById(final Integer id, final Principal principal) throws AppAbstractException {
		InvoiceWorkflow workflow = invoiceWorkflowDao.findOne(id);
		if (workflow == null) {
			throw new AppResourceNotFoundException("No workflow found with id " + id);
		}
		if (InvoiceWorkflowService.isForbiddenToSee(principal, workflow)) {
			throw new AppAccessForbiddenException("User " + SecurityUtil.findUsername(principal) + " has no right to see workflow " + id);
		}
		return InvoiceWorkflowDTO.mapToDTO(workflow, dozerBeanMapper, hostName);
	}

	public PollAnswerDTO findForClientByLocaleAndLastPollTime(final String locale, Long posixTime, final Principal principal) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		PollAnswerDTO pollAnswerDTO = new PollAnswerDTO();
		pollAnswerDTO.setLastPollTime(TimeHelper.getUTCEpochSecondsForNow());
		LocalDateTime lastPollTime = TimeHelper.convertUTCEpochSecondsToLocalDateTime(posixTime);
		List<InvoiceWorkflow> workflowsInsertedAfterLastPoll = invoiceWorkflowDao.findBySecurityUserAndStateAndLocaleAndInsertTimeAfter(securityUser, InvoiceWorkflowState.pending_review, locale, lastPollTime);
		List<InvoiceWorkflow> workflowsCompletedAfterLastPoll = invoiceWorkflowDao.findBySecurityUserAndStateAndLocaleAndCompleteTimeAfter(securityUser, InvoiceWorkflowState.completed, locale, lastPollTime);
		// TODO Bug: not returning failed invoices!
		List<InvoiceWorkflow> workflowsAfterLastPoll = new ArrayList<>();
		workflowsAfterLastPoll.addAll(workflowsInsertedAfterLastPoll);
		workflowsAfterLastPoll.addAll(workflowsCompletedAfterLastPoll);
		pollAnswerDTO.setInvoiceWorkflows(InvoiceWorkflowDTO.mapToDtos(workflowsAfterLastPoll, dozerBeanMapper, hostName));
		return pollAnswerDTO;
	}

	public InvoiceWorkflowDTO updateOne(final Integer id, final InvoiceWorkflowDTO invoiceWorkflowDTO) {
		InvoiceWorkflow invoiceWorkflow = invoiceWorkflowDao.findOne(id);
		for (PaymentFieldType fieldType : invoiceWorkflowDTO.getExtractedData().keySet()) {
			extractedFieldService.saveOrUpdateExtractedField(fieldType, invoiceWorkflowDTO.getExtractedData().get(fieldType), invoiceWorkflow);
		}
		if (isStateChangedToComplete(invoiceWorkflow, invoiceWorkflowDTO)) {
			invoiceWorkflow.setCompleteTime(LocalDateTime.now());
		}
		invoiceWorkflow.setState(invoiceWorkflowDTO.getState());
		invoiceWorkflowDao.saveAndFlush(invoiceWorkflow);
		InvoiceWorkflowDTO ret = dozerBeanMapper.map(invoiceWorkflow, InvoiceWorkflowDTO.class);
		ret.afterMapped(hostName);
		return ret;
	}

	private static boolean isStateChangedToComplete(final InvoiceWorkflow invoiceWorkflow, final InvoiceWorkflowDTO invoiceWorkflowDTO) {
		return InvoiceWorkflowState.completed.equals(invoiceWorkflowDTO.getState()) && !InvoiceWorkflowState.completed.equals(invoiceWorkflow.getState());
	}

	public PollAnswerDTO findAllByLocaleAndStateAndLastPollTime(final String locale, final InvoiceWorkflowState state, final Long lastPollPosixTime) {
		LocalDateTime lastPollTime = TimeHelper.convertUTCEpochSecondsToLocalDateTime(lastPollPosixTime);
		List<InvoiceWorkflow> workflowsAfterLastPoll = invoiceWorkflowDao.findByStateAndLocaleAndInsertTimeAfter(state, locale, lastPollTime);
		PollAnswerDTO pollAnswerDTO = new PollAnswerDTO();
		pollAnswerDTO.setInvoiceWorkflows(InvoiceWorkflowDTO.mapToDtos(workflowsAfterLastPoll, dozerBeanMapper, hostName));
		pollAnswerDTO.setLastPollTime(TimeHelper.getUTCEpochSecondsForNow());
		return pollAnswerDTO;
	}

}
