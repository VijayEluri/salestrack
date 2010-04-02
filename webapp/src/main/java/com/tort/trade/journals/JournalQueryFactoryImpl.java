package com.tort.trade.journals;

public class JournalQueryFactoryImpl implements JournalQueryFactory {

	@Override
	public String getBalanceQuery() {
		return "select new com.tort.trade.journals.GoodBalance(transition.good, sum(transition.quant)) " +
				"from Transition transition " +
				"group by transition.good.id ";
	}

}
