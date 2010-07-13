package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import org.hibernate.Criteria;
import org.hibernate.Session;

import java.util.List;

public class TransitionOperationFactoryImpl implements TransitionOperationFactory {
    private final Session _session;
    private final Sales _me;

    public TransitionOperationFactoryImpl(Session session, Sales me) {
        _session = session;
        _me = me;
    }

    private List<SalesAlias> getPatternAliases() {
        Criteria criteria = _session.createCriteria(SalesAlias.class);

        return criteria.list();
    }

    @Override
    public Operation createOperation(String transitionString) throws ConvertTransitionException {
        if(transitionString == null)
            throw new IllegalArgumentException("transition string is null");

        java.util.regex.Matcher matcher;

        TransitionOperation.Matcher transitionMatcher = new TransitionOperation.Matcher(getPatternAliases(), transitionString);
        if (transitionMatcher.matches())
            return new TransitionOperation(_session, _me, transitionMatcher);

        matcher = SellOperation.FullMatcher.PATTERN.matcher(transitionString);
        if(matcher.matches())
            return new SellOperation(_session, _me, new SellOperation.FullMatcher(matcher));

        matcher = SellOperation.ShortMatcher.PATTERN.matcher(transitionString);
        if(matcher.matches())
        return new SellOperation(_session, _me, new SellOperation.ShortMatcher(matcher));

        matcher = IncomeOperation.Matcher.PATTERN.matcher(transitionString);
        if(matcher.matches())
            return new IncomeOperation(_session, _me, new IncomeOperation.Matcher(matcher));

        throw new ConvertTransitionException("Передача " + transitionString + " неверна");
    }
}
