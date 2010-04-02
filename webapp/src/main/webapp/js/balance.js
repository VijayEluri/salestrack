var balance = function(){
	var menu;
	var goodBalances = [{name: "jeans", balance: "10"}, {name: "shirt", balance: "5"}];
	
	var updateBalance = function (){
		jQuery("#balance > tbody").html("");
		jQuery.each(goodBalances, function(){
			jQuery("#balance > tbody").append("<tr><td>" + this.name + "</td><td>" + this.balance + "</td></tr>");
		});
	}
	
	return {
		init: function (activeMenuId){
			menu = new Menu(activeMenuId);
			updateBalance();
		}
	}
}();