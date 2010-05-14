package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

public class ConsistencyAction implements Action {
    private final JournalQueryFactory _queryFactory;
    private final Session _session;
    private final Long _meId;

    public ConsistencyAction(Session session, JournalQueryFactory queryFactory, Map params) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(params.get("me") == null)
            throw new IllegalArgumentException("me param absent");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        if(queryFactory == null)
            throw new IllegalArgumentException("queryFactory is null");

        _session = session;
        _queryFactory = queryFactory;
        _meId = new Long(((String[])params.get("me"))[0]);
    }

    public JsonView<List<DiffTO>> act() {
        Sales _me = (Sales) _session.load(Sales.class, _meId);
        Query query = _session.createQuery(_queryFactory.getConsistencyQuery());
        query.setParameter("me", _me);
        List<DiffTO> model = query.list();

        return new JsonView<List<DiffTO>>(model);
    }
}