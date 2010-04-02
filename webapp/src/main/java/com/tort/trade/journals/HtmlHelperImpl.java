package com.tort.trade.journals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class HtmlHelperImpl implements HtmlHelper {

	@Override
	public String render(Page page) {
		return null;
	}

	private String readHtmlFile(String file) throws IOException {
		InputStream htmlStream = this.getClass().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream));
		StringBuilder builder = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null){
			builder.append(line);
		}
		
		return builder.toString();
	}

	@Override
	public byte[] render(List<GoodBalance> model) throws IOException {
		return readHtmlFile("balance.html").getBytes();
	}
}
