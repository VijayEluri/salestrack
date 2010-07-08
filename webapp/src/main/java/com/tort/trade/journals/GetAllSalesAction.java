package com.tort.trade.journals;

import org.hibernate.Query;
import org.hibernate.Session;

public class GetAllSalesAction implements Action {
    private Session _session;

    public GetAllSalesAction(Session session) {
        if(session == null)
            throw new IllegalArgumentException("session is null");
        
        _session = session;
    }

    @Override
    public View act() {
        Query query = _session.createQuery("from Sales sales where sales.id > 2");

        return new JsonView(query.list());
    }
}
