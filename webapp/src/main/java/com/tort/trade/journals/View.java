package com.tort.trade.journals;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View<Model> {
    void render(HttpServletResponse resp) throws IOException;
}
