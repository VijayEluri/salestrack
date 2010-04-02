var balance = function(){
	var menu;
	
	return {
		init: function (activeMenuId){
			menu = new Menu(activeMenuId);
		}
	}
}();