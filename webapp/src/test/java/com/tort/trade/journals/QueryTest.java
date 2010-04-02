package com.tort.trade.journals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class QueryTest {
	protected SessionFactory _sessionFactory;
	protected Session _session;

	@BeforeClass
	public void setUpClass(){
		_sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	}
	
	@BeforeMethod
	public void setUp(){
		_session = _sessionFactory.openSession();
	}
	
	@AfterMethod
	public void tearDown(){
		_session.close();
	}
}
