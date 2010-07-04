package com.tort.trade.model;

import org.hibernate.validator.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "DEP")
public class Sales {
	private Long _id;
	private String _name;
    private SalesAlias _alias;

    public Sales(){

    }
    
	public Sales(String name) {
		_name = name;
	}
	
	public Sales(Long id, String name) {
		_id = id;
		_name = name;
	}

	@Id
	@Column(name = "DEP_SEQ")
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

    @OneToOne(mappedBy = "sales")
    public SalesAlias getAlias() {
        return _alias;
    }

    public void setAlias(final SalesAlias alias) {
        _alias = alias;
    }
}
