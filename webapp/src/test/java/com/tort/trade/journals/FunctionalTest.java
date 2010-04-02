package com.tort.trade.journals;

import static org.testng.Assert.fail;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.thoughtworks.selenium.DefaultSelenium;

public class FunctionalTest {
	protected DefaultSelenium _selenium;

	@BeforeClass
	public void setUpSuit() {
		_selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/");
		_selenium.start();		
	}

	@AfterClass
	public void tearDownFunctional() {
		_selenium.stop();
	}
	
	protected void waitForElement(String element) throws InterruptedException {
		for (int second = 0;; second++) {
			if (second >= 10) fail("timeout");
			try { if (_selenium.isElementPresent(element)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}
