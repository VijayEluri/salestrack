package com.tort.trade.journals;

import org.hibernate.Query;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

@Test(groups = {"integration"})
public class JournalQueryFactoryTest extends QueryTest{
	private JournalQueryFactory _queryFactory = new JournalQueryFactoryImpl();
	
	public void balanceQuery(){
		Query query = _session.createQuery(_queryFactory.getBalanceQuery());
		
		assertNotNull(query.list());
	}
}
