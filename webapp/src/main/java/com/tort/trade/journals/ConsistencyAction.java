package com.tort.trade.journals;

import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public class ConsistencyAction implements Action {
    private final JournalQueryFactory _queryFactory;
    private final Session _session;
    private final String[] _meParam;
    private final String[] _startDateParam;
    private final String[] _endDateParam;
    public static final String START_DATE_PARAM = "startDate";
    public static final String END_DATE_PARAM = "endDate";

    public ConsistencyAction(Session session, JournalQueryFactory queryFactory, Map params) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(params.get("me") == null)
            throw new IllegalArgumentException("me param absent");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        if(queryFactory == null)
            throw new IllegalArgumentException("queryFactory is null");

        if(params.get(START_DATE_PARAM) == null)
            throw new IllegalArgumentException("startDate is null");

        if(params.get(END_DATE_PARAM) == null)
            throw new IllegalArgumentException("endDate is null");

        _session = session;
        _queryFactory = queryFactory;
        _meParam = (String[]) params.get("me");
        _startDateParam = (String[]) params.get(START_DATE_PARAM);
        _endDateParam = (String[]) params.get(END_DATE_PARAM);
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
            me = (Sales) _session.load(Sales.class, meId);
        } catch (HibernateException e) {
            return new ErrorView("unknown sales");
        }

        Date startDate = new Date(Long.valueOf(_startDateParam[0]));
        Date endDate = new Date(Long.valueOf(_endDateParam[0]));

        Query query = _session.createQuery(_queryFactory.getConsistencyQuery());
        query.setParameter("me", me);
        query.setParameter(START_DATE_PARAM, startDate);
        query.setParameter(END_DATE_PARAM, endDate);
        List<Transition> transitions = query.list();

        Map<Date, List<Transition>> sortedTransitions = groupByDate(transitions);
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
