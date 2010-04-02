package com.tort.trade.journals;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.easymock.EasyMock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(enabled = false)
public class HtmlHelperTest {
	private HtmlHelper _htmlHelper;
	
	@BeforeMethod
	public void setUp(){
		_htmlHelper = EasyMock.createMock(HtmlHelper.class);
	}
	
	public void substituteTable() throws IOException{
		Page page = new Page(readHtmlFile("test.html"));
		String result = _htmlHelper.render(page);
		
		assertNotNull(result);
		assertTrue(result.contains("джинсы синие"));
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
}
