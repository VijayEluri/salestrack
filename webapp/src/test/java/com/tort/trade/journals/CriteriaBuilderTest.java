package com.tort.trade.journals;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class CriteriaBuilderTest {
    public void testCreateCriterias() throws Exception {
        final CriteriaBuilder builder = new CriteriaBuilder("goodname", "дж т т");
        final String criterias = builder.createCriterias();

        assertNotNull(criterias);
        assertEquals(criterias, " where goodname like 'дж% т% т%'");
    }

    public void testCreateEmptyCriterias(){
        final CriteriaBuilder builder = new CriteriaBuilder("goodname", "");
        final String criterias = builder.createCriterias();

        assertNotNull(criterias);
        assertEquals(criterias, "");
    }

    public void testCreateSimpleCriteria(){
        final CriteriaBuilder builder = new CriteriaBuilder("goodname", "дж");
        final String criterias = builder.createCriterias();

        assertNotNull(criterias);
        assertEquals(criterias, " where goodname like 'дж%'");
    }
}
