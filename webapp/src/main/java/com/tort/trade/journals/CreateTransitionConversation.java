package com.tort.trade.journals;

import org.hibernate.Session;

public class CreateTransitionConversation {
	private Session _hibernateSession;

	public Session getHibernateSession() {
		return _hibernateSession;
	}

	public void setHibernateSession(Session hibernateSession) {
		_hibernateSession = hibernateSession;
	}
}
