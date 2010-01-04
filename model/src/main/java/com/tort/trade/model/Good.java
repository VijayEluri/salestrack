package com.tort.trade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.Length;

@Entity
@Table(name = "MAT")
public class Good {
	private Long _id;
	private String _name;
	
	@Id
	@Column(name = "SEQ_M")
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		_id = id;
	}
		
	@Column(name = "NAME")
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
}
