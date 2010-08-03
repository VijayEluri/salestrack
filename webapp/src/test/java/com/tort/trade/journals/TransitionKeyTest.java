package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Test
public class TransitionKeyTest {
    public void testOpposite() throws Exception {
        final Good good = new Good();
        final Sales masha = new Sales();
        final Sales sasha = new Sales();

        TransitionKey transitionKey = new TransitionKey(good, masha, masha, sasha);
        TransitionKey oppositeKey = new TransitionKey(good, sasha, masha, sasha);

        assertTrue(transitionKey.opposite(oppositeKey));

        transitionKey = new TransitionKey(good, masha, sasha, masha);
        oppositeKey = new TransitionKey(good, sasha, sasha, masha);

        assertTrue(transitionKey.opposite(oppositeKey));
    }
}
