package com.tort.trade.journals;

import org.hibernate.Session;

import com.tort.trade.model.Sales;

public class TransitionConversation {
    private Session _hibernateSession;

    public Session getHibernateSession() {
        return _hibernateSession;
    }

    public void setHibernateSession(Session hibernateSession) {
        _hibernateSession = hibernateSession;
    }
}
