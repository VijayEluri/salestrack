package com.tort.trade.journals;

import com.google.gson.Gson;
import com.tort.trade.journals.filtering.GoodsFilter;
import com.tort.trade.model.Good;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetGoodsServlet extends HttpServlet{
    private static final int MAX_RESULTS = 20;

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ParamValidator paramValidator = new ParamValidator();
		paramValidator.checkParams(req);
		
		TransitionConversation conversation = (TransitionConversation) req.getSession().getAttribute(Constants.CONVERSATION);
		Session session = conversation.getHibernateSession();

        final Query query = session.createQuery(new GoodsQueryFactory().getFilteredGoodsQuery(paramValidator.getFilter()));
        query.setMaxResults(MAX_RESULTS);
        final List<Good> goods = new GoodsFilter(query.list(), paramValidator.getFilter()).filter();

        Gson gson = new Gson();
		resp.getOutputStream().write(gson.toJson(goods).getBytes());		
	}


    class ParamValidator{
		private String _filter;

		public String checkParams(HttpServletRequest req) {
			_filter = req.getParameter("filter");
			if(_filter == null){
				throw new IllegalArgumentException("filter param is null or empty");
			}
			return _filter;
		}
		
		public String getFilter() {
			return _filter;
		}
    }
}
