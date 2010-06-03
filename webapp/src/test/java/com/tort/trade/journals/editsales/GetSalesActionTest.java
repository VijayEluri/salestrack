package com.tort.trade.journals.editsales;

import com.tort.trade.journals.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test
public class GetSalesActionTest {

    public void act(){
        JournalQueryFactory queryFactory = createMock(JournalQueryFactory.class);
        expect(queryFactory.getSales()).andReturn("query");
        replay(queryFactory);

        Query query = createMock(Query.class);
        expect(query.list()).andReturn(new ArrayList());
        replay(query);

        Session session = createMock(Session.class);
        expect(session.createQuery(isA(String.class))).andReturn(query);
        replay(session);

        Action action = new GetSalesAction(queryFactory, session);

        final View view = action.act();

        assertNotNull(view);
        assertTrue(view instanceof JsonView);
        verify(query, queryFactory, session);
    }

    public void newGetSalesActionNullQueryFactory(){
        try {
            new GetSalesAction(null, createMock(Session.class));
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void newGetSalesActionNullSession(){
        try {
            new GetSalesAction(createMock(JournalQueryFactory.class), null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
