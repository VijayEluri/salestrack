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

	private List<TransitionTO> _transitions;
	private TransitionConverter _converter;
	private Session _session;

	public SaveAllAction(Map<String, String[]> params, Session session) {
		String encodedTransitions = extractData(params);				
		Long meId = extractMeId(params);


		Type listType = new TypeToken<List<TransitionTO>>() {}.getType();
		_transitions = new Gson().fromJson(encodedTransitions, listType);		
		_session = session;
		
		Sales me = (Sales) _session.load(Sales.class, meId);
		TransitionConverter converter = new TransitionConverterImpl(session, me);
		_converter = converter;
	}

	public SaveAllAction(Map<String, String[]> params, Session session, TransitionConverter converter) {
		String encodedTransitions = extractData(params);				
		Long meId = extractMeId(params);


		Type listType = new TypeToken<List<TransitionTO>>() {}.getType();
		_transitions = new Gson().fromJson(encodedTransitions, listType);		
		_session = session;
		
		_converter = converter;
	}

	private String extractData(Map<String, String[]> params) {
		if (params.get("data") == null)
			throw new IllegalArgumentException("data is null");
		
		String encodedTransitions = params.get("data")[0].replaceAll("###", "+");
		return encodedTransitions;
	}

	private Long extractMeId(Map<String, String[]> params) {
		if (params.get("me") == null || params.get("me").length == 0)
			throw new IllegalArgumentException("me is null");
		
		Long meId = new Long(params.get("me")[0]);
		return meId;
	}

	public List<TransitionErrorTO> act() {
		ArrayList<TransitionErrorTO> errors = new ArrayList<TransitionErrorTO>();
		for (TransitionTO transitionTO : _transitions) {
			try {
				List<Transition> transitions = _converter.convertToEntity(transitionTO);
				for (Transition transition : transitions) {
					_session.save(transition);					
				}
			} catch (ConvertTransitionException e) {
				errors.add(new TransitionErrorTO(transitionTO.getLid(), e.getMessage()));							
			}
		}
		
		_session.flush();
	
		return errors;
	}
}
