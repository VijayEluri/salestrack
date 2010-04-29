package com.tort.trade.journals;

import static org.easymock.EasyMock.*;
import static org.testng.AssertJUnit.*;

import com.tort.trade.model.Transition;
import org.testng.annotations.Test;

@Test
public class TransitionConverterImplTest extends TransitionConverterTest {
	
	public void newConverterNullOperationFactory(){
        try {
            new TransitionConverterImpl(null);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }
	
	@Override
	protected TransitionConverter getConverter() throws ConvertTransitionException {
        Operation operation = createMock(Operation.class);
        expect(operation.createTransition(isA(TransitionTO.class))).andStubReturn(new Transition());
        TransitionOperationFactory operationFactory = createMock(TransitionOperationFactory.class);
        expect(operationFactory.createOperation(isA(String.class))).andStubReturn(operation);

        replay(operationFactory, operation);

        return new TransitionConverterImpl(operationFactory);
	}
}
