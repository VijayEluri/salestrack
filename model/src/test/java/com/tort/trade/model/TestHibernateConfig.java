package com.tort.trade.model;

import org.hibernate.cfg.AnnotationConfiguration;

public class TestHibernateConfig {

	private static AnnotationConfiguration _annotationConfiguration;
	
	static{
		_annotationConfiguration = new AnnotationConfiguration();
		_annotationConfiguration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		_annotationConfiguration.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
		_annotationConfiguration.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:ft");
		_annotationConfiguration.setProperty("hibernate.connection.username", "sa");
		_annotationConfiguration.setProperty("hibernate.connection.password", "");
		_annotationConfiguration.setProperty("hibernate.connection.pool_size", "1");
		_annotationConfiguration.setProperty("hibernate.connection.autocommit", "false");
		_annotationConfiguration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		_annotationConfiguration.setProperty("hibernate.show_sql", "true");
	}

	public AnnotationConfiguration get() {				
		return _annotationConfiguration;
	}

}

