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
		
		Transition transition = transitions.get(3);
		assertEquals(transition.getMe(), transition.getFrom());
		assertEquals(transition.getQuant(), new Long(2));
		assertEquals(transition.getSellPrice(), new BigDecimal(250));
	}

    public void positiveIncome() throws ConvertTransitionException {
        TransitionTO transitionTO = new TransitionTO();
        transitionTO.setLid(1L);
        transitionTO.setText("250/400*10");
        transitionTO.setGoodId(1L);

        TransitionConverter converter = getConverter();
        List<Transition> transitions = converter.convertToEntity(transitionTO);

        assertNotNull(transitions);
        assertEquals(transitions.size(), 1);

        Transition transition = transitions.get(0);
        assertEquals(transition.getQuant(), new Long(10));
        assertEquals(transition.getBuyPrice(), 250);
        assertEquals(transition.getSellPrice(), 400);
    }

    public void positiveSell() throws ConvertTransitionException {
        TransitionTO transitionTO = new TransitionTO();
        transitionTO.setLid(1L);
        transitionTO.setText("400*10");
        transitionTO.setGoodId(1L);

        TransitionConverter converter = getConverter();
        List<Transition> transitions = converter.convertToEntity(transitionTO);

        assertNotNull(transitions);
        assertEquals(transitions.size(), 1);

        Transition transition = transitions.get(0);
        assertEquals(transition.getQuant(), new Long(10));
        assertEquals(transition.getSellPrice(), new BigDecimal(400));
    }

    public void positiveTransition() throws ConvertTransitionException {
        TransitionTO transitionTO = new TransitionTO();
        transitionTO.setLid(1L);
        transitionTO.setText("+3С");
        transitionTO.setGoodId(1L);

        TransitionConverter converter = getConverter();
        List<Transition> transitions = converter.convertToEntity(transitionTO);

        assertNotNull(transitions);
        assertEquals(transitions.size(), 1);

        Transition transition = transitions.get(0);
        assertEquals(transition.getQuant(), new Long(3));
        assertEquals(transition.getMe(), transition.getTo());
    }
	
	public void badTransitionText(){
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("blablabla");
		
		try{
			getConverter().convertToEntity(transitionTO);
			fail();
		} catch (ConvertTransitionException ignored) {
			
		}		
	}	
	
	public void badTransitionQuantity(){
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("+F$");
		
		try{
			getConverter().convertToEntity(transitionTO);
			fail();
		} catch (Exception ignored) {

		}
	}
	
	public void badTransitionPrice(){
		TransitionTO transitionTO = new TransitionTO();
		transitionTO.setLid(1L);
		transitionTO.setText("+$bad");
		
		try{
			getConverter().convertToEntity(transitionTO);
			fail();
		} catch (Exception ignored) {

		}
	}
	
	public void nullTransition() throws ConvertTransitionException{
		try{
			getConverter().convertToEntity(null);
			fail();
		}catch (IllegalArgumentException ignored) {
			
		}
	}
	
	protected abstract TransitionConverter getConverter();
}