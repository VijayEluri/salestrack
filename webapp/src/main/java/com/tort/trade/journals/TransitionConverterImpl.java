package com.tort.trade.journals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;

public class TransitionConverterImpl implements TransitionConverter {
    private TransitionOperationFactory _operationFactory;

    public TransitionConverterImpl(Session session, Sales me) {
		if(session == null)
			throw new IllegalArgumentException("session is null");
		
		if(me == null)
			throw new IllegalArgumentException("me is null");

        _operationFactory = new TransitionOperationFactory(session, me);
    }

	@Override
	public List<Transition> convertToEntity(TransitionTO transitionTO) throws ConvertTransitionException {
		if (transitionTO == null)
			throw new IllegalArgumentException();

		List<Transition> transitions = new ArrayList<Transition>();
		
		String[] transitionStrings = transitionTO.getText().split(",");
		for (String transitionString : transitionStrings) {
            TransitionOperation operation = _operationFactory.createOperation(transitionString);
            Transition transition = operation.createTransition(transitionTO);
			
			transitions.add(transition);
		}
		

		return transitions;
	}

}
    