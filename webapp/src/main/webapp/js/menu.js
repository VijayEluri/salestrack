function Menu(activeMenu) {
	var currentActiveMenu;
	
	var initMenu = function (activeMenuId){
		currentActiveMenu = jQuery("table[id=sales] > tbody > tr > td[name=" + activeMenuId + "]").get(0);
		selectMenu(currentActiveMenu);
		
		jQuery("table[id=sales] > tbody > tr > td").click(function (){
			selectMenu(this);
		});
	}
	
	var selectMenu = function (menuItem){		
		jQuery(currentActiveMenu).removeClass("active");
		currentActiveMenu = menuItem;
		jQuery(currentActiveMenu).addClass("active");
		var me = jQuery(currentActiveMenu).attr("name");
		jQuery("table[class=journal] > tbody > tr[me!=" + me + "]").hide();
		jQuery("table[class=journal] > tbody > tr[me=" + me + "]").show();
	}
	
	initMenu(activeMenu);
	
	return {
		getMe: function() {
			return jQuery(currentActiveMenu).attr("name");
		}
	}
};