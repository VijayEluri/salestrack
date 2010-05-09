package com.tort.trade.journals.filtering;

import com.tort.trade.model.Good;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class GoodsFilter {
    private final List<Good> _goods;
    private Pattern _pattern;

    public GoodsFilter(List<Good> goods, String filter) {
        if(goods == null)
            throw new IllegalArgumentException("goods is null");

        if(filter == null)
            throw new IllegalArgumentException("filter is null");

        _goods = goods;
        _pattern = Pattern.compile(new PatternBuilder(filter).buildPattern());
    }

    public List<Good> filter(){
        for (Iterator iterator = _goods.iterator(); iterator.hasNext();) {
            Good good = (Good) iterator.next();
            if(!matches(good))
                iterator.remove();
        }

        return _goods;
    }

    private boolean matches(Good good) {
        return _pattern.matcher(good.getName()).matches();
    }

}
