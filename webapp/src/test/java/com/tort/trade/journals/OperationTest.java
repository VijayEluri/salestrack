package com.tort.trade.journals;

import com.tort.trade.model.Transition;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@Test
public abstract class OperationTest {
    public void createOperation(){
        Operation operation = positiveSetUp();
        Transition transition = operation.createTransition(new TransitionTO());

        assertNotNull(transition);
    }

    protected abstract Operation positiveSetUp();
}
