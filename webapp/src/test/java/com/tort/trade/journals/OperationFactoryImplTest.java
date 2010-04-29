package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import org.hibernate.Session;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.createMock;
import static org.testng.Assert.assertTrue;

@Test
public class OperationFactoryImplTest extends OperationFactoryTest {

    @Override
    protected TransitionOperationFactory positiveSetUp() {
        Session session = createMock(Session.class);
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
