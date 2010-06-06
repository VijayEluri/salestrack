package com.tort.trade.journals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.tort.trade.model.Sales;

public class BalanceAction implements Action {

	private final Session _session;
	private final JournalQueryFactory _queryFactory;
    private final Sales _me;
    private Date _today;

    public BalanceAction(Session session, JournalQueryFactory queryFactory, Map<String, String[]> params) {
		if(session == null)
			throw new IllegalArgumentException("session is null");
		
		if(queryFactory == null)
			throw new IllegalArgumentException("query factory is null");
		
		if(params == null)
			throw new IllegalArgumentException("params is null");
		
		if(params.size() == 0)
			throw new IllegalArgumentException("params is empty");
		
		if(params.get("me") == null)
			throw new IllegalArgumentException("me is null");

        if(params.get("today") == null)
            throw new IllegalArgumentException("today is null");
		
		Long meId = new Long(params.get("me")[0]);
		_me = (Sales) session.load(Sales.class, meId);

        Long today = new Long(params.get("today")[0]);
        _today = new Date(today);

        _session = session;
		_queryFactory = queryFactory;
	}

	@Override
	public View act() {
		Query query = _session.createQuery(_queryFactory.getBalanceQuery());
		query.setParameter("me", _me);
        query.setParameter("today", _today);
		
		return new JsonView<List>(query.list());
	}

}
