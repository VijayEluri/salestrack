package com.tort.trade.journals;

public class DiffTO {
    private Long _me;
    private String _good;
    private String _diff;

    public DiffTO(Long me, String good, String diff) {
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

    public String getDiff() {
        return _diff;
    }
}
