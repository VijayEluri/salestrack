var consistency = function() {
    var diffs = [
        {_day: "01.01.2010", _errors: [
            {_me: 3, _good: "Джинсы синие", _diff: "+3В"}
        ]},
        {_day: "02.01.2010", _errors: [
            {_me: 3, _good: "Джинсы красные", _diff: "-2С"},
            {_me: 3, _good: "Джинсы зеленые", _diff: "+5В"}
        ]
        }
    ];
    var menu;

    var render = function(me, diffs) {
        jQuery("table#diffs > tbody").empty();
        jQuery.each(diffs, function(i, diff) {
            jQuery("table#diffs > tbody").append("<tr><td colspan='2'>" + diff._day + "</td></tr>");
            jQuery.each(diff._errors, function(j, error) {
                if (error._me == me)
                    jQuery("table#diffs > tbody").append("<tr><td>" + error._good + "</td><td>" + error._diff + "</td></tr>");
            });
        });
    };

    var loadDiffs = function() {
        jQuery.ajax({
            url: "getConsistency",
            data: "me=" + menu.getMe(),
            type: "GET",
            dataType: "json",
            error: function() {
                alert("Сервер недоступен");
            },
            success: function(data) {
                diffs = data;
                render(menu.getMe(), diffs);
            }
        });
    };

    return {
        init: function(activeMenu) {
            menu = new Menu("consistency", activeMenu);
            menu.getSalesMenu().bind(function() {
                loadDiffs();
            });
        }
    };
}();