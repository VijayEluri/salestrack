package com.tort.replicator;

import java.util.List;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tort.trade.model.Transition;
import com.tort.trade.model.Sales;
import com.tort.trade.model.Good;
import java.sql.Connection;
import java.sql.Statement;

public class Replicator {
	private Session _srcSession;
	private Session _destSession;
	
	public static void main(String[] args) throws Exception{
		new Replicator().replicate();
	}

	private void replicate() throws Exception{
		_srcSession = new HibernateHelper().getSrcSessionFactory().openSession();
		_destSession = new HibernateHelper().getDestSessionFactory().openSession();
		Transaction srcTx = _srcSession.beginTransaction();
		Transaction destTx = _destSession.beginTransaction();
		
		replicateData(loadData(Sales.class));
		replicateData(loadData(Good.class));
		replicateData(loadData(Transition.class));
		
		destTx.commit();
		srcTx.commit();
		Connection connection = _destSession.connection();
		Statement statement = connection.createStatement();
		statement.execute("SHUTDOWN");
		statement.close();
		_destSession.close();
		_srcSession.close();
	}

	private <T> void replicateData(List<T> classes) {
		for (T clazz : classes) {			
			_destSession.replicate(clazz, ReplicationMode.LATEST_VERSION);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> loadData(Class<?> T) {		
		List<T> classes = _srcSession.createCriteria(T).list();				
		
		return classes;
	}
}