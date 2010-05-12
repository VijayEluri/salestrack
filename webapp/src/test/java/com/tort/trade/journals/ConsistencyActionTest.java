package com.tort.trade.journals;

import org.hibernate.Query;
import org.hibernate.Session;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertNotNull;

@Test
public class ConsistencyActionTest {
    public void actPositive() {
        Query query = createMock(Query.class);
        expect(query.list()).andReturn(new ArrayList());
        replay(query);

        Session session = createMock(Session.class);
        expect(session.createQuery(isA(String.class))).andReturn(query);
        replay(session);

        JournalQueryFactory queryFactory = createMock(JournalQueryFactory.class);
        expect(queryFactory.getConsistencyQuery()).andReturn("query");
        replay(queryFactory);

        final ConsistencyAction action = new ConsistencyAction(session, queryFactory);
        final JsonView<List<DiffTO>> view = action.act();
        
        assertNotNull(view);
    }
}
