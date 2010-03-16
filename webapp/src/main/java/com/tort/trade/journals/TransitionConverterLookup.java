package com.tort.trade.journals;

import org.hibernate.Session;

import com.tort.trade.model.Sales;

public interface TransitionConverterLookup {

	TransitionConverter getTransitionConverter(Session session, Sales me);

}
