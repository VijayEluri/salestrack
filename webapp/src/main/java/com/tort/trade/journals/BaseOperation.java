package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import org.hibernate.Session;

public abstract class BaseOperation implements Operation {
    protected final Session _session;
    protected final Sales _me;

    public BaseOperation(Session session, Sales me) {
        if(session == null) {
            throw new IllegalArgumentException("session is null");
        }

        if(me == null) {
            throw new IllegalArgumentException("me is null");
        }

        _me = me;
        _session = session;
    }

    protected Good loadGood(Long goodId) {
        return (Good) _session.load(Good.class, goodId);
	}

    protected Sales loadSales(String dest, String sign) {
		if("$".equals(dest)){
			dest = sign + dest;
		}

        return ((SalesAlias) _session.load(SalesAlias.class, dest)).getSales();
	}
}
