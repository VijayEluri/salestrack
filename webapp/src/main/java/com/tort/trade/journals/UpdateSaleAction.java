package com.tort.trade.journals;

import com.tort.trade.journals.editsales.SaleTO;
import com.tort.trade.model.Sales;
import org.hibernate.Session;

public class UpdateSaleAction implements Action {
    private Session _session;
    private Long _id;
    private String _newName;

    public UpdateSaleAction(final Session session, final Long id, final String newName) {
        if(session == null)
            throw new IllegalArgumentException("session is null");

        if(id == null)
            throw new IllegalArgumentException("sale id is null");

        if(newName == null)
            throw new IllegalArgumentException("name is null");

        _newName = newName;
        _session = session;
        _id = id;
    }

    @Override
    public View act() {
        final Sales sales = (Sales) _session.load(Sales.class, _id);
        sales.setName(_newName);

        _session.flush();

        return new JsonView<SaleTO>(new SaleTO(sales.getId(), sales.getName(), "ALIAS"));
    }
}
