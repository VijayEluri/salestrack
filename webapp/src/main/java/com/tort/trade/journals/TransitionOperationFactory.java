package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import org.hibernate.Session;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransitionOperationFactory {
    private static final Pattern _pattern = Pattern.compile("^([+-])(\\d+)?([ВС\\$])(\\d+)?$");
    private final Session _session;
    private final Sales _me;

    public TransitionOperationFactory(Session session, Sales me) {

        _session = session;
        _me = me;
    }

    public TransitionOperation createOperation(String _transitionString) throws ConvertTransitionException {
        Matcher matcher = _pattern.matcher(_transitionString);
        if(!matcher.find())
            throw new ConvertTransitionException("Передача " + _transitionString + " неверна");

        return new TransitionOperation(_session, _me, matcher);
    }
}
