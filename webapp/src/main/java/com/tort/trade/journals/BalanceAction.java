package com.tort.trade.journals;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.tort.trade.model.Sales;

public class BalanceAction implements Action<List<GoodBalance>> {

	private final Session _session;
	private final JournalQueryFactory _queryFactory;
	private final Map _params;
	private final Sales _me;

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
		
		Long meId = new Long(params.get("me")[0]);
		_me = (Sales) session.load(Sales.class, meId);
		
		_session = session;
		_queryFactory = queryFactory;
		_params = params;
	}

	@Override
	public List<GoodBalance> act() {
		Query query = _session.createQuery(_queryFactory.getBalanceQuery());
		query.setParameter("me", _me);
		
		return query.list();
	}

}
