package com.tort.trade.journals.consistency;

import com.tort.trade.journals.JsonView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BordersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        new JsonView(new ConsistencyPeriodBorders()).render(resp);
    }
}
