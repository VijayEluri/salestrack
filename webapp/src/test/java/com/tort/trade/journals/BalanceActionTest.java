package com.tort.trade.journals;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.testng.annotations.Test;

@Test
public class BalanceActionTest extends ActionTest {

	@Override
	protected Action positiveSetUp() {
		Session session = createMock(Session.class);
		Query query = createMock(Query.class);
		JournalQueryFactory queryFactory = createMock(JournalQueryFactory.class);
		
		expect(query.list()).andReturn(new ArrayList<GoodBalance>());
		expect(session.createQuery(isA(String.class))).andReturn(query);
		expect(queryFactory.getBalanceQuery()).andReturn("query");
		
		replay(session, query, queryFactory);
		
		return new BalanceAction(session, queryFactory);
	}
}
