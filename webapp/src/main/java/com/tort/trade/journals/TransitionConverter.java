package com.tort.trade.journals;

import com.tort.trade.model.Transition;

public interface TransitionConverter {

	public Transition convertToEntity(TransitionTO transitionTO);
}
