function Menu(activeMenu) {
	var currentActiveMenu;
	var listener = function(){};
	
	var afterLoad = function (activeMenuId){
		currentActiveMenu = jQuery("table[id=sales] > tbody > tr > td[name=" + activeMenuId + "]").get(0);
		selectMenu(currentActiveMenu);
		
		jQuery("table[id=sales] > tbody > tr > td").click(function (){
			selectMenu(this);
		});
	}
	
	var initMenu = function (activeMenuId){
		loadSalesFragmentMarkup(activeMenuId);
	}
	
	var loadSalesFragmentMarkup = function (activeMenuId){
		jQuery("#salesMenuFragmentWrapper").load("salesMenuFragment.html", [], function(){
			afterLoad(activeMenuId);
		});
	}
	
	var selectMenu = function (menuItem){		
		jQuery(currentActiveMenu).removeClass("active");
		currentActiveMenu = menuItem;
		jQuery(currentActiveMenu).addClass("active");
		var me = jQuery(currentActiveMenu).attr("name");
		listener(me);
	}
	
	initMenu(activeMenu);
	
	return {
		getMe: function() {
			return jQuery(currentActiveMenu).attr("name");
		},
		bind: function (argListener) {
			listener = argListener;
		}
	}
};