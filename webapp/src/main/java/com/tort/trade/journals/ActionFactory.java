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
    private static final String UPDATE_COMMAND = "update";
    private static final String SALE_ID_PARAM = "saleId";
    private static final String SALE_NAME = "saleName";
    private static final String SALE_ALIAS = "saleAlias";

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

        final String command = commandParam[0];

        if(REMOVE_COMMAND.equals(command))
            return new RemoveSaleAction(_params);

        if(GET_COMMAND.equals(command))
            return new GetSalesAction(new JournalQueryFactoryImpl(), _session);

        if(UPDATE_COMMAND.equals(command)){
            final String[] saleIdParam = _params.get(SALE_ID_PARAM);
            if(saleIdParam == null)
                return new ErrorAction("saleId is null");

            final String[] newNameParam = _params.get(SALE_NAME);
            if(newNameParam == null)
                return new ErrorAction("sale name is null");

            final String[] newAliasParam = _params.get(SALE_ALIAS);
            if(newAliasParam == null)
                return new ErrorAction("sale alias is null");

            final Long saleId = new Long(saleIdParam[0]);
            final String newName = newNameParam[0];
            final String newAlias = newAliasParam[0];
            
            return new UpdateSaleAction(_session, saleId, newName, newAlias);
        }

        return new ErrorAction("unknown command");
    }
}
