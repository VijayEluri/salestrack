package com.tort.trade.journals;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonView<Model> implements View<Model> {
    private final Model _model;

    public JsonView(Model model) {
        _model = model;
    }

    @Override
    public void render(HttpServletResponse resp) throws IOException {
        byte[] bytes = new Gson().toJson(_model).getBytes();

        resp.getOutputStream().write(bytes);
    }
}
