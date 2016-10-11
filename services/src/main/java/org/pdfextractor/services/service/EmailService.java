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

import org.pdfextractor.db.dao.InvoiceWorkflowDao;
import org.pdfextractor.db.dao.SecurityAuthorityDao;
import org.pdfextractor.db.dao.SettingDao;
import org.pdfextractor.db.domain.SecurityAuthority;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.domain.dictionary.AppAuthority;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.pdfextractor.db.domain.dictionary.SettingType;
import org.pdfextractor.db.domain.dictionary.SupportedLocales;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EmailService {

	private static Logger log = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private SettingDao settingDao;

	@Autowired
	private SecurityAuthorityDao securityAuthorityDao;

	@Autowired
	private InvoiceWorkflowDao invoiceWorkflowDao;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	private String username, password;

	private Lock lock = new ReentrantLock();

	private SimpleMailMessage templateMessage = new SimpleMailMessage();

	@org.springframework.context.event.EventListener({ContextRefreshedEvent.class})
	public void refreshed() {
		lock.lock();
		try {
			log.info("Refreshing email settings started");
			username = settingDao.findBySettingType(SettingType.API_EMAIL_USERNAME).getValue();
			password = settingDao.findBySettingType(SettingType.API_EMAIL_PASSWORD).getValue();
			templateMessage.setFrom(username);
			mailSender.setUsername(username);
			mailSender.setPassword(password);
			log.info("Refreshing email settings completed");
		} finally {
			lock.unlock();
		}
	}

	public void notifyReviewersOfNewInvoice(final String username) {
		threadPoolTaskScheduler.execute(new SendToAllReviewersTask(username));
	}

	public class SendToAllReviewersTask implements Runnable {

		private final String username;

		public SendToAllReviewersTask(final String username) {
			this.username = username;
		}

		@Override
		public void run() {
			lock.lock();
			try {
				boolean notifyReviewersByEmail = invoiceWorkflowDao.countByStateAndLocale(InvoiceWorkflowState.pending_review, SupportedLocales.ITALIAN_LANG_CODE) < 1;
				if (notifyReviewersByEmail) {
					log.info("Submitting email for notifying reviewers of new invoice");
					sendToAllReviewers("New invoices", "New invoice received from client '" + username + "'");
				} else {
					log.info("Skipping email notification to reviewers");
				}
			} finally {
				lock.unlock();
			}
		}

		private void sendToAllReviewers(final String subject, final String text) {
			try {
				log.info("Preparing to sending text '" + text + "' via email to reviewers");
				SecurityAuthority securityAuthority = securityAuthorityDao.findByAuthority(AppAuthority.REVIEWER.toString());
				Collection<SecurityUser> securityUsers = securityAuthority.getSecurityUsers();
				List<String> emails = new ArrayList<>();
				for (SecurityUser securityUser: securityUsers) {
					if (!StringUtils.isEmpty(securityUser.getEmail())) {
						emails.add(securityUser.getEmail());
					}
				}
				if (!emails.isEmpty()) {
					String[] emailsArray = emails.toArray(new String[emails.size()]);
					SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
					msg.setTo(emailsArray);
					msg.setSubject(subject);
					msg.setText(text);
					mailSender.send(msg);
					log.info("Email sent to following reviewers: " + emails);
				} else {
					log.info("There were no reviewers to send email to");
				}
			} catch (Exception e) {
				log.error("Sending email failed", e);
			}
		} // sendToAllReviewers

	} // SendToAllReviewersTask

} // EmailService
