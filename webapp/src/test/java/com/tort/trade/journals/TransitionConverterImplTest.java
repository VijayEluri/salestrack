package com.tort.trade.journals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class TransitionConverterImplTest extends TransitionConverterTest{

	@Override
	protected TransitionConverter getConverter() {
		return new TransitionConverterImpl();
	}
}
