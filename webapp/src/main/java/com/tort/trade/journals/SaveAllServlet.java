package com.tort.trade.journals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveAllServlet  extends HttpServlet{
	
	private TransitionConverter _converter = new TransitionConverterImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {				
	
		CreateTransitionConversation conversation = (CreateTransitionConversation) req.getSession().getAttribute("createTransitionConversation");
		
		SaveAllAction action = new SaveAllAction(req.getParameterMap(), conversation.getHibernateSession(), _converter);
		resp.getOutputStream().write(action.act());
	}
}
