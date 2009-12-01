package com.tort.replicator;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateHelper {	

	private static SessionFactory srcSessionFactory;
	private static SessionFactory destSessionFactory;

	public SessionFactory getSrcSessionFactory() {
		File file = new File(".");
		System.out.println(file.getAbsolutePath());
		if(srcSessionFactory == null){						
			srcSessionFactory = new AnnotationConfiguration()
									.configure(new File("src.hibernate.cfg.xml"))
									.buildSessionFactory();			
		}
		
		return srcSessionFactory;
	}

	public SessionFactory getDestSessionFactory() {
		if(destSessionFactory == null){						
			destSessionFactory = new AnnotationConfiguration()
									.configure(new File("dest.hibernate.cfg.xml"))
									.buildSessionFactory();			
		}
		
		return destSessionFactory;
	}

}
