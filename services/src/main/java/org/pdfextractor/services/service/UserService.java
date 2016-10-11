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
import org.pdfextractor.db.dao.SecurityUserDao;
import org.pdfextractor.db.domain.SecurityAuthority;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.domain.dictionary.AppAuthority;
import org.pdfextractor.db.domain.dictionary.InvoiceWorkflowState;
import org.pdfextractor.dto.composite.*;
import org.pdfextractor.dto.domain.*;
import org.pdfextractor.db.exception.AppAccessForbiddenException;
import org.pdfextractor.db.exception.AppBadInputException;
import org.pdfextractor.db.exception.AppResourceNotFoundException;
import org.pdfextractor.services.util.SecurityUtil;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private SecurityUserDao securityUserDao;

	@Autowired
	private InvoiceWorkflowDao invoiceWorkflowDao;

	@Autowired
	private SecurityAuthorityDao securityAuthorityDao;

	@Autowired
	private Mapper dozerBeanMapper;


	public StatisticsDTO getStatisticsByClientId(final Integer id) {
		SecurityUser securityUser = findExistingById(id);
		if (!securityUser.isClient()) {
			throw new AppBadInputException("User id has no client role: " + id);
		}
		LocalDateTime beginningOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), java.time.LocalTime.MIDNIGHT);
		Long totalThisMonth = invoiceWorkflowDao.countBySecurityUserAndCompleteTimeAfter(securityUser, beginningOfMonth);
		StatisticsDTO ret = new StatisticsDTO();
		Long countOfSubmittedInvoices = countNumberOfSubmittedInvoices(securityUser);
		ret.setTotalNumber(countOfSubmittedInvoices);
		ret.setTotalThisMonth(totalThisMonth);
		return ret;
	}

	public Long countNumberOfSubmittedInvoices(final SecurityUser securityUser) {
		Long totalNumber = 0L;
		totalNumber += invoiceWorkflowDao.countBySecurityUserAndState(securityUser, InvoiceWorkflowState.completed);
		totalNumber += invoiceWorkflowDao.countBySecurityUserAndState(securityUser, InvoiceWorkflowState.pending_review);
		return totalNumber;
	}

	public PeriodStatisticsDTO getStatisticsByClientIdForPeriod(final Integer id, final LocalDate startDate, final LocalDate endDate) {
		SecurityUser securityUser = securityUserDao.findOne(id);
		LocalDateTime startMidnight = LocalDateTime.of(startDate, java.time.LocalTime.MIDNIGHT);
		LocalDateTime endMidnight = LocalDateTime.of(endDate, java.time.LocalTime.MIDNIGHT);
		Long totalBetweenDates = invoiceWorkflowDao.countBySecurityUserAndCompleteTimeBetween(securityUser, startMidnight, endMidnight);
		PeriodStatisticsDTO ret = new PeriodStatisticsDTO();
		ret.setTotalBetweenDates(totalBetweenDates);
		return ret;
	}

	public SecurityUserDTO findOneById(final Integer id) {
		SecurityUser securityUser = findExistingNonAdminById(id);
		SecurityUserDTO ret = dozerBeanMapper.map(securityUser, SecurityUserDTO.class);
		return ret;
	}

	private SecurityUser findExistingNonAdminById(final Integer id) {
		SecurityUser securityUser = findExistingById(id);
		checkThatIsNotAdmin(securityUser);
		return securityUser;
	}

	private void checkThatIsNotAdmin(final SecurityUser securityUser) {
		if (securityUser.isAdmin()) {
			throw new AppAccessForbiddenException("User is admin: " + securityUser.getUsername());
		}
	}

	private SecurityUser findExistingById(final Integer id) {
		SecurityUser securityUser = securityUserDao.findOne(id);
		if (securityUser == null) {
			throw new AppResourceNotFoundException("No user found with id " + id);
		}
		return securityUser;
	}

	public void deleteOneById(final Integer id) {
		SecurityUser securityUser = findExistingNonAdminById(id);
		securityUserDao.delete(securityUser);
		securityUserDao.flush();
	}

	public SecurityUserDTO updateOne(final Integer id, final SecurityUserDTO securityUserDTO) {
		SecurityUser securityUser = findExistingNonAdminById(id);
		securityUser.setUsername(securityUserDTO.getUsername());
		securityUser.setEmail(securityUserDTO.getEmail());
		securityUser.setPassword(securityUserDTO.getPassword());
		securityUser.setEnabled(securityUserDTO.isEnabled());
		securityUser.setAccountType(securityUserDTO.getAccountType());
		securityUser.setTrialLimit(securityUserDTO.getTrialLimit());
		securityUserDao.saveAndFlush(securityUser);
		SecurityUserDTO ret = dozerBeanMapper.map(securityUser, SecurityUserDTO.class);
		return ret;
	}

	public List<SecurityUserDTO> findAllByAuthority(final AppAuthority authority) {
		SecurityAuthority securityAuthority = securityAuthorityDao.findByAuthority(authority.toString());
		Collection<SecurityUser> securityUsers = securityAuthority.getSecurityUsers();
		List<SecurityUserDTO> dtos = new ArrayList<>();
		for (SecurityUser securityUser : securityUsers) {
			SecurityUserDTO securityUserDTO = dozerBeanMapper.map(securityUser, SecurityUserDTO.class);
			dtos.add(securityUserDTO);
		}
		return dtos;
	}

	public SecurityUserDTO createOne(final AppAuthority authority, final SecurityUserDTO securityUserDTO) {
		if (AppAuthority.ADMIN.equals(authority)) {
			throw new AppAccessForbiddenException("Creating admin is not allowed");
		}
		SecurityAuthority securityAuthority = securityAuthorityDao.findByAuthority(authority.toString());
		if (securityAuthority == null) {
			throw new AppBadInputException("No corresponding authority found in database for " + authority);
		}
		SecurityUser securityUser = dozerBeanMapper.map(securityUserDTO, SecurityUser.class);
		securityUser.setCredentialsNonExpired(true);
		securityUser.setAccountNonLocked(true);
		securityUser.setAccountNonExpired(true);
		securityUser.getAuthorities().add(securityAuthority);
		securityUserDao.saveAndFlush(securityUser);
		SecurityUserDTO ret = dozerBeanMapper.map(securityUser, SecurityUserDTO.class);
		return ret;
	}

	public BootstrapDataDTO getBootstrapData(final Principal principal) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		log.info("Retrieving bootstrap info for " + securityUser.getUsername());
		LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		Long workflowsCompletedToday = invoiceWorkflowDao.countByStateAndLocaleAndCompleteTimeAfter(InvoiceWorkflowState.completed, "it", todayMidnight);
		BootstrapDataDTO bootstrapDataDTO = new BootstrapDataDTO();
		bootstrapDataDTO.setCompletedToday(workflowsCompletedToday);
		bootstrapDataDTO.setUsername(securityUser.getUsername());
		bootstrapDataDTO.setEmail(securityUser.getEmail());
		bootstrapDataDTO.setTrialLimit(securityUser.getTrialLimit());
		for (SecurityAuthority authority : securityUser.getAuthorities()) {
			bootstrapDataDTO.getAuthorities().add(authority.getAuthority());
		}
		return bootstrapDataDTO;
	}

	public void updateEmail(final EmailEditDTO emailEditDTO, final Principal principal) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		log.info("Updating email for user '" + securityUser.getUsername() + "'");
		if (!securityUser.getPassword().equals(emailEditDTO.getPasswordForChangingEmail())) {
			throw new AppBadInputException("Supplied password is incorrect");
		}
		securityUser.setEmail(emailEditDTO.getNewEmail());
		securityUserDao.saveAndFlush(securityUser);
	}

	public void updatePassword(final PasswordEditDTO passwordEditDTO, final Principal principal) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		log.info("Updating password for user '" + securityUser.getUsername() + "'");
		if (!securityUser.getPassword().equals(passwordEditDTO.getOldPassword())) {
			throw new AppBadInputException("Supplied old password is incorrect");
		}
		System.out.println("new password=" + passwordEditDTO.getNewPassword());
		securityUser.setPassword(passwordEditDTO.getNewPassword());
		securityUserDao.saveAndFlush(securityUser);
	}

}
