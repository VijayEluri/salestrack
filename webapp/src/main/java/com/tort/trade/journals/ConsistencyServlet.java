package com.tort.trade.journals;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsistencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<DiffTO> model = new ArrayList<DiffTO>();
        model.add(new DiffTO(3, "Джинсы", "-2В"));

        resp.getOutputStream().write(new JsonView<List<DiffTO>>().render(model));
    }
}
