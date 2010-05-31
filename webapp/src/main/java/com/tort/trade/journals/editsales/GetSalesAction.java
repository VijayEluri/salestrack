package com.tort.trade.journals.editsales;

import com.tort.trade.journals.Action;
import com.tort.trade.journals.JsonView;
import com.tort.trade.journals.View;

import java.util.ArrayList;

public class GetSalesAction implements Action {
    @Override
    public View act() {
        return new JsonView(new ArrayList());
    }
}
