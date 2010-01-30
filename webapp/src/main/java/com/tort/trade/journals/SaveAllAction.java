package com.tort.trade.journals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tort.trade.model.Transition;

public class SaveAllAction implements Action {

	private List<TransitionTO> _transitions;
	private TransitionConverter _converter;
	private Session _session;

	public SaveAllAction(Map<String, String[]> params, Session session, TransitionConverter converter) {
		if (params.get("data") == null)
			throw new IllegalArgumentException();

		String encodedTransitions = params.get("data")[0].replaceAll("###", "+");		

		Type listType = new TypeToken<List<TransitionTO>>() {}.getType();
		_transitions = new Gson().fromJson(encodedTransitions, listType);		
		_session = session;
		_converter = converter;
	}

	public byte[] act() {
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

		return new Gson().toJson(errors).getBytes();
	}
}
