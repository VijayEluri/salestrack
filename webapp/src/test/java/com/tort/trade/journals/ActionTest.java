package com.tort.trade.journals;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public abstract class ActionTest {
	private Action _action;

	@BeforeTest
	public void setUp(){		
		_action = createAction();
	}

	public void act() {				
		assertNotNull(_action.act());		
	}	
	
	protected abstract Action createAction();
}