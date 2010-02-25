package com.tort.trade.journals;

import java.util.Collection;

import com.google.gson.Gson;

public class JsonView<E> {
	public byte[] render(Collection<E> objects) {
		return new Gson().toJson(objects).getBytes();
	}
	
}
