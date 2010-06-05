package com.tort.trade.journals;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test
public class RemoveSaleActionTest {
    public void act() throws Exception {
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("id", new String[]{"1"});

        final Action action = new RemoveSaleAction(params);

        final View view = action.act();

        assertNotNull(view);
        assertTrue(view instanceof JsonView);
    }

    public void newActionNullParams(){
        try {
            new RemoveSaleAction(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void actNullIdParam(){
        Map<String, String[]> params = new HashMap<String, String[]>();

        final RemoveSaleAction action = new RemoveSaleAction(params);
        final View view = action.act();

        assertNotNull(view);
        assertTrue(view instanceof ErrorView);
    }

    public void newActionMalformedIdParam(){
        Map<String, String[]> params = new HashMap();
        params.put(RemoveSaleAction.ID_PARAM, new String[]{"malformed id"});

        final RemoveSaleAction action = new RemoveSaleAction(params);
        final View view = action.act();

        assertNotNull(view);
        assertTrue(view instanceof ErrorView);
    }
}
