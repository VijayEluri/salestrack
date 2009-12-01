package com.tort.trade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "DEP")
@SequenceGenerator(name = "salesGenerator", sequenceName = "sales_seq")
public class Sales {
	private Long _id;
	private String _name;
	
	public Sales(){
		
	}
	
	public Sales(String name) {
		_name = name;
	}
	
	@Id
	@Column(name = "DEP_SEQ")
	@GeneratedValue(generator = "salesGenerator")
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		_id = id;
	}
	
	@NotNull
	@Column(name = "DEP_NAME")
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
}
