var balance = function(){
	var menu;
	var goodBalances = [{name: "jeans", balance: "10"}, {name: "shirt", balance: "5"}];
	
	var updateBalance = function (me){
		jQuery("#balance > tbody").html("");
		jQuery.each(getGoodBalances(me), function(){
			jQuery("#balance > tbody").append("<tr><td>" + this.name + "</td><td>" + this.balance + "</td></tr>");
		});
	}
	
	var getGoodBalances = function(me){
		if(me == 5){
			return [{name: "hat", balance: "33"}, {name: "boots", balance: "55"}];
		}
		
		return goodBalances;
	}
	
	return {
		init: function (activeMenuId){
			menu = new Menu(activeMenuId);
			menu.bind(function(me){updateBalance(me);});
			updateBalance();
		}
	}
}();