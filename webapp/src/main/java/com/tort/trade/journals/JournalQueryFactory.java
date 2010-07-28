package com.tort.trade.journals;

public interface JournalQueryFactory {

    String getBalanceQuery();

    String getConsistencyQuery();

    String getSales();
}
