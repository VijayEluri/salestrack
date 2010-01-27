package com.tort.trade.journals;

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
}
