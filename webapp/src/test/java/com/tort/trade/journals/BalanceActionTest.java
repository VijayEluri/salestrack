package com.tort.trade.journals;

import static org.easymock.EasyMock.*;
import static org.testng.AssertJUnit.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.testng.annotations.Test;

import com.tort.trade.model.Sales;

@Test
public class BalanceActionTest extends ActionTest {

	@Override
	protected Action positiveSetUp() {
		return new BalanceAction(createSession(), createQueryFactory(), createParams());
	}

	private Session createSession() {
		Query query = createMock(Query.class);
		expect(query.setParameter(eq("me"), isA(Sales.class))).andReturn(query);
        expect(query.setParameter(eq("today"), isA(Date.class))).andReturn(query);
		expect(query.list()).andReturn(new ArrayList<GoodBalance>());
		replay(query);
		
		Session session = createMock(Session.class);
		expect(session.load(eq(Sales.class), isA(Long.class))).andReturn(new Sales(1L, "test"));
		expect(session.createQuery(isA(String.class))).andReturn(query);
		replay(session);
		return session;
	}

	private Map createParams() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("me", new String[]{"1"});
        params.put("today", new String[]{"01/02/03"});
		
		return params;
	}

	private JournalQueryFactory createQueryFactory() {
		JournalQueryFactory queryFactory = createMock(JournalQueryFactory.class);
		expect(queryFactory.getBalanceQuery()).andReturn("query");
		replay(queryFactory);
		
		return queryFactory;
	}
	
	public void newBalanceActionNullSession(){
		try{
			new BalanceAction(null, createQueryFactory(), createParams());
			fail();
		} catch (IllegalArgumentException e){
			
		}
	}
	
	public void newBalanceActionNullQueryFactory(){
		try{
			new BalanceAction(createSession(), null, createParams());
			fail();
		} catch (IllegalArgumentException e){
			
		}
	}
	
	public void newBalanceActionNullParams(){
		try {
			new BalanceAction(createSession(), createQueryFactory(), null);
			fail();
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void newBalanceActionEmptyParams(){
		try {
			Map params = new HashMap();
			new BalanceAction(createSession(), createQueryFactory(), params);
			fail();
		} catch(IllegalArgumentException e){
			
		}
	}
	
	public void newBalanceActionNullMeParam(){
		try{
			Map<String, String[]> params = new HashMap<String, String[]>();
			params.put("unknown", new String[]{"1"});
			
			new BalanceAction(createSession(), createQueryFactory(), params);
			fail();
		} catch (IllegalArgumentException e) {
			
		}
	}

    public void actNullTodayParam() {
        Map<String, String[]> params = new HashMap();
        params.put("me", new String[]{"1"});

        try {
            new BalanceAction(createSession(), createQueryFactory(), params);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
	
	public void newBalanceActionUnExistentMe(){
		try{
			Session session = createMock(Session.class);
			expect(session.load(eq(Sales.class), isA(Long.class))).andThrow(new HibernateException("no sales with such id"));
			replay(session);
			
			new BalanceAction(session, createQueryFactory(), createParams());
			fail();
		} catch (HibernateException e) {
			
		}
	}
}
