package com.tort.trade.journals;

import java.util.List;

import com.tort.trade.model.Transition;

public interface TransitionConverter {

	public List<Transition> convertToEntity(TransitionTO transitionTO) throws ConvertTransitionException;
}
