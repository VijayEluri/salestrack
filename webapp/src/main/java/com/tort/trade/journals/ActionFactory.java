package com.tort.trade.journals;

import com.tort.trade.journals.editsales.GetSalesAction;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.Map;

public class ActionFactory {
    private final Map<String, String[]> _params;
    public static final String COMMAND_PARAM = "command";
    public static final String GET_COMMAND = "get";
    private Session _session;
    public static final String REMOVE_COMMAND = "remove";
    private static final String UPDATE_COMMAND = "update";

    public ActionFactory(Map<String, String[]> params, Session session) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        _params = params;
        _session = session;
    }

    public Action createAction() {
        final String[] commandParam = _params.get(COMMAND_PARAM);
        if(commandParam == null)
            return new ErrorAction(COMMAND_PARAM + " absent");

        final String command = ((String[]) commandParam)[0];

        if(REMOVE_COMMAND.equals(command))
            return new RemoveSaleAction(_params);

        if(GET_COMMAND.equals(command))
            return new GetSalesAction(new JournalQueryFactoryImpl(), _session);

        if(UPDATE_COMMAND.equals(command)){
            return new UpdateSaleAction();
        }

        return new ErrorAction("unknown command");
    }
}
