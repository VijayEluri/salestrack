package com.tort.trade.journals;

import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

import static org.testng.AssertJUnit.*;

public class SaveAllActionTest extends ActionTest {
	
	public void newAction(){
		createAction();
	}
	
	public void newActionNegative(){
		try{
			new SaveAllAction(new HashMap<String, String[]>());
			fail();
		} catch (IllegalArgumentException e) {

		}
	}
	
	public void actImpl(){
		throw new RuntimeException("not implemented");
	}
	
	protected Action createAction() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("data", new String[]{"[{_goodId: 1, _text: \"драссте вам\", _lid: 1}]"});
		
		return new SaveAllAction(params);
	}
}
