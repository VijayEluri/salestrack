var balance = function(){
	var menu;
    var initFinished = false;
    var today = new Date();
	
	var updateBalance = function (){
        jQuery.ajax({
            url: "balance",
            data: "me=" + menu.getMe() + "&today=" + today.getTime(),
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
            jQuery("#today").dateinput({selectors: true, trigger: true, format: "dd/mm/yyyy"});
            jQuery("#today").data("dateinput").setValue(today);
            jQuery("#today").change(function(event, date){
                if(initFinished){
                    today = jQuery("#today").data("dateinput").getValue();
                    updateBalance();
                }

                return true;
            });

			menu = new Menu("balance", activeMenuId);
			menu.getSalesMenu().bind(function(activeMenuId){initFinished = true; updateBalance(activeMenuId);});
		}
	}
}();