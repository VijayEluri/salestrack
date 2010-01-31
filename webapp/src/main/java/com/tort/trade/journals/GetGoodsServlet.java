package com.tort.trade.journals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.google.gson.Gson;
import com.tort.trade.model.Good;

public class GetGoodsServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ParamValidator paramValidator = new ParamValidator();
		paramValidator.checkParams(req);
		
		TransitionConversation conversation = (TransitionConversation) req.getSession().getAttribute(Constants.CONVERSATION);
		Session session = conversation.getHibernateSession();;
		
		Criteria criteria = session.createCriteria(Good.class);
		criteria.add(Restrictions.like("name", paramValidator.getFilter(), MatchMode.START));
		criteria.setMaxResults(20);
		List<Good> goods = criteria.list();
		
		Gson gson = new Gson();				
		resp.getOutputStream().write(gson.toJson(goods).getBytes());		
	}


	private ArrayList<String> goodsToAggs(List<Good> goods) {
		ArrayList<String> aggs = new ArrayList<String>();
		
		for (Good good : goods) {
			aggs.add(good.getName());
		}
		return aggs;
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

		public void setFilter(String filter) {
			this._filter = filter;
		}
	}
}
