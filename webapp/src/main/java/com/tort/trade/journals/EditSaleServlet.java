package com.tort.trade.journals;

import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditSaleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final TransitionConversation conversation = (TransitionConversation) request.getSession().getAttribute("conversation");
        Session session = conversation.getHibernateSession();
        
        final ActionFactory actionFactory = new ActionFactory(request.getParameterMap(), session);
        final Action action = actionFactory.createAction();
        final View view = action.act();

        view.render(response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
