package com.tort.trade.journals;

import com.tort.trade.model.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionConverterImpl implements TransitionConverter {
    private final TransitionOperationFactory _operationFactory;

    public TransitionConverterImpl(TransitionOperationFactory operationFactory) {
        if (operationFactory == null)
            throw new IllegalArgumentException("operation factory is null");

        _operationFactory = operationFactory;
    }

    @Override
    public List<Transition> convertToEntity(TransitionTO transitionTO) throws ConvertTransitionException {
        if (transitionTO == null)
            throw new IllegalArgumentException();

        List<Transition> transitions = new ArrayList<Transition>();

        String[] transitionStrings = transitionTO.getText().split(",");
        for (String transitionString : transitionStrings) {
            Operation operation = _operationFactory.createOperation(transitionString);
            Transition transition = operation.createTransition(transitionTO);

            transitions.add(transition);
        }


        return transitions;
    }

}
    