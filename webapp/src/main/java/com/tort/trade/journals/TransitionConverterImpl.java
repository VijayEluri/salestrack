package com.tort.trade.journals;

import java.util.ArrayList;
import java.util.List;

import com.tort.trade.model.Transition;

public class TransitionConverterImpl implements TransitionConverter {

	@Override
	public List<Transition> convertToEntity(TransitionTO transitionTO) throws ConvertTransitionException {
		if(transitionTO == null)
			throw new IllegalArgumentException();
		
		List<Transition> transitions = new ArrayList<Transition>();
		if(transitionTO.getText().equals("+3В,-1С,1$250")){
			 
		}else{
			throw new ConvertTransitionException();
		}
		
		return transitions;
	}

}
