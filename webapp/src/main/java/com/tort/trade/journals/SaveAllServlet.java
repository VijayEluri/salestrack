package com.tort.trade.journals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SaveAllServlet extends HttpServlet {

    @SuppressWarnings({"unchecked"})
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        TransitionConversation conversation = (TransitionConversation) req.getSession().getAttribute(Constants.CONVERSATION);
        SaveAllAction action = new SaveAllAction(req.getParameterMap(), conversation.getHibernateSession(), new TransitionConverterLookupNoScoped());

        action.act().render(resp);
    }
}
