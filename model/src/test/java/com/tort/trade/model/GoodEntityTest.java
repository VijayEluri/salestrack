package com.tort.trade.model;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

@Test
public class GoodEntityTest extends EntityTest{
	public void newGood(){
		Good good = new Good();
		good.setId(1L);
		
		_session.save(good);
		_session.flush();
	}

	@Override
	protected List<Class> getClasses() {		
		return Arrays.asList(new Class[]{
				Good.class
		});
	}
}
