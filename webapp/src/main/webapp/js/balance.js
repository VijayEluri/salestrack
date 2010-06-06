var balance = function(){
	var menu;
	
	var updateBalance = function (me){
        jQuery.ajax({
            url: "balance",
            data: "me=" + menu.getMe(),
            type: "GET",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: "json",
            error: function(){alert("Сервер недоступен")},
            success: function(balances){
                jQuery("#balance > tbody").empty();
                jQuery.each(balances, function(){
                    jQuery("#balance > tbody").append("<tr><td>" + this._good._name + "</td><td>" + this._balance + "</td></tr>");
                });
            }
        });
	}
	
	return {
		init: function (activeMenuId){
			menu = new Menu("balance", activeMenuId);
			menu.getSalesMenu().bind(function(activeMenuId){updateBalance(activeMenuId);});
		}
	}
}();