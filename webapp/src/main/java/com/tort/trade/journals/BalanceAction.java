package com.tort.trade.journals;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

public class BalanceAction implements Action<List<GoodBalance>> {

	private final Session _session;
	private final JournalQueryFactory _queryFactory;
	private final Map _params;

	public BalanceAction(Session session, JournalQueryFactory queryFactory, Map params) {
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
		
		_session = session;
		_queryFactory = queryFactory;
		_params = params;
	}

	@Override
	public List<GoodBalance> act() {
		Query query = _session.createQuery(_queryFactory.getBalanceQuery());
		
		return query.list();
	}

}
