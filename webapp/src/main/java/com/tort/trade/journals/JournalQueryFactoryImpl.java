package com.tort.trade.journals;

public class JournalQueryFactoryImpl implements JournalQueryFactory {

	@Override
	public String getBalanceQuery() {
		return "select new com.tort.trade.journals.GoodBalance(transition.good, sum(transition.quant)) " +
				"from Transition transition " +
				"where transition.me = :me " +
                "and transition.date <= :today " +
				"group by transition.good.id ";
	}

    @Override
    public String getConsistencyQuery() {
        return "select new com.tort.trade.journals.DiffTO(transition.me.id, transition.good.name, " +
                "sum(case when transition.me = transition.to then transition.quant " +
                "    when transition.me = transition.from then (transition.quant*-1) end)) " +
                "from Transition transition " +
                "where transition.me = :me " +
                "and transition.from.id > 2 " +
                "and transition.to.id > 2 " +
                "group by transition.good.name, transition.me.id " +
                "having sum(case when transition.me = transition.to then transition.quant " +
                "           when transition.me = transition.from then (transition.quant*-1) end) != 0 " +
                " order by abs(sum(case when transition.me = transition.to then transition.quant " +
                "           when transition.me = transition.from then (transition.quant*-1) end)) desc, transition.good.name asc";
    }

}
