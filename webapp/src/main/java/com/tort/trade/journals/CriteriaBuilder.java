package com.tort.trade.journals;

public class CriteriaBuilder {
    private final String _query;
    private final String _var;

    public CriteriaBuilder(String var, String query) {
        if (query == null)
            throw new IllegalArgumentException("query is null");

        if (var == null)
            throw new IllegalArgumentException("var is null");

        _query = query;
        _var = var;
    }

    public String createCriterias() {
        String result = null;
        for (String word : _query.split("\\s")) {
            if (result == null) {
                result = " where " + _var + " like " + "'" + word + "%";
            } else {
                result += " " + word + "%";
            }
        }
        result += "'";

        return result;
    }
}
