var consistency = function() {
    var menu;
    var oldestBorder;

    var render = function(me, dayDiffs) {
        for (var day in dayDiffs){
            jQuery("table#diffs > tbody").append("<tr><td colspan='2'>" + day + "</td></tr>");
            jQuery.each(dayDiffs[day], function(j, diff) {
                    jQuery("table#diffs > tbody").append("<tr><td>" + diff._good + "</td><td>" + diff._diff + "</td></tr>");
            });
        }
    };

    var loadDiffs = function() {
        loadBorders();
    };

    var loadBorders = function (){
        jQuery.ajax({
            url: "getBorders",
            data: "",
            type: "GET",
            dataType: "json",
            error: function(error) {
                alert(error.statusText);
            },
            success: function(result){
                oldestBorder = new Date(result._oldest);

                jQuery("table#diffs > tbody").empty();
                loadPeriod(result._newest);
            }
        });
    }

    var loadPeriod = function(today){
        var endDate = new Date(today);
        var startDate = new Date(today);
        startDate.setMonth(endDate.getMonth() - 1);
        if (startDate.getTime() < oldestBorder.getTime()){
            return;
        }

        jQuery.ajax({
            url: "getConsistency",
            data: "me=" + menu.getMe() + "&startDate=" + startDate.getTime() + "&endDate=" + endDate.getTime(),
            type: "GET",
            dataType: "json",
            error: function(error) {
                alert(error.statusText);
            },
            success: function(data) {
                diffs = data;
                render(menu.getMe(), diffs);

                loadPeriod(startDate);
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