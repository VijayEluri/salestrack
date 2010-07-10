package com.tort.trade.model;

import java.math.BigDecimal;
import java.util.Date;

import org.testng.annotations.Test;

@Test
public class TransitionEntityTest extends EntityTest{
	public void newTransition(){
		Sales from = new Sales("name");
		_session.save(from);
		
		Sales to = new Sales("name");
		_session.save(to);
		
		Good good = new Good();
		good.setId(3L);
		_session.save(good);
		
		Transition transition = new Transition();
		transition.setDate(new Date());
		transition.setFrom(from);
		transition.setTo(to);		
		transition.setGood(good);
		transition.setMe(from);
		transition.setSellPrice(BigDecimal.TEN);
		transition.setQuant(1L);
		
		_session.save(transition);
		_session.flush();
	}
}
