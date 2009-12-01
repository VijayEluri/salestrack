package com.tort.replicator;

import java.util.List;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tort.replicator.HibernateHelper;
import com.tort.trade.model.Good;

public class Replicator {
	public static void main(String[] args) {
		replicateData(loadData());
	}

	private static void replicateData(List<Good> goods) {
		Session destSession = new HibernateHelper().getDestSessionFactory().openSession();
		Transaction tx2 = destSession.beginTransaction();
		for (Good good : goods) {			
			destSession.replicate(good, ReplicationMode.LATEST_VERSION);
		}
		tx2.commit();
		destSession.close();
	}

	private static List<Good> loadData() {
		Session srcSession = new HibernateHelper().getSrcSessionFactory().openSession();
		Transaction tx1 = srcSession.beginTransaction();
		List<Good> goods = srcSession.createCriteria(Good.class).list();
		tx1.commit();
		srcSession.close();
		return goods;
	}
}