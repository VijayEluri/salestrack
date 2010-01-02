package com.tort.trade.model;

import org.testng.annotations.Test;

@Test
public class GoodEntityTest extends EntityTest{
	public void newGood(){
		Good good = new Good();
		good.setId(1L);
		
		_session.save(good);
		_session.flush();
	}
}
