package com.tort.trade.journals;

import org.hibernate.Query;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test
public class GoodsQueryFactoryTest extends QueryTest {
    private static final String FILTER_STRING = "дж т";

    public void testGetFilteredGoodsQuery() {
        final GoodsQueryFactory factory = new GoodsQueryFactory();
        final String queryString = factory.getFilteredGoodsQuery(FILTER_STRING);

        final Query query = _session.createQuery(queryString);
        final List goods = query.list();

        assertNotNull(goods);
        assertTrue(goods.size() > 0);
    }
}
