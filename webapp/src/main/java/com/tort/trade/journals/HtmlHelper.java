package com.tort.trade.journals;

import java.io.IOException;
import java.util.List;

public interface HtmlHelper {

	public String render(Page page);

	public byte[] render(List<GoodBalance> model) throws IOException;

}
