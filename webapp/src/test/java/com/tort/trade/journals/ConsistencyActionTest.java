package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import org.hibernate.Query;
import org.hibernate.Session;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertNotNull;

@Test
public class ConsistencyActionTest {
    public void actPositive() {
        Query query = createMock(Query.class);
        expect(query.setParameter(isA(String.class), isA(Sales.class))).andReturn(createMock(Query.class));
        expect(query.setParameter(isA(String.class), isA(Date.class))).andStubReturn(null);
        expect(query.list()).andReturn(new ArrayList());
        replay(query);

        Session session = createMock(Session.class);
        expect(session.load(eq(Sales.class), isA(Long.class))).andReturn(new Sales("me"));
        expect(session.createQuery(isA(String.class))).andReturn(query);
        replay(session);

        JournalQueryFactory queryFactory = createMock(JournalQueryFactory.class);
        expect(queryFactory.getConsistencyQuery()).andReturn("query");
        replay(queryFactory);

        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("me", new String[]{"1"});
        params.put(ConsistencyAction.START_DATE_PARAM, new String[]{"111"});
        params.put(ConsistencyAction.END_DATE_PARAM, new String[]{"222"});

        final ConsistencyAction action = new ConsistencyAction(session, queryFactory, params);
        final View view = action.act();
        
        assertNotNull(view);
    }
}
