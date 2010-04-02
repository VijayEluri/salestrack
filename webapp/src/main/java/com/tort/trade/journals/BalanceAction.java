package com.tort.trade.journals;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class BalanceAction implements Action<List<GoodBalance>> {

	private final Session _session;
	private final JournalQueryFactory _queryFactory;

	public BalanceAction(Session session, JournalQueryFactory queryFactory) {
		_session = session;
		_queryFactory = queryFactory;
	}

	@Override
	public List<GoodBalance> act() {
		Query query = _session.createQuery(_queryFactory.getBalanceQuery());
		
		return query.list();
	}

}
