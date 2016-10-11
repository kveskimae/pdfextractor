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

package org.pdfextractor.db.config;


import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackages = "org.pdfextractor.db.dao")
@EnableTransactionManagement
@Import({JndiDataConfig.class})
public class JpaConfig {

	@Autowired
	private DataConfig dataConfig;

	@Bean
	public DataConfig dataConfig() {
		return new JndiDataConfig();
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataConfig.dataSource());
		entityManagerFactory.setPackagesToScan(new String[]{"org.pdfextractor.db.domain", "org.springframework.data.jpa.convert.threeten"});
		// entityManagerFactory.getJpaPropertyMap().put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy"); // Hibernate 4 used this
		entityManagerFactory.getJpaPropertyMap().put("hibernate.physical_naming_strategy", "org.pdfextractor.db.util.PhysicalNamingStrategyImpl");
		entityManagerFactory.getJpaPropertyMap().put("hibernate.show_sql", "true");
		entityManagerFactory.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
		entityManagerFactory.getJpaPropertyMap().put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory"); // use SingletonEhCacheRegionFactory for singleton
		entityManagerFactory.getJpaPropertyMap().put("hibernate.cache.use_second_level_cache", "true");
		entityManagerFactory.getJpaPropertyMap().put("hibernate.cache.use_query_cache", "true"); // HQL
		entityManagerFactory.getJpaPropertyMap().put("net.sf.ehcache.configurationResourceName", "ehcache_conf.xml");
		// entityManagerFactory.getJpaPropertyMap().put("hibernate.current_session_context_class", "managed");
		// entityManagerFactory.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "validate");
		entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
		entityManagerFactory.afterPropertiesSet();
		return entityManagerFactory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory());
		return transactionManager;
	}

}
