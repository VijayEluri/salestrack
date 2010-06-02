package com.tort.trade.journals.editsales;

import com.tort.trade.journals.Action;
import com.tort.trade.journals.JsonView;
import com.tort.trade.journals.View;

import java.util.ArrayList;

public class GetSalesAction implements Action {
    @Override
    public View act() {
        final ArrayList<SaleTO> sales = new ArrayList<SaleTO>();
        sales.add(new SaleTO(1L, "Миша", "М"));
        sales.add(new SaleTO(2L, "Вася", "В"));

        return new JsonView<ArrayList<SaleTO>>(sales);
    }
}
