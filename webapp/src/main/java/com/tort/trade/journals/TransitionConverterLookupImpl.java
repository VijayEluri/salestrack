package com.tort.trade.journals;

import org.hibernate.Session;

import com.tort.trade.model.Sales;

public class TransitionConverterLookupImpl implements TransitionConverterLookup {

	@Override
	public TransitionConverter getTransitionConverter(Session session, Sales me) {
		return new TransitionConverterImpl(session, me);
	}

}
