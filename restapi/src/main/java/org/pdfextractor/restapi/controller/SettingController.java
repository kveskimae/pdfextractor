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

package org.pdfextractor.restapi.controller;

import org.pdfextractor.db.domain.Setting;
import org.pdfextractor.db.domain.dictionary.SettingType;
import org.pdfextractor.db.domain.dictionary.SettingTypeEditor;
import org.pdfextractor.dto.CollectionNames;
import org.pdfextractor.dto.domain.SettingDTO;
import org.pdfextractor.services.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/" + SettingController.URL_PART)
public class SettingController {

	public static final String URL_PART = CollectionNames.SETTINGS;

	private static Logger log = LoggerFactory.getLogger(SettingController.class);

	@Autowired
	private SettingService settingService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(SettingType.class, new SettingTypeEditor());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Setting> getAll() {
		return settingService.findAll();
	}

	@RequestMapping(value = "/{settingType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Setting> get(@PathVariable("settingType") final SettingType settingType) {
		Setting setting = settingService.findOneByType(settingType);
		ResponseEntity<Setting> ret = new ResponseEntity<>(setting, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{settingType}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Setting> put(@PathVariable("settingType") final SettingType settingType, @RequestBody final SettingDTO settingDTO, final Principal principal) {
		Setting setting = settingService.update(settingType, settingDTO, principal);
		ResponseEntity<Setting> ret = new ResponseEntity<>(setting, HttpStatus.OK);
		return ret;
	}

}
