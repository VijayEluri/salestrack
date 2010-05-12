package com.tort.trade.journals;

public class DiffTO {
    private Long _me;
    private String _good;
    private Long _diff;

    public DiffTO(){
        
    }

    public DiffTO(Long me, String good, Long diff) {
        _me = me;
        _good = good;
        _diff = diff;
    }

    public Long getMe() {
        return _me;
    }

    public String getGood() {
        return _good;
    }

    public Long getDiff() {
        return _diff;
    }
}
