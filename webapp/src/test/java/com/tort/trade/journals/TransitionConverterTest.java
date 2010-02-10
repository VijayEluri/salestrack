package com.tort.trade.journals;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.testng.annotations.Test;

import com.tort.trade.model.Transition;

@Test
public abstract class TransitionConverterTest {
	public void positive() throws ConvertTransitionException{
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("+$,-3С,+$100,-2$250");
		transitionTO.setGoodId(new Long(1L));
				
		TransitionConverter converter = getConverter();
		List<Transition> transitions = converter.convertToEntity(transitionTO);
		
		assertNotNull(transitions);		
		assertEquals(transitions.size(), 4);
		
		Transition transition = transitions.get(3);
		assertEquals(transition.getMe(), transition.getFrom());
		assertEquals(transition.getQuant(), new Long(2));
		assertEquals(transition.getPrice(), new BigDecimal(250));
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
	
	public void badTransitionQuantity(){
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("+F$");
		
		try{
			getConverter().convertToEntity(transitionTO);
			fail();
		} catch (Exception e) {

		}
	}
	
	public void badTransitionPrice(){
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("+$bad");
		
		try{
			getConverter().convertToEntity(transitionTO);
			fail();
		} catch (Exception e) {

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