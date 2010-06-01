package com.tort.trade.journals;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorView implements View {
    private final String _message;

    public ErrorView(String message) {
        if(message == null)
            throw new IllegalArgumentException("message is null");

        _message = message;
    }

    @Override
    public void render(HttpServletResponse resp) throws IOException {
        resp.getOutputStream().write(_message.getBytes());
    }
}
