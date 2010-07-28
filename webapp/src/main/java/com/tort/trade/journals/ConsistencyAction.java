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

    public ConsistencyAction(Session session, JournalQueryFactory queryFactory, Map params) {
        if(params == null)
            throw new IllegalArgumentException("params is null");

        if(params.get("me") == null)
            throw new IllegalArgumentException("me param absent");

        if(session == null)
            throw new IllegalArgumentException("session is null");

        if(queryFactory == null)
            throw new IllegalArgumentException("queryFactory is null");

        _session = session;
        _queryFactory = queryFactory;
        _meParam = (String[]) params.get("me");
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

        Query query = _session.createQuery(_queryFactory.getConsistencyQuery());
        query.setParameter("me", me);
        List<Transition> transitions = query.list();

        Map<Date, List<DiffTO>> model = new TreeMap<Date, List<DiffTO>>();
        for (Transition transition : transitions) {
            List<DiffTO> diffs = model.get(transition.getDate());
            if(diffs == null){
                diffs = new ArrayList();
                model.put(transition.getDate(), diffs);
            }
            diffs.add(new DiffTO(transition.getMe().getId(), transition.getGood().getName(), String.valueOf(transition.getQuant())));
        }

        return new JsonView<Map<Date, List<DiffTO>>>(model);
    }
}
