package com.tort.trade.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class EntityTest {

	protected Session _session;
	protected SessionFactory _sessionFactory;
	
	@BeforeClass
	public void setUpClass() {
		AnnotationConfiguration annotationConfiguration = new TestHibernateConfig().get();
		
		for (Class clazz : getClasses()) {
			annotationConfiguration.addAnnotatedClass(clazz);
		}
		
		_sessionFactory = annotationConfiguration										
										.buildSessionFactory();
	}
	@BeforeMethod
	public void setUp() {
		_session = _sessionFactory.openSession();		
	}
	@AfterMethod
	public void tearDown() {
		_session.close();
	}

	protected abstract List<Class> getClasses();
}
