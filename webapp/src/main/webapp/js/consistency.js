var consistency = function(){
    var diffs = [{_me: 3, _good: "Джинсы синие", _diff: "+3В"},
                 {_me: 3, _good: "Джинсы красные", _diff: "-2С"},
                 {_me: 3, _good: "Джинсы зеленые", _diff: "+5В"}];
    var menu;

    var render = function(me, diffs){
        jQuery("table#diffs > tbody").empty();
        jQuery.each(diffs, function(i, diff){
            if(diff._me == me)
            jQuery("table#diffs > tbody").append("<tr><td>" + diff._good + "</td><td>" + diff._diff + "</td></tr>");
        });
    };

    var loadDiffs = function(){
        jQuery.ajax({
				url: "getConsistency",
				data: "me=" + menu.getMe(),
				type: "GET",
				dataType: "json",
				error: function(){jQuery("div[class=error]").text("Ошибка проверки журналов");},
				success: function(data){diffs = data; render(menu.getMe(), diffs);}
			});
    };

    return {
        init: function(activeMenu){
            menu = new Menu("consistency", activeMenu);
            menu.getSalesMenu().bind(function(){
                loadDiffs();
            });
        }
    };
}();