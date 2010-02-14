package com.tort.trade.journals;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;

public class SessionFactoryUtil {

	private static SessionFactory __sessionFactory;
	
	static{
		__sessionFactory = new AnnotationConfiguration().configure()
		.addAnnotatedClass(Good.class)
		.addAnnotatedClass(SalesAlias.class)
		.addAnnotatedClass(Sales.class)
		.addAnnotatedClass(Transition.class)
		.buildSessionFactory();
	}

	public SessionFactory getSessionFactory() {
		return __sessionFactory;
	}

}
