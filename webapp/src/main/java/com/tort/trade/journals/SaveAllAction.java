package com.tort.trade.journals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveAllAction implements Action {

	private List<TransitionTO> _transitions;
	private TransitionConverter _converter;
	private Session _session;

	public SaveAllAction(Map<String, String[]> params, Session session,
			TransitionConverter converter) {
		if (params.get("data") == null)
			throw new IllegalArgumentException();

		String encodedTransitions = params.get("data")[0];

		Type listType = new TypeToken<List<TransitionTO>>() {}.getType();
		_transitions = new Gson().fromJson(encodedTransitions, listType);
		_session = session;
		_converter = converter;
	}

	public byte[] act() {
		ArrayList<Long> saved = new ArrayList<Long>();
		for (TransitionTO transitionTO : _transitions) {
			_session.save(_converter.convertToEntity(transitionTO));
			saved.add(transitionTO.getLid());			
		}

		return new Gson().toJson(saved).getBytes();
	}
}
