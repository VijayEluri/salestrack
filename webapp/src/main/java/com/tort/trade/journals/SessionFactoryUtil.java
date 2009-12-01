package com.tort.trade.journals;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.tort.trade.model.Good;

public class SessionFactoryUtil {

	private static SessionFactory __sessionFactory;
	
	static{
		__sessionFactory = new AnnotationConfiguration().configure()
		.addAnnotatedClass(Good.class)
		.buildSessionFactory();
	}

	public SessionFactory getSessionFactory() {
		return __sessionFactory;
	}

}
