package com.tort.trade.journals;

import org.hibernate.Query;

public interface JournalQueryFactory {

	String getBalanceQuery();

    String getConsistencyQuery();

    String getSales();
}
