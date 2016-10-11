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

import org.pdfextractor.db.domain.dictionary.AppAuthority;
import org.pdfextractor.db.domain.dictionary.AppAuthorityEditor;
import org.pdfextractor.dto.CollectionNames;
import org.pdfextractor.dto.domain.SecurityUserDTO;
import org.pdfextractor.services.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/" + UserController.URL_PART)
public class UserController {

	public static final String URL_PART = CollectionNames.USERS;

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(AppAuthority.class, new AppAuthorityEditor());
	}

	@RequestMapping(value = "/all/{authority}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SecurityUserDTO>> getAll(@PathVariable("authority") final AppAuthority authority) {
		List<SecurityUserDTO> securityUserDTOs = userService.findAllByAuthority(authority);
		ResponseEntity<List<SecurityUserDTO>> ret = new ResponseEntity<>(securityUserDTOs, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SecurityUserDTO> get(@PathVariable("id") final Integer id) throws IOException {
		SecurityUserDTO securityUserDTO = userService.findOneById(id);
		ResponseEntity<SecurityUserDTO> ret = new ResponseEntity<>(securityUserDTO, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/all/{authority}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SecurityUserDTO> post(@PathVariable("authority") final AppAuthority authority, @RequestBody final SecurityUserDTO securityUserDTO) {
		SecurityUserDTO securityUserDTO1 = userService.createOne(authority, securityUserDTO);
		ResponseEntity<SecurityUserDTO> ret = new ResponseEntity<>(securityUserDTO1, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SecurityUserDTO> put(@PathVariable("id") final Integer id, @RequestBody final SecurityUserDTO securityUserDTO) throws IOException {
		SecurityUserDTO securityUserDTO1 =userService.updateOne(id, securityUserDTO);
		ResponseEntity<SecurityUserDTO> ret = new ResponseEntity<>(securityUserDTO1, HttpStatus.OK);
		return ret;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity delete(@PathVariable("id") final Integer id) throws IOException {
		userService.deleteOneById(id);
		ResponseEntity ret = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return ret;
	}

}
