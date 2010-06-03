package com.tort.trade.journals;

import com.tort.trade.journals.editsales.GetSalesAction;
import org.hibernate.Session;

import java.util.Map;

public class ActionFactory {
    private final Map<String, String[]> _params;
    public static final String COMMAND_PARAM = "command";
    public static final String GET_COMMAND = "get";
    private Session _session;

    public ActionFactory(Map<String, String[]> params, Session session) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        _params = params;
        _session = session;
    }

    public Action createAction() {
        if(_params.get(COMMAND_PARAM) == null)
            return new ErrorAction(COMMAND_PARAM + " absent");

        if(unknownCommand())
            return new ErrorAction("unknown command");

        return new GetSalesAction(new JournalQueryFactoryImpl(), _session);
    }

    private boolean unknownCommand() {
        final String[] command = _params.get(COMMAND_PARAM);

        return !command[0].equals(GET_COMMAND);
    }
}
