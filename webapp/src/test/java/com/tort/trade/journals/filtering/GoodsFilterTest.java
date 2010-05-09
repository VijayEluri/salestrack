package com.tort.trade.journals.filtering;

import com.tort.trade.model.Good;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

@Test
public class GoodsFilterTest {
    public void testFilterPositive() {
        final List<Good> result = new GoodsFilter(createGoods(), "дж т т").filter();

        assertNotNull(result);
        assertEquals(result.size(), 2);
    }

    public void testFilterNullGoods(){
        try {
            new GoodsFilter(null, "");
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void testFilterNullFilter(){
        try {
            new GoodsFilter(new ArrayList<Good>(), null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    private ArrayList<Good> createGoods() {
        final ArrayList<Good> goods = new ArrayList<Good>();

        Good good = new Good();
        good.setId(1L);
        good.setName("джинсы тертые три штуки");
        goods.add(good);

        good = new Good();
        good.setId(2L);
        good.setName("джинсы синие");
        goods.add(good);

        good = new Good();
        good.setId(3L);
        good.setName("джинсы тыреные тройные");
        goods.add(good);

        good = new Good();
        good.setId(4L);
        good.setName("джинсы трехногие");
        goods.add(good);

        good = new Good();
        good.setId(5L);
        good.setName("джинсы синие тертые тройные");

        return goods;
    }
}
