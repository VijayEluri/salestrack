package com.tort.trade.journals;

import static org.testng.AssertJUnit.assertNotNull;

import org.testng.annotations.Test;

@Test
public abstract class ActionTest {
	public void act() {				
		Action action = positiveSetUp();
		
		assertNotNull(action.act());		
	}

	protected abstract Action positiveSetUp();		
}