package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

public class TransitionOperation extends BaseOperation {
    public static final String OUTCOME = "-";
    public static final String INCOME = "+";
    private final Matcher _matcher;

    public TransitionOperation(Session session, Sales me, Matcher matcher) {
        super(session, me);
        _matcher = matcher;
    }

    @Override
    public Transition createTransition(TransitionTO transitionTO) {
        String sign = _matcher.getSign();
        long number;
        try{
            number = Long.parseLong(_matcher.getNumber());
        } catch (NumberFormatException e) {
            number = 0L;
        }
        String alias = _matcher.getAlias();

        Transition transition = new Transition();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        final Date today = calendar.getTime();
        transition.setDate(today);
        transition.setMe(_me);
        transition.setGood(loadGood(transitionTO.getGoodId()));
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

    public static class Matcher {
        private final java.util.regex.Matcher _matcher;

        public Matcher(List<SalesAlias> aliases, String transitionString) {
            if(aliases == null)
                throw new IllegalArgumentException("aliases is null");

            if(aliases.size() == 0)
                throw new IllegalArgumentException("aliases size is 0");

            StringBuilder aliasesBuilder = new StringBuilder();
            for (SalesAlias alias : aliases) {
                aliasesBuilder.append(alias.getId());
            }

            _matcher = Pattern.compile("^([+-])(\\d+)([" + aliasesBuilder.toString() + "])$").matcher(transitionString);
        }

        public String getSign() {
            return _matcher.group(1);
        }

        public String getNumber() {
            return _matcher.group(2);
        }

        public String getAlias() {
            return _matcher.group(3);
        }

        public boolean matches() {
            return _matcher.matches();
        }
    }
}
