package com.tort.trade.journals;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

@Test(groups = {"functional"})
public class GoodsBalanceIT extends FunctionalTest {
	public void getBalance() throws InterruptedException{
		_selenium.open("/webapp/journal");
		_selenium.click("//td[@name='balance']/a");
		waitForElement("//td[@name='6']");
		_selenium.click("//td[@name='6']");
		
		waitForElement("//table[@id='balance']//tr[5]");
		
		assertEquals("Остатки", _selenium.getTitle());
	}
}
