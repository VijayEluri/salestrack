package com.tort.trade.journals;

import java.util.ArrayList;
import java.util.Map;

public class RemoveSaleAction implements Action{
    public static final String ID_PARAM = "id";

    private final Map<String, String[]> _params;

    public RemoveSaleAction(Map<String, String[]> params) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        _params = params;
    }

    @Override
    public View act() {
        String[] idParam = _params.get(ID_PARAM);
        if(idParam == null)
            return new ErrorView(ID_PARAM + " param is null");

        try {
            Long id = new Long(idParam[0]);
        } catch (NumberFormatException e) {
            return new ErrorView("Bad " + ID_PARAM + " param:" + idParam[0]);
        }

        return new JsonView(new ErrorTO("sale is unremovable"));
    }
}
