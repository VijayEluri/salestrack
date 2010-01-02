package com.tort.replicator;

import java.util.List;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;

import com.tort.trade.model.Transition;

public class Replicator {
	private Session _srcSession;
	private Session _destSession;
	
	public static void main(String[] args) {
		new Replicator().replicate();
	}

	private void replicate() {
		_srcSession = new HibernateHelper().getSrcSessionFactory().openSession();
		_destSession = new HibernateHelper().getDestSessionFactory().openSession();		
		
		replicateData(loadData(Transition.class));
		
		_srcSession.close();
		_destSession.close();
	}

	private <T> void replicateData(List<T> classes) {
		for (T clazz : classes) {			
			_destSession.replicate(clazz, ReplicationMode.LATEST_VERSION);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> loadData(Class<?> T) {		
		List<T> goods = _srcSession.createCriteria(T).list();				
		
		return goods;
	}
}