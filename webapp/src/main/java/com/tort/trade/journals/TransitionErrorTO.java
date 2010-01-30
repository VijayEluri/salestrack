package com.tort.trade.journals;

public class TransitionErrorTO {
	private Long _lid;
	private String _desc;
	
	public TransitionErrorTO(Long lid, String desc) {
		_lid = lid;
		_desc = desc;
	}
	public Long getLid() {
		return _lid;
	}
	public void setLid(Long lid) {
		_lid = lid;
	}
	public String getDesc() {
		return _desc;
	}
	public void setDesc(String desc) {
		_desc = desc;
	}
}
