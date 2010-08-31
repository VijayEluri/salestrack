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
        return "select transition " +
                "from Transition transition " +
                "where transition.id not in (" +
                "select myjournal.id " +
                "from Transition myjournal, Transition opponent " +
                "where myjournal.date = opponent.date " +
                "and myjournal.me = :me " +
                "and myjournal.from.id > 2 " +
                "and myjournal.to.id > 2 " +
                "and myjournal.me.id > 2 " +
                "and opponent.me.id > 2 " +
                "and myjournal.to = opponent.to " +
                "and myjournal.from = opponent.from " +
                "and myjournal.me <> opponent.me " +
                "and myjournal.date >= :startDate " +
                "and myjournal.date < :endDate " +
                ")" +
                "and transition.from.id > 2 " +
                "and transition.to.id > 2 " +
                "and transition.me.id = :me " +
                "and transition.date >= :startDate " +
                "and transition.date < :endDate " +
                "order by transition.date ";
    }

    @Override
    public String getSales() {
        return "select new com.tort.trade.journals.editsales.SaleTO(alias.sales.id, alias.sales.name, alias.id)" +
                "from SalesAlias alias " +
                "where alias.sales.id > 2";
    }

}
