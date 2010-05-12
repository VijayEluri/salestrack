package com.tort.trade.journals;

import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConsistencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JournalQueryFactory queryFactory = new JournalQueryFactoryImpl();
        TransitionConversation conversation = (TransitionConversation) req.getSession().getAttribute("conversation");
        Session session = conversation.getHibernateSession();

        new ConsistencyAction(session, queryFactory).act().render(resp);
    }

}
