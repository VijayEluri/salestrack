package com.tort.trade.journals;

import org.hibernate.Query;
import org.testng.annotations.Test;

import com.tort.trade.model.Sales;

import java.util.Date;

import static org.testng.AssertJUnit.*;

@Test(groups = {"integration"})
public class JournalQueryFactoryTest extends QueryTest{
	private JournalQueryFactory _queryFactory = new JournalQueryFactoryImpl();
	
	public void balanceQuery(){
		Query query = _session.createQuery(_queryFactory.getBalanceQuery());
		query.setParameter("me", new Sales(1L, "test"));
        query.setParameter("today", new Date());
		
		assertNotNull(query.list());
	}


    public void consistencyQuery(){
        final Query query = _session.createQuery(_queryFactory.getConsistencyQuery());
        query.setParameter("me", new Sales(2L, "test"));

        assertNotNull(query.list());
    }
}
