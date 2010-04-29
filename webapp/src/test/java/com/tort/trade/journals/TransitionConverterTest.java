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
		transitionTO.setGoodId(1L);

		TransitionConverter converter = getConverter();
		List<Transition> transitions = converter.convertToEntity(transitionTO);

		assertNotNull(transitions);
		assertEquals(transitions.size(), 4);
	}
	
	public void nullTransition() throws ConvertTransitionException{
		try{
			getConverter().convertToEntity(null);
			fail();
		}catch (IllegalArgumentException ignored) {
			
		}
	}
	
	protected abstract TransitionConverter getConverter() throws ConvertTransitionException;
}