package com.tort.trade.journals;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class RemoveSaleActionTest {
    @Test
    public void act() throws Exception {
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("id", new String[]{"1"});

        final Action action = new RemoveSaleAction(params);

        final View view = action.act();

        assertNotNull(view);
        assertTrue(view instanceof JsonView);
    }

    @Test
    public void newActionNullParams(){
        try {
            new RemoveSaleAction(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void newActionNullIdParam(){
        Map<String, String[]> params = new HashMap<String, String[]>();

        try {
            new RemoveSaleAction(params);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void newActionMalformedIdParam(){
        Map<String, String[]> params = new HashMap();

        try {
            new RemoveSaleAction(params);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
