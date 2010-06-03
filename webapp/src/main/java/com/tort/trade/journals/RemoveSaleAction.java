package com.tort.trade.journals;

import java.util.ArrayList;
import java.util.Map;

public class RemoveSaleAction implements Action{
    private final Map<String, String[]> _params;

    public RemoveSaleAction(Map<String, String[]> params) {
        _params = params;
    }

    @Override
    public View act() {
        return new JsonView(new ErrorTO("sale is unremovable"));
    }
}
