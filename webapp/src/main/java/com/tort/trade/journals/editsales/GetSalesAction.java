package com.tort.trade.journals.editsales;

import com.tort.trade.journals.Action;
import com.tort.trade.journals.JournalQueryFactory;
import com.tort.trade.journals.JsonView;
import com.tort.trade.journals.View;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetSalesAction implements Action {
    private final JournalQueryFactory _queryFactory;
    private final Session _session;

    public GetSalesAction(JournalQueryFactory queryFactory, Session session) {
        if(queryFactory == null)
            throw new IllegalArgumentException("queryFactory is null");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        _queryFactory = queryFactory;
        _session = session;
    }

    @Override
    public View act() {
        String queryString = _queryFactory.getSales();
        final Query query = _session.createQuery(queryString);
        final List<SaleTO> sales = query.list();

        return new JsonView<List<SaleTO>>(sales);
    }
}
