package com.tort.trade.journals;

import org.testng.annotations.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.testng.Assert.fail;

public class ErrorViewTest {
    private static final String ERROR_MESSAGE = "message";

    @Test
    public void render() throws Exception {
        final View view = new ErrorView(ERROR_MESSAGE);

        final ServletOutputStream outputStream = createMock(ServletOutputStream.class);
        outputStream.write(isA(byte[].class));
        replay(outputStream);

        HttpServletResponse response = createMock(HttpServletResponse.class);
        expect(response.getOutputStream()).andReturn(outputStream);
        replay(response);

        view.render(response);
        verify(response);
    }


    @Test
    public void newErrorViewNullCommand(){
        try {
            new ErrorView(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
