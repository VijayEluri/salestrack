package com.tort.trade.journals;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleniumException;

@Test(groups = {"functional"})
public class ChangeCurrentJournalIT extends FunctionalTest {
	public void changeJournal(){
		_selenium.open("/webapp/journal");
		assertEquals(_selenium.getAttribute("//table[@id='journals']//tr[1]/td[1]@class"), "active");
		try{
			_selenium.getAttribute("//table[@id='journals']//tr[1]/td[2]@class");
			fail();
		} catch (SeleniumException e){
			
		}
		
		
		_selenium.click("//table[@id='journals']/tbody/tr[1]/td[2]");
		
		try{
			_selenium.getAttribute("//table[@id='journals']//tr[1]/td[1]@class");
			fail();
		} catch (SeleniumException e){
			
		}
		assertEquals(_selenium.getAttribute("//table[@id='journals']//tr[1]/td[2]@class"), "active");
	}
}