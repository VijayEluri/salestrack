package com.tort.trade.journals;

import com.tort.trade.model.Transition;
import org.hibernate.Session;

import java.util.List;

public class TransitionConversation {
    private Session _hibernateSession;
    private List<Transition> inconsistent;

    public Session getHibernateSession() {
        return _hibernateSession;
    }

    public List<Transition> getInconsistent() {
        return inconsistent;
    }

    public void addTransition(Transition transition){
        inconsistent.add(transition);
    }

    public void setInconsistent(List<Transition> inconsistent) {
        this.inconsistent = inconsistent;
    }

    public void setHibernateSession(Session hibernateSession) {
        _hibernateSession = hibernateSession;
    }
}
