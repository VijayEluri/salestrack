var balance = function(){
	var menu;
	var goodBalances = [{name: "jeans", balance: "10"}, {name: "shirt", balance: "5"}];
	
	var updateBalance = function (me){
		jQuery("#balance > tbody").empty();
		requestBalance(me);
	}
	
	var getGoodBalances = function(me){
		if(me == 5){
			return [{name: "hat", balance: "33"}, {name: "boots", balance: "55"}];
		}
		
		return goodBalances;
	}
	
	var requestBalance = function(me){
		jQuery.ajax({
			url: "balance",			
			data: "me=" + menu.getMe(),
			type: "GET",
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			dataType: "json",						
			error: function(){jQuery("div[class=error]").text("Ошибка сохранения передач");},
			success: function(balances){
				jQuery.each(balances, function(){
					jQuery("#balance > tbody").append("<tr><td>" + this._good._name + "</td><td>" + this._balance + "</td></tr>");
				});
			}
		});
	}
	
	return {
		init: function (activeMenuId){
			menu = new Menu(activeMenuId);
			menu.bind(function(activeMenuId){updateBalance(activeMenuId);});
		}
	}
}();