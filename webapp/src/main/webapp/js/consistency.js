var consistency = function(){
    var diffs = [{me: 3, good: "Джинсы синие", diff: "+3В"},
                 {me: 3, good: "Джинсы красные", diff: "-2С"},
                 {me: 3, good: "Джинсы зеленые", diff: "+5В"}];
    var menu;

    var render = function(me){
        jQuery("table#diffs > tbody").empty();
        jQuery.each(diffs, function(i, diff){
            if(diff.me == me)
            jQuery("table#diffs > tbody").append("<tr><td>" + diff.good + "</td><td>" + diff.diff + "</td></tr>");
        });
    };

    return {
        init: function(activeMenu){
            menu = new Menu("consistency", activeMenu);
            menu.getSalesMenu().bind(function(me){
                render(me);
            });
        }
    };
}();