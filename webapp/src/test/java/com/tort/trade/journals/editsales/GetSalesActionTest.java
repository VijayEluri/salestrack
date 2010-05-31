package com.tort.trade.journals.editsales;

import com.tort.trade.journals.Action;
import com.tort.trade.journals.ActionTest;
import org.testng.annotations.Test;

@Test
public class GetSalesActionTest extends ActionTest {
    @Override
    protected Action positiveSetUp() {
        return new GetSalesAction();
    }
}
