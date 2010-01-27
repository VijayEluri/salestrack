package com.tort.trade.journals;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.testng.annotations.Test;

import com.tort.trade.model.Transition;

@Test
public class SaveAllActionImplTest extends SaveAllActionTest {
	
	public void newActionNegative(){
		try{
			new SaveAllAction(new HashMap<String, String[]>(), null, null);
			fail();
		} catch (IllegalArgumentException e) {

		}
	}
	
	public void newActionPositive(){
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("data", new String[]{"[{_goodId: 1, _text: \"драссте вам\", _lid: 1}]"});		
		
		new  SaveAllAction(params, null, null);
	}
	
	public void actImpl(){
		Action action = positiveSetUp();				
		
		byte[] result = action.act();
		assertEquals("[1,2]", new String(result));
	}
	
	protected Action positiveSetUp() {
		Session session = createMock(Session.class);
		TransitionConverter converter = createMock(TransitionConverter.class);
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("data", new String[]{"[{_goodId: 1, _text: \"драссте вам\", _lid: 1}, {_goodId: 2, _text: \"драссте опять\", _lid: 2}]"});		
		
		SaveAllAction action = new  SaveAllAction(params, session, converter);
		
		expect(session.save(isA(Transition.class))).andStubReturn(1L);
		expect(converter.convertToEntity(isA(TransitionTO.class))).andStubReturn(new Transition());
		replay(converter, session);
		
		return action;
	}
}

