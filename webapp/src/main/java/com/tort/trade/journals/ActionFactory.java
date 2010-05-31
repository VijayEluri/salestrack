package com.tort.trade.journals;

import com.tort.trade.journals.editsales.GetSalesAction;

import java.util.Map;

public class ActionFactory {
    private final Map<String, String[]> _params;
    public static final String COMMAND_PARAM = "command";
    public static final String GET_COMMAND = "get";

    public ActionFactory(Map<String, String[]> params) {
        if(params == null)
            throw new IllegalArgumentException("params is null");
        
        _params = params;
    }

    public Action createAction() {
        if(_params.get(COMMAND_PARAM) == null)
            return new ErrorAction(COMMAND_PARAM + " absent");

        if(unknownCommand())
            return new ErrorAction("unknown command");

        return new GetSalesAction();
    }

    private boolean unknownCommand() {
        final String[] command = _params.get(COMMAND_PARAM);
        
        return !command[0].equals(GET_COMMAND);
    }
}
