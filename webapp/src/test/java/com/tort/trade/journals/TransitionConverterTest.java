package com.tort.trade.journals;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public abstract class TransitionConverterTest {
	public void positive() throws ConvertTransitionException{
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("+3В,-1С,1$250");
		
		assertNotNull(getConverter().convertToEntity(transitionTO));		
	}
	
	public void badTransitionText(){
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("blablabla");
		
		try{
			getConverter().convertToEntity(transitionTO);
			fail();
		} catch (ConvertTransitionException e) {
			
		}
	}
	
	public void nullTransition() throws ConvertTransitionException{
		try{
			getConverter().convertToEntity(null);
			fail();
		}catch (IllegalArgumentException e) {
			
		}
	}
	
	protected abstract TransitionConverter getConverter();
}