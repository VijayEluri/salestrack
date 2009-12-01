package com.tort.trade.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

@Test
public class TransitionEntityTest extends EntityTest{
	public void newTransition(){
		Sales from = new Sales();
		from.setName("name");
		_session.save(from);
		
		Sales to = new Sales();
		to.setName("name");
		_session.save(to);
		
		Good good = new Good();
		_session.save(good);
		
		Transition transition = new Transition();
		transition.setDate(new Date());
		transition.setFrom(from);
		transition.setTo(to);		
		transition.setGood(good);
		transition.setJournal(from);
		transition.setPrice(BigDecimal.TEN);
		transition.setQuant(1L);
		
		_session.save(transition);
	}

	@Override
	protected List<Class> getClasses() {
		return Arrays.asList(new Class[]{
			Transition.class	
		});
	}
}
