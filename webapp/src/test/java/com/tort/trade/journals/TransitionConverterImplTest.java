package com.tort.trade.journals;

import static org.easymock.EasyMock.*;
import static org.testng.AssertJUnit.*;

import org.hibernate.Session;
import org.testng.annotations.Test;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;

@Test
public class TransitionConverterImplTest extends TransitionConverterTest{
	
	public void newConverterNullSession(){
		try{
			new TransitionConverterImpl(null, new Sales());
			fail();
		}catch (IllegalArgumentException e) {
			
		}
	}
	
	public void newConverterNullMe(){
		Session session = createMock(Session.class);
		try{
			new TransitionConverterImpl(session, null);
			fail();
		}catch (IllegalArgumentException e) {
			
		}
	}
	
	@Override
	protected TransitionConverter getConverter() {
		Sales me = new Sales(3L, "Г.В.");
		Sales supplier = new Sales(1L, "supplier");
		Sales buyer = new Sales(2L, "buyer");
		Sales sashaSales = new Sales(10L, "sasha");
		
		Session mockSession = createMock(Session.class);
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(SalesAlias.class, "+$")).andReturn(new SalesAlias(supplier));
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(SalesAlias.class, "С")).andReturn(new SalesAlias(sashaSales));
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(SalesAlias.class, "+$")).andReturn(new SalesAlias(supplier));
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(SalesAlias.class, "-$")).andReturn(new SalesAlias(buyer));
		
		replay(mockSession);
		
		return new TransitionConverterImpl(mockSession, me);
	}
}
