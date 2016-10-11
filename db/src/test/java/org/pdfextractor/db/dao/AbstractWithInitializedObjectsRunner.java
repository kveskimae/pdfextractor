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

import org.junit.Before;
import org.junit.Ignore;
import org.pdfextractor.db.domain.InvoiceWorkflow;
import org.pdfextractor.db.domain.SecurityAuthority;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.domain.dictionary.AccountType;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Ignore
public class AbstractWithInitializedObjectsRunner extends AbstractInMemoryRunner {

	protected static InvoiceWorkflow workflow1, workflow2;
	protected static SecurityAuthority securityAuthority1, securityAuthority2;
	protected static SecurityUser su;
	protected static int idx = 1;
	private static boolean setUpIsDone = false;
	@Autowired
	protected InvoiceWorkflowDao invoiceWorkflowDao;
	@Autowired
	protected SecurityAuthorityDao securityAuthorityDao;
	@Autowired
	protected SecurityUserDao securityUserDao;

	protected static InvoiceWorkflow generateWorkflow() {
		InvoiceWorkflow ret = new InvoiceWorkflow();
		ret.setFileName("file" + idx++);
		ret.setLocale("it");
		ret.setState(InvoiceWorkflowState.pending_review);
		ret.setInsertTime(LocalDateTime.now());
		ret.setSecurityUser(su);
		// su.getInvoiceWorkflows().add(ret);
		return ret;
	}

	protected static SecurityAuthority generateSecurityAuthority() {
		SecurityAuthority ret = new SecurityAuthority();
		String authority = "ROLE-" + idx++;
		ret.setAuthority(authority);
		return ret;
	}

	protected static SecurityUser generateSecurityUser() {
		SecurityUser ret = new SecurityUser();
		String username = "user" + idx++;
		ret.setUsername(username);
		ret.setAccountNonExpired(true);
		ret.setAccountNonLocked(true);
		ret.setCredentialsNonExpired(true);
		ret.setEnabled(true);
		ret.setEmail(username + "@test.com");
		ret.setPassword("abc");
		ret.setAccountType(AccountType.REGULAR);
		ret.setTrialLimit(20);
		return ret;
	}

	@Before
	public void initObjects() {
		if (setUpIsDone) {
			return;
		}

		securityAuthority1 = generateSecurityAuthority();
		securityAuthorityDao.saveAndFlush(securityAuthority1);

		securityAuthority2 = generateSecurityAuthority();
		securityAuthorityDao.saveAndFlush(securityAuthority2);

		su = generateSecurityUser();
		su.getAuthorities().add(securityAuthority1);
		su.getAuthorities().add(securityAuthority2);
		securityUserDao.saveAndFlush(su);

		workflow1 = generateWorkflow();
		invoiceWorkflowDao.saveAndFlush(workflow1);

		workflow2 = generateWorkflow();
		invoiceWorkflowDao.saveAndFlush(workflow2);

		setUpIsDone = true;
	}

}
