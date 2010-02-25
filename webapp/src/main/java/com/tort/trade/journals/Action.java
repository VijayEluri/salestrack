package com.tort.trade.journals;

import java.util.ArrayList;
import java.util.List;

public interface Action {

	/**
	 * @return errors, if any
	 */
	List<TransitionErrorTO> act();

}
