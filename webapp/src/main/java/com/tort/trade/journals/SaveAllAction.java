package com.tort.trade.journals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveAllAction implements Action {

	private List<TransitionTO> _transitions;

	public SaveAllAction(Map<String, String[]> params) {
		if(params.get("data") == null)
			throw new IllegalArgumentException();
			
		String encodedTransitions = params.get("data")[0];		
		
		Type listType = new TypeToken<List<TransitionTO>>() {}.getType();		
		_transitions = new Gson().fromJson(encodedTransitions, listType);
	}

	public byte[] act() {
		ArrayList<Long> saved = new ArrayList<Long>();
		for (TransitionTO transition : _transitions) {
			saved.add(transition.getLid());
		}
					
		return new Gson().toJson(saved).getBytes();
	}

}
