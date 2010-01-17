package com.tort.trade.journals;

public class TransitionTO {
	private Long _goodId;
	private String _text;
	private Long _lid;
	
	public String getText() {
		return _text;
	}
	public void setText(String text) {
		_text = text;
	}
	public Long getLid() {
		return _lid;
	}
	public void setLid(Long lid) {
		_lid = lid;
	}
	public Long getGoodId() {
		return _goodId;
	}
	public void setGoodId(Long goodId) {
		_goodId = goodId;
	}
}
