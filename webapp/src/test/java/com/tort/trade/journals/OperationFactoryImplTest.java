package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertTrue;

@Test
public class OperationFactoryImplTest extends OperationFactoryTest {

    @Override
    protected TransitionOperationFactory positiveSetUp() {
        ArrayList<SalesAlias> aliases = new ArrayList();

        SalesAlias alias = new SalesAlias();
        alias.setId("В");
        aliases.add(alias);

        alias = new SalesAlias();
        alias.setId("С");
        aliases.add(alias);

        Criteria criteria = createMock(Criteria.class);
        expect(criteria.list()).andReturn(aliases);
        replay(criteria);

        Session session = createMock(Session.class);
        expect(session.createCriteria(eq(SalesAlias.class))).andReturn(criteria);
        replay(session);

        Sales me = new Sales("test");

        return new TransitionOperationFactoryImpl(session, me);
    }

    public void testTransitionOperationImpl() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        Operation operation = operationFactory.createOperation("-3В");

        assertTrue(operation instanceof TransitionOperation);
    }

    public void testSellOperationImpl() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        Operation operation = operationFactory.createOperation("200*10");

        assertTrue(operation instanceof SellOperation);
    }

    public void testIncomeOperationImpl() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        Operation operation = operationFactory.createOperation("300/400*10");

        assertTrue(operation instanceof IncomeOperation);
    }
}
