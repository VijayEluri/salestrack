var consistency = function() {
    var menu;

    var render = function(me, dayDiffs) {
        jQuery("table#diffs > tbody").empty();
        for (var day in dayDiffs){
            jQuery("table#diffs > tbody").append("<tr><td colspan='2'>" + day + "</td></tr>");
            jQuery.each(dayDiffs[day], function(j, diff) {
                    jQuery("table#diffs > tbody").append("<tr><td>" + diff._good + "</td><td>" + diff._diff + "</td></tr>");
            });
        }
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