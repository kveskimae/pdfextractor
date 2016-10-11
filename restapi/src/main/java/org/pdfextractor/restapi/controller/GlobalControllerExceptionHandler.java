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

import org.pdfextractor.db.exception.AppAccessForbiddenException;
import org.pdfextractor.db.exception.AppResourceNotFoundException;
import org.pdfextractor.db.exception.AppServerFaultException;
import org.pdfextractor.db.exception.AppTrialLimitExceededException;
import org.pdfextractor.dto.error.ErrorInfo;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> defaultErrorHandler(final HttpServletRequest req, final Exception exc) {
		log.error("Error while handling REST API request", exc); // TODO IP & user
		ErrorInfo ret = new ErrorInfo(req.getRequestURL().toString(), exc.getMessage());
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (exc instanceof AppAccessForbiddenException) {
			status = HttpStatus.FORBIDDEN;
		} else if (exc instanceof AppResourceNotFoundException) {
			status = HttpStatus.NOT_FOUND;
		} else if (exc instanceof AppTrialLimitExceededException) {
			status = HttpStatus.FORBIDDEN;
		} else if (exc instanceof AppServerFaultException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} else if (exc instanceof PersistenceException) {
			if (exc.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException constraintViolationException = (ConstraintViolationException)exc.getCause();
				SQLException sqlException = constraintViolationException.getSQLException();
				if (sqlException != null) {
					status = HttpStatus.UNPROCESSABLE_ENTITY;
					ret = new ErrorInfo(ret.url, sqlException.getMessage());
				}
			}
		}
		return new ResponseEntity<>(ret, status);
	}

}
