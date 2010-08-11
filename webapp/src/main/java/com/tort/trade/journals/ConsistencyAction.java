package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public class ConsistencyAction implements Action {
    private final JournalQueryFactory _queryFactory;
    private final String[] _meParam;
    private TransitionConversation _conversation;

    public ConsistencyAction(TransitionConversation conversation, JournalQueryFactory queryFactory, Map params) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(params.get("me") == null)
            throw new IllegalArgumentException("me param absent");

        if(conversation == null)
            throw new IllegalArgumentException("conversation is null");

        if(queryFactory == null)
            throw new IllegalArgumentException("queryFactory is null");

        _queryFactory = queryFactory;
        _meParam = (String[]) params.get("me");
        _conversation = conversation;
    }

    public View act() {
        Long meId;
        Sales me;
        try {
            meId = new Long(_meParam[0]);
        } catch (NumberFormatException e) {
            return new ErrorView("meId is non-numeric");
        }

        try {
            me = (Sales) _conversation.getHibernateSession().load(Sales.class, meId);
        } catch (HibernateException e) {
            return new ErrorView("unknown sales");
        }

        if (_conversation.getInconsistent() == null) {
            Query query = _conversation.getHibernateSession().createQuery(_queryFactory.getConsistencyQuery());
            query.setParameter("me", me);
            _conversation.setInconsistent((List<Transition>) query.list());
        }

        Map<Date, List<Transition>> sortedTransitions = groupByDate(_conversation.getInconsistent());
        sortedTransitions = new DaySumsChecker().invoke(sortedTransitions);
        Map<Date, List<DiffTO>> model = toModel(sortedTransitions);

        return new JsonView<Map<Date, List<DiffTO>>>(model);
    }

    private Map<Date, List<DiffTO>> toModel(Map<Date, List<Transition>> sortedTransitions) {
        Map<Date, List<DiffTO>> model = new TreeMap<Date, List<DiffTO>>();
        for (Date date : sortedTransitions.keySet()) {
            List<DiffTO> diffs = model.get(date);
            if(diffs == null){
                diffs = new ArrayList();
                model.put(date, diffs);
            }
            final List<Transition> transitions = sortedTransitions.get(date);
            for (Transition transition : transitions) {
                diffs.add(new DiffTO(transition.getMe().getId(), transition.getGood().getName(), String.valueOf(transition.getQuant())));
            }
        }
        return model;
    }

    private Map<Date, List<Transition>> groupByDate(List<Transition> transitions) {
        TreeMap<Date, List<Transition>> sortedTransitions = new TreeMap<Date, List<Transition>>();
        for (Transition transition : transitions) {
            List<Transition> value = sortedTransitions.get(transition.getDate());
            if(value == null){
                value = new ArrayList();
                sortedTransitions.put(transition.getDate(), value);
            }
            value.add(transition);
        }
        return sortedTransitions;
    }

}
