package com.tort.trade.model;

import javax.persistence.Entity;


@Entity
public class SalesAlias {
	private String _id;
	private Sales _sales;

	public SalesAlias(Sales sales) {
		_sales = sales;
	}
	
	public SalesAlias(){
		
	}

	public Sales getSales() {
		return _sales;
	}

	public void setSales(Sales sales) {
		_sales = sales;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}
}
