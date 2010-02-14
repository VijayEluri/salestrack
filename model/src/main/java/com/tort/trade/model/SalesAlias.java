package com.tort.trade.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SalesAlias {
	private String _id;
	private Sales _sales;

	public SalesAlias(Sales sales) {
		_sales = sales;
	}
	
	public SalesAlias(){
		
	}

	@ManyToOne
	public Sales getSales() {
		return _sales;
	}

	public void setSales(Sales sales) {
		_sales = sales;
	}

	@Id
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}
}
