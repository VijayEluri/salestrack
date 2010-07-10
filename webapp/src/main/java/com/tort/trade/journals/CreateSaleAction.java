package com.tort.trade.journals;

import com.tort.trade.journals.editsales.SaleTO;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import org.hibernate.Session;

import java.io.Serializable;

public class CreateSaleAction implements Action {
    private Session _session;
    private String _newName;
    private String _newAlias;

    public CreateSaleAction(final Session session, final String newName, final String newAlias) {
        if(session == null)
            throw new IllegalArgumentException("session id null");

        if(newName == null)
            throw new IllegalArgumentException("newName is null");

        if(newAlias == null)
            throw new IllegalArgumentException("newAlias is null");
        
        _session = session;
        _newName = newName;
        _newAlias = newAlias;
    }

    @Override
    public View act() {
        final Sales sales = new Sales();
        sales.setName(_newName);
        _session.persist(sales);

        SalesAlias alias = new SalesAlias();
        alias.setId(_newAlias);
        alias.setSales(sales);
        sales.setAlias(alias);
        _session.persist(alias);

        _session.flush();

        return new JsonView<SaleTO>(new SaleTO(sales.getId(), sales.getName(), alias.getId()));
    }
}
