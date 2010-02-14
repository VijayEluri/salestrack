package com.tort.trade.journals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;

public class TransitionConverterImpl implements TransitionConverter {
	private static final String OUTCOME = "-";
	private static final String INCOME = "+";
	private static final Pattern _pattern = Pattern.compile("^([+-])(\\d+)?([ВС\\$])(\\d+)?$");	
	private Sales _me;
	private Session _session;
	
	public TransitionConverterImpl(Session session, Sales me) {
		if(session == null)
			throw new IllegalArgumentException("session is null");
		
		if(me == null)
			throw new IllegalArgumentException("me is null");
		
		_me = me;
		_session = session;
	}

	@Override
	public List<Transition> convertToEntity(TransitionTO transitionTO) throws ConvertTransitionException {
		if (transitionTO == null)
			throw new IllegalArgumentException();

		List<Transition> transitions = new ArrayList<Transition>();
		
		String[] transitionStrings = transitionTO.getText().split(",");
		for (String transitionString : transitionStrings) {
			Transition transition = new Transition();
			Matcher matcher = _pattern.matcher(transitionString);
			if(!matcher.find())
				throw new ConvertTransitionException("Передача " + transitionString + " неверна");
						
			String sign = matcher.group(1);
			Long number = 0L;
			try{
				number = Long.parseLong(matcher.group(2));
			} catch (NumberFormatException e) {
				
			}
			String alias = matcher.group(3);
			BigDecimal price = BigDecimal.ZERO;
			try{
				price = new BigDecimal(matcher.group(4));
			} catch (NullPointerException e) {
				
			}
			
			transition.setDate(new Date());
			transition.setMe(_me);
			transition.setGood(loadGood(transitionTO.getGoodId()));
			transition.setPrice(price);
			transition.setQuant(new Long(number));
			if(INCOME.equals(sign)){
				transition.setFrom(loadSales(alias, sign));
				transition.setTo(_me);
			} 
			if(OUTCOME.equals(sign)){
				transition.setFrom(_me);
				transition.setTo(loadSales(alias, sign));
			}	
			
			transitions.add(transition);
		}
		

		return transitions;
	}

	private Good loadGood(Long goodId) {		
		return (Good) _session.load(Good.class, goodId);
	}

	private Sales loadSales(String dest, String sign) {
		if("$".equals(dest)){
			dest = sign + dest;
		}
		
		return ((SalesAlias) _session.load(SalesAlias.class, dest)).getSales();
	}
}
