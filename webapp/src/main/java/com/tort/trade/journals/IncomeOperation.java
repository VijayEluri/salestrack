package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;
import org.hibernate.Session;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

public class IncomeOperation extends BaseOperation {
    private final Matcher _matcher;
    private static final Long SUPPLIER_ID = 1L;

    public IncomeOperation(Session session, Sales me, Matcher matcher) {
        super(session, me);

        if (matcher == null)
            throw new IllegalArgumentException("matcher is null");

        _matcher = matcher;
    }

    @Override
    public Transition createTransition(TransitionTO transitionTO) {
        final Transition transition = new Transition();
        transition.setDate(new Date());
        transition.setBuyPrice(_matcher.getBuyPrice());
        transition.setSellPrice(_matcher.getSellPrice());
        transition.setQuant(_matcher.getQuant());
        transition.setGood(loadGood(transitionTO.getGoodId()));
        transition.setMe(_me);
        transition.setTo(_me);
        transition.setFrom((Sales) _session.load(Sales.class, SUPPLIER_ID));

        return transition;
    }

    public static class Matcher {
        public static final Pattern PATTERN = Pattern.compile("^(\\d+)/(\\d+)\\*(\\d+)$");
        private final java.util.regex.Matcher _matcher;

        public Matcher(java.util.regex.Matcher matcher) {
            _matcher = matcher;
        }

        public BigDecimal getBuyPrice() {
            return new BigDecimal(_matcher.group(1));
        }

        public BigDecimal getSellPrice() {
            return new BigDecimal(_matcher.group(2));
        }

        public Long getQuant() {
            return new Long(_matcher.group(3));
        }
    }
}
