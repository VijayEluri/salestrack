package com.tort.trade.journals;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

@Test
public abstract class OperationFactoryTest {
    public void testIncomeOperation() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        Operation operation = operationFactory.createOperation("240/350*10");

        assertNotNull(operation);
    }

    public void testSellOperation() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        Operation operation = operationFactory.createOperation("250*2");

        assertNotNull(operation);
    }

    public void testTransitionOperation() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        Operation operation = operationFactory.createOperation("+3В");

        assertNotNull(operation);
    }

    public void testSellOperationShort() throws ConvertTransitionException {
        final TransitionOperationFactory factory = positiveSetUp();
        final Operation operation = factory.createOperation("350");

        assertNotNull(operation);
    }

    public void testBadOperation() {
        TransitionOperationFactory operationFactory = positiveSetUp();
        try {
            operationFactory.createOperation("blabla");
            fail();
        } catch (ConvertTransitionException e) {

        }
    }

    public void testEmptyOperationString() {
        TransitionOperationFactory operationFactory = positiveSetUp();
        try {
            operationFactory.createOperation("");
            fail();
        } catch (ConvertTransitionException e) {

        }
    }

    public void testNullOperationString() throws ConvertTransitionException {
        TransitionOperationFactory operationFactory = positiveSetUp();
        try {
            operationFactory.createOperation(null);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    protected abstract TransitionOperationFactory positiveSetUp();
}
