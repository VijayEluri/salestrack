package com.tort.trade.journals;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class ConsistencyAction implements Action {
    private final JournalQueryFactory _queryFactory;
    private final Session _session;

    public ConsistencyAction(Session session, JournalQueryFactory queryFactory) {
        _session = session;
        _queryFactory = queryFactory;
    }

    public JsonView<List<DiffTO>> act() {
        Query query = _session.createQuery(_queryFactory.getConsistencyQuery());
        List<DiffTO> model = query.list();

        return new JsonView<List<DiffTO>>(model);
    }
}