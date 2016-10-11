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

package org.pdfextractor.services.config;

import org.pdfextractor.services.service.EmailService;
import org.pdfextractor.services.service.FileSaver;
import org.apache.commons.lang3.math.NumberUtils;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableAsync
@EnableScheduling
@PropertySource("classpath:" + BaseServicesConfig.APPLICATION_PROPERTIES_FILE_NAME)
public class BaseServicesConfig {

	private static Logger log = LoggerFactory.getLogger(BaseServicesConfig.class);

	public static final String APPLICATION_PROPERTIES_FILE_NAME = "application.properties";

	@Value( "${" + InitializationAttributes.SAVE_INVOICES + "}" )
	private Boolean saveInvoices;

	@Value( "${" + InitializationAttributes.SAVE_LOCATION + "}" )
	private String saveLocation;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JavaMailSenderImpl mailSender() throws IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(props);
		return mailSender;
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler ret = new ThreadPoolTaskScheduler();
		ret.setPoolSize(1);
		return ret;
	}

	@Bean
	public DozerBeanMapperFactoryBean mapper() throws IOException {
		DozerBeanMapperFactoryBean mapper = new DozerBeanMapperFactoryBean();
		return mapper;
	}

	@Bean
	public FileSaver fileSaver() {
		if (saveInvoices) {
			File file = new File(saveLocation);
			if (!file.exists()) {
				throw new RuntimeException("Invoices folder location '" + saveLocation + "' does not exist");
			}
			if (!file.isDirectory()) {
				throw new RuntimeException("Invoices folder location '" + saveLocation + "' is not a folder");
			}
			if (!file.canRead()) {
				throw new RuntimeException("Invoices folder '" + saveLocation + "' is not readable for application");
			}
			if (!file.canWrite()) {
				throw new RuntimeException("Invoices folder '" + saveLocation + "' is not writable for application");
			}
			Long counterStart = getCounterStart();
			log.info("Saving submitted invoices in folder " + saveLocation + " starting from counter number " + counterStart);
			return new FileSaver(saveInvoices, saveLocation, counterStart);
		} else {
			log.info("Submitted invoices are not saved");
			return new FileSaver(saveInvoices, null, null);
		}
	}

	@Bean
	public EmailService emailService() {
		return new EmailService();
	}

	private Long getCounterStart() {
		Long counterStart = 1L;
		File file = new File(saveLocation);
		File[] filesInFolder = file.listFiles();
		if (filesInFolder != null) {
			for (File invoiceFile : filesInFolder) {
				if (invoiceFile.isFile()) {
					String name = invoiceFile.getName();
					String[] tokens = name.split("\\.");
					if (tokens.length > 0) {
						String fileNameWithoutExtension = tokens[0];
						if (NumberUtils.isDigits(fileNameWithoutExtension)) {
							Long fileNameAsNumber = Long.parseLong(fileNameWithoutExtension);
							if (fileNameAsNumber > counterStart) {
								counterStart = fileNameAsNumber;
							}
						}
					}
				}
			}
		}
		Long ret = counterStart + 1;
		return ret;
	}

}
