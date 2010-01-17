package com.tort.trade.journals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveAllServlet  extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		String encodedTransitions = req.getParameter("data");		
		
		Type listType = new TypeToken<List<TransitionTO>>() {}.getType();		
		List<TransitionTO> transitions = new Gson().fromJson(encodedTransitions, listType);
		
		ArrayList<Long> saved = new ArrayList<Long>();
		for (TransitionTO transition : transitions) {
			saved.add(transition.getLid());
		}
		
		resp.getOutputStream().write(new Gson().toJson(saved).getBytes());
	}
}
