package com.tort.trade.journals.editsales;

public class SaleTO {
    private Long _id;
    private String _name;
    private String _alias;

    public SaleTO(Long id, String name, String alias) {
        _id = id;
        _name = name;
        _alias = alias;
    }

    public void setAlias(String alias) {
        _alias = alias;
    }

    public void setId(Long id) {
        _id = id;
    }

    public void setName(String name) {
        _name = name;
    }
}
