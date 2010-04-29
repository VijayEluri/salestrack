package com.tort.trade.journals;

import com.tort.trade.model.Transition;

public interface Operation {
    Transition createTransition(TransitionTO transitionTO);
}
