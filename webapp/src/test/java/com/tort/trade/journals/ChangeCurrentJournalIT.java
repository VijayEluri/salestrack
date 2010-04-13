package com.tort.trade.journals;

import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.*;

import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleniumException;

@Test(groups = {"functional"})
public class ChangeCurrentJournalIT extends FunctionalTest {
	public void changeJournal() throws InterruptedException{
		_selenium.open("/webapp/journal");		
			
		assertEquals(_selenium.getAttribute("//td[@id='salesMenu']/table//tr[1]/td[1]@class"), "active");
		
		try{
			_selenium.getAttribute("//td[@id='salesMenu']/table//tr[1]/td[2]@class");
			fail();
		} catch (SeleniumException e){
			
		}
		
		
		_selenium.click("//td[@id='salesMenu']/table/tbody/tr[1]/td[2]");
		
		try{
			_selenium.getAttribute("//td[@id='salesMenu']/table//tr[1]/td[1]@class");
			fail();
		} catch (SeleniumException e){
			
		}
		assertEquals(_selenium.getAttribute("//td[@id='salesMenu']/table//tr[1]/td[2]@class"), "active");
	}
}
