function Menu(activeMainMenu, activeMenu) {
    var mainMenu = new SubMenu('#mainMenu', 'mainMenu.html');
    var salesMenu = new SubMenu('#salesMenu', 'salesMenuFragment.html');

    var loadMenu = function (activeMainMenu, activeMenuId) {
        jQuery("div#menu").load('menu.html', [], function() {
            mainMenu.init(activeMainMenu);
            salesMenu.init(activeMenuId);
        });
    };

    loadMenu(activeMainMenu, activeMenu);

    return {
        getMe: function() {
            return salesMenu.getActive();
        },
        getMainMenu: function(){
            return mainMenu;
        },
        getSalesMenu: function(){
            return salesMenu;
        }
    }
}

function SubMenu(wrapperTag, markupUrl) {
    var active;
    var wrapper = wrapperTag;
    var markup = markupUrl;
    var listener = function(){};

    var selectMenu = function (newActiveMenu) {
        jQuery(wrapper + " > table > tbody > tr > td").removeClass("active");
        active = jQuery(newActiveMenu).attr("name");
        jQuery(newActiveMenu).addClass("active");
        listener(active);
    };
    var afterMarkupLoad = function () {
        currentMainMenu = jQuery(wrapper + " > table > tbody > tr > td[name=" + active + "]").get(0);
        selectMenu(currentMainMenu);

        jQuery(wrapper + " > table > tbody > tr > td").click(function () {
            selectMenu(this);
        });
    };
    var loadMarkup = function () {
        jQuery(wrapper).load(markup, [], function() {
            afterMarkupLoad();
        });
    };

    return {
        getActive: function() {
            return active;
        },
        init: function(initial){
            active = initial;
            loadMarkup();
        },
        bind: function (argListener) {
            listener = argListener;
        }
    }
}