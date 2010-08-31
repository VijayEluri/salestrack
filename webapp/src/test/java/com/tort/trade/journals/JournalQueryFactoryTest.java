package com.tort.trade.journals;

import org.hibernate.Query;
import org.testng.annotations.Test;

import com.tort.trade.model.Sales;

import java.util.Date;

import static org.testng.AssertJUnit.*;

@Test(groups = {"integration"})
public class JournalQueryFactoryTest extends QueryTest {
    private JournalQueryFactory _queryFactory = new JournalQueryFactoryImpl();

    public void balanceQuery() {
        Query query = _session.createQuery(_queryFactory.getBalanceQuery());
        Sales me = (Sales) _session.load(Sales.class, 1L);
        query.setParameter("me", me);
        query.setParameter("today", new Date());

        assertNotNull(query.list());
    }


    public void consistencyQuery() {
        Sales me = (Sales) _session.load(Sales.class, 7L);
        final Query query = _session.createQuery(_queryFactory.getConsistencyQuery());
        query.setParameter("me", me);
        query.setParameter("startDate", new Date());
        query.setParameter("endDate", new Date());

        assertNotNull(query.list());
    }

    public void getSales() {
        final Query query = _session.createQuery(_queryFactory.getSales());

        assertNotNull(query.list());
    }
}
