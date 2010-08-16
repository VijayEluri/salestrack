package com.tort.trade.journals;

import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JournalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = new SessionFactoryUtil().getSessionFactory().openSession();
        JournalQueryFactory _queryFactory = new JournalQueryFactoryImpl();

        TransitionConversation conversation = new TransitionConversation();
        conversation.setHibernateSession(session);
        req.getSession().setAttribute(Constants.CONVERSATION, conversation);

        if (conversation.getInconsistent() == null) {
            Query query = conversation.getHibernateSession().createQuery(_queryFactory.getConsistencyQuery());
            conversation.setInconsistent(query.list());
        }

        resp.sendRedirect("journal.html");
    }

}
