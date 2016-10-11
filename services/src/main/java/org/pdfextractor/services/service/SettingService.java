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

import org.pdfextractor.db.dao.SettingDao;
import org.pdfextractor.db.domain.SecurityUser;
import org.pdfextractor.db.domain.Setting;
import org.pdfextractor.db.domain.dictionary.SettingType;
import org.pdfextractor.dto.domain.SettingDTO;
import org.pdfextractor.db.exception.AppAccessForbiddenException;
import org.pdfextractor.services.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class SettingService {

	private static Logger log = LoggerFactory.getLogger(SettingService.class);

	@Autowired
	private SettingDao settingDao;


	public List<Setting> findAll() {
		List<Setting> ret = settingDao.findAll();
		return ret;
	}

	public Setting findOneByType(final SettingType settingType) {
		Setting ret = settingDao.findBySettingType(settingType);
		return ret;
	}

	public Setting update(final SettingType settingType, final SettingDTO settingDTO, final Principal principal) {
		SecurityUser securityUser = SecurityUtil.findSecurityUser(principal);
		if (!securityUser.isAdmin()) {
			throw new AppAccessForbiddenException("Only admin may change settings");
		}
		Setting ret = settingDao.findBySettingType(settingType);
		ret.setValue(settingDTO.getValue());
		settingDao.saveAndFlush(ret);
		return ret;
	}
}
