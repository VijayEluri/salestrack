package com.tort.trade.journals;

import com.tort.trade.journals.editsales.SaleTO;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import org.hibernate.Session;

public class UpdateSaleAction implements Action {
    private Session _session;
    private Long _id;
    private String _newName;
    private String _newAlias;

    public UpdateSaleAction(final Session session, final Long id, final String newName, final String newAlias) {
        _newAlias = newAlias;
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

        _session.delete(sales.getAlias());

        final SalesAlias salesAlias = new SalesAlias();
        salesAlias.setId(_newAlias);
        salesAlias.setSales(sales);

        sales.setAlias(salesAlias);
        
        _session.persist(salesAlias);

        _session.flush();

        return new JsonView<SaleTO>(new SaleTO(sales.getId(), sales.getName(), sales.getAlias().getId()));
    }
}
