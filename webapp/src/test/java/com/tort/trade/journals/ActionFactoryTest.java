package com.tort.trade.journals;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.tort.trade.journals.editsales.GetSalesAction;
import org.testng.annotations.Test;

@Test
public class ActionFactoryTest {
    public void newActionFactory(){
        try {
            new ActionFactory(null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void createActionNullCommandParam(){
        Map<String, String[]> params = new HashMap<String, String[]>();

        final ActionFactory actionFactory = new ActionFactory(params);

        final Action action = actionFactory.createAction();

        assertNotNull(action);
        assertTrue(action instanceof ErrorAction);
    }

    public void createActionUnknownCommandParam(){
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put(ActionFactory.COMMAND_PARAM, new String[]{"unknown"});

        final ActionFactory actionFactory = new ActionFactory(params);

        final Action action = actionFactory.createAction();

        assertNotNull(action);
        assertTrue(action instanceof ErrorAction);
    }

    public void createGetSaleAction(){
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put(ActionFactory.COMMAND_PARAM, new String[]{ActionFactory.GET_COMMAND});

        final ActionFactory actionFactory = new ActionFactory(params);

        final Action action = actionFactory.createAction();

        assertNotNull(action);
        assertTrue(action instanceof GetSalesAction);
    }
}
