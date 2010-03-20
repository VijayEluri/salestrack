package com.tort.trade.journals;

import org.testng.annotations.Test;

@Test(groups = {"functional"})
public class BalanceIT extends FunctionalTest {
	public void getBalance(){
		_selenium.open("/webapp/balance.html");
	}
}
