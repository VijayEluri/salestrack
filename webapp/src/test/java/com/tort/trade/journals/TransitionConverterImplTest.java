package com.tort.trade.journals;

import static org.easymock.EasyMock.*;

import org.hibernate.Session;
import org.testng.annotations.Test;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;

@Test
public class TransitionConverterImplTest extends TransitionConverterTest{
	
	@Override
	protected TransitionConverter getConverter() {
		Sales me = new Sales(3L, "Г.В.");
		Sales supplier = new Sales(1L, "supplier");
		Sales buyer = new Sales(2L, "buyer");
		Sales sashaSales = new Sales(10L, "sasha");
		
		Session mockSession = createMock(Session.class);
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(eq(SalesAlias.class), isA(String.class))).andReturn(new SalesAlias(supplier));
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(eq(SalesAlias.class), isA(String.class))).andReturn(new SalesAlias(sashaSales));
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(eq(SalesAlias.class), isA(String.class))).andReturn(new SalesAlias(supplier));
		expect(mockSession.load(eq(Good.class), isA(Long.class))).andReturn(new Good());
		expect(mockSession.load(eq(SalesAlias.class), isA(String.class))).andReturn(new SalesAlias(buyer));
		
		replay(mockSession);
		
		return new TransitionConverterImpl(mockSession, me);
	}
	
	
//	Transition transition = transitions.get(0);
//	assertEquals(transition.getFrom(), supplier);
//	assertEquals(transition.getTo(), me);
//	assertEquals(transition.getMe(), me);
//	assertEquals(transition.getQuant(), new Long(1L));
//	
//	transition = transitions.get(1);
//	assertEquals(transition.getMe(), me);
//	assertEquals(transition.getFrom(), me);
//	assertEquals(transition.getTo(), sashaSales);
//	assertEquals(transition.getQuant(), new Long(3L));
//	
//	transition = transitions.get(2);
//	assertEquals(transition.getFrom(), supplier);
//	assertEquals(transition.getMe(), me);
//	assertEquals(transition.getTo(), me);
//	assertEquals(transition.getQuant(), new Long(1L));
//	assertEquals(transition.getPrice(), new BigDecimal(100));
//	
//	transition = transitions.get(3);
//	assertEquals(transition.getFrom(), me);
//	assertEquals(transition.getMe(), me);
//	assertEquals(transition.getTo(), buyer);
//	assertEquals(transition.getQuant(), new Long(2));
//	assertEquals(transition.getPrice(), new BigDecimal(250));
}
