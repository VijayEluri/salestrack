package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;
import org.hibernate.Session;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

public class SellOperation extends BaseOperation {
    private final Matcher _matcher;
    private static final Long CUSTOMER_ID = 2L;

    public SellOperation(Session session, Sales me, Matcher matcher) {
        super(session, me);

        if(matcher == null) {
            throw new IllegalArgumentException("matcher is null");
        }

        _matcher = matcher;
    }

    @Override
    public Transition createTransition(TransitionTO transitionTO) {
        Transition transition = new Transition();
        transition.setDate(new Date());
        transition.setSellPrice(_matcher.getSellPrice());
        transition.setQuant(_matcher.getQuant());
        transition.setMe(_me);
        transition.setFrom(_me);
        transition.setTo((Sales) _session.load(Sales.class, CUSTOMER_ID));
        transition.setGood(loadGood(transitionTO.getGoodId()));

        return transition;
    }

    public static class Matcher {
        public static final Pattern PATTERN = Pattern.compile("^(\\d+)\\*(\\d+)$");
        private final java.util.regex.Matcher _matcher;

        public Matcher(java.util.regex.Matcher matcher) {
            _matcher = matcher;
        }

        public BigDecimal getSellPrice() {
            return new BigDecimal(_matcher.group(1));
        }

        public long getQuant() {
            return Long.parseLong(_matcher.group(2));
        }
    }
}
