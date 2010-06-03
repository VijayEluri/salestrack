package com.tort.trade.journals;

import com.tort.trade.journals.editsales.GetSalesAction;
import org.hibernate.Session;

import java.util.Map;

public class ActionFactory {
    private final Map<String, String[]> _params;
    public static final String COMMAND_PARAM = "command";
    public static final String GET_COMMAND = "get";
    private Session _session;
    public static final String REMOVE_COMMAND = "remove";

    public ActionFactory(Map<String, String[]> params, Session session) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        _params = params;
        _session = session;
    }

    public Action createAction() {
        final String command = ((String[])_params.get(COMMAND_PARAM))[0];

        if(command == null)
            return new ErrorAction(COMMAND_PARAM + " absent");

        if(REMOVE_COMMAND.equals(command))
            return new RemoveSaleAction(_params);

        if(GET_COMMAND.equals(command))
            return new GetSalesAction(new JournalQueryFactoryImpl(), _session);

        return new ErrorAction("unknown command");
    }
}
