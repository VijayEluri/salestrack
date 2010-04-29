package com.tort.trade.journals;

public interface TransitionOperationFactory {
    Operation createOperation(String _transitionString) throws ConvertTransitionException;
}
