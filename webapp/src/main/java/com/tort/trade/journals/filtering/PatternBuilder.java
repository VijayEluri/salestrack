package com.tort.trade.journals.filtering;

public class PatternBuilder {
    private final String _filter;

    public PatternBuilder(String filter) {
        if(filter == null)
            throw new IllegalArgumentException("filter is null");

        _filter = filter;
    }

    public String buildPattern() {
        final String[] strings = _filter.split("\\s");

        String result = "^";
        for (int i = 0; i < strings.length; i++) {
            if(i + 1 < strings.length){
                result += strings[i] + "[^\\s]* ";
            }else{
                result += strings[i] + ".*$";
            }
        }

        return result;
    }
}
