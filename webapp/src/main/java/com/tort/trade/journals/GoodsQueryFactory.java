package com.tort.trade.journals;

public class GoodsQueryFactory {
    public String getFilteredGoodsQuery(String filter) {
        final String query = "select good from Good good ";
        
        return query + new CriteriaBuilder("good.name", filter).createCriterias();
    }
}
