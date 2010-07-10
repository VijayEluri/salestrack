package com.tort.trade.model;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

@Test
public class SalesEntityTest extends EntityTest{
	public void newSales(){
		Sales sales = new Sales("sales name");
		
		_session.save(sales);
		_session.flush();
	}
}
