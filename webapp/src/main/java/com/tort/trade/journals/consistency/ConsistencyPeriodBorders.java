package com.tort.trade.journals.consistency;

import java.util.Date;

public class ConsistencyPeriodBorders {
    private long _newest= new Date().getTime();
    private long _oldest= _newest - (25L*30L*24L*60L*60L*1000L);

    public long getNewest() {
        return _newest;
    }

    public void setNewest(final long newest) {
        _newest = newest;
    }

    public long getOldest() {
        return _oldest;
    }

    public void setOldest(final long oldest) {
        _oldest = oldest;
    }
}