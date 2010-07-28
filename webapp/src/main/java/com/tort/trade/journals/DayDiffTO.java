package com.tort.trade.journals;

import java.util.List;

public class DayDiffTO {
    private String _date;
    private List<DiffTO> _diffs;

    public DayDiffTO(String date, List<DiffTO> diffs) {
        _date = date;
        _diffs = diffs;
    }

    public String getDate() {
        return _date;
    }
    public List<DiffTO> getDiffs() {
        return _diffs;
    }
}
