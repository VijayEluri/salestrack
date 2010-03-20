package com.tort.trade.journals;

public interface Action<Result> {

	/**
	 * @return errors, if any
	 */
	Result act();

}
