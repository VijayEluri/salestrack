package com.tort.trade.journals.filtering;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

@Test
public class PatternBuilderTest {
    public void buildPatternPositive(){
        final String result = new PatternBuilder("дж т т").buildPattern();

        assertNotNull(result);
        assertEquals(result, "^дж[^\\s]* т[^\\s]* т.*$");
    }

    public void buildPatternNullFilter(){
        try {
            new PatternBuilder(null);
            fail();
        } catch (Exception ignored) {
        }
    }
}
