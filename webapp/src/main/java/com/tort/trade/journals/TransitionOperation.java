package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;

public class TransitionOperation {
    public static final String OUTCOME = "-";
    public static final String INCOME = "+";
    private final Session _session;
    private final Sales _me;
    private final Matcher _matcher;

    public TransitionOperation(Session session, Sales me, Matcher matcher) {

        _session = session;
        _me = me;
        _matcher = matcher;
    }

    private Good loadGood(Long goodId) {
        return (Good) _session.load(Good.class, goodId);
	}

    private Sales loadSales(String dest, String sign) {
		if("$".equals(dest)){
			dest = sign + dest;
		}

        return ((SalesAlias) _session.load(SalesAlias.class, dest)).getSales();
	}

    public Transition createTransition(TransitionTO transitionTO) {
        String sign = _matcher.group(1);
        long number;
        try{
            number = Long.parseLong(_matcher.group(2));
        } catch (NumberFormatException e) {
            number = 0L;
        }
        String alias = _matcher.group(3);
        BigDecimal price;
        try{
            price = new BigDecimal(_matcher.group(4));
        } catch (NullPointerException e) {
            price = BigDecimal.ZERO;
        }

        Transition transition = new Transition();
        transition.setDate(new Date());
        transition.setMe(_me);
        transition.setGood(loadGood(transitionTO.getGoodId()));
        transition.setSellPrice(price);
        transition.setQuant(number);
        if(INCOME.equals(sign)){
            transition.setFrom(loadSales(alias, sign));
            transition.setTo(_me);
        }
        if(OUTCOME.equals(sign)){
            transition.setFrom(_me);
            transition.setTo(loadSales(alias, sign));
        }
        return transition;
    }
}
