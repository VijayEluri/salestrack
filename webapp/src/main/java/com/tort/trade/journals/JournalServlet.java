package com.tort.trade.journals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class JournalServlet  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Session session = new SessionFactoryUtil().getSessionFactory().openSession();
		
		CreateTransitionConversation conversation = new CreateTransitionConversation();
		conversation.setHibernateSession(session);
		req.getSession().setAttribute("createTransitionConversation", conversation);
		
		resp.sendRedirect("journal.html");
	}
	
}
