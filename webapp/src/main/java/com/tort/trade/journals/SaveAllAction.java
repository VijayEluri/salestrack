package com.tort.trade.journals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;

public class SaveAllAction implements Action {

    private final List<TransitionTO> _transitions;
    private final Session _session;
    private final Long _meId;
    private final TransitionConverterLookup _converterLookup;

    public SaveAllAction(Map<String, String[]> params, Session session, TransitionConverterLookup converterLookup) {
        if (params == null)
            throw new IllegalArgumentException("params is null");

        String encodedTransitions = extractData(params);
        _meId = extractMeId(params);

        if (session == null)
            throw new IllegalArgumentException("session is null");

        if (converterLookup == null)
            throw new IllegalArgumentException("converterLookup is null");


        Type listType = new TypeToken<List<TransitionTO>>() {
        }.getType();
        _transitions = new Gson().fromJson(encodedTransitions, listType);
        _session = session;
        _converterLookup = converterLookup;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable"})
    private String extractData(Map<String, String[]> params) {
        if (params.get("data") == null)
            throw new IllegalArgumentException("data is null");

        String encodedTransitions = params.get("data")[0].replaceAll("###", "+");
        return encodedTransitions;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable"})
    private Long extractMeId(Map<String, String[]> params) {
        if (params.get("me") == null || params.get("me").length == 0)
            throw new IllegalArgumentException("me is null");

        Long meId = new Long(params.get("me")[0]);
        return meId;
    }

    public View act() {
        Sales me = (Sales) _session.load(Sales.class, _meId);
        TransitionConverter converter = _converterLookup.getTransitionConverter(_session, me);

        ArrayList<TransitionErrorTO> errors = new ArrayList<TransitionErrorTO>();
        for (TransitionTO transitionTO : _transitions) {
            try {
                List<Transition> transitions = converter.convertToEntity(transitionTO);
                for (Transition transition : transitions) {
                    _session.save(transition);
                }
            } catch (ConvertTransitionException e) {
                errors.add(new TransitionErrorTO(transitionTO.getLid(), e.getMessage()));
            }
        }

        _session.flush();

        return new JsonView<ArrayList<TransitionErrorTO>>(errors);
    }
}
