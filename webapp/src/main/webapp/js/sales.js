var editSales = function(){
    var mainMenu;
    var sales = [{name: "Саша", alias: "С"}, {name: "Оля", alias: "О"}];

    var render = function() {
        jQuery("#sales > tbody > tr:has(td)").remove();
        jQuery.each(sales, function(i, sale) {
            jQuery("#sales > tbody").append("<tr><td>" + i + "</td><td>" + sale.name + "</td><td>" + sale.alias + "</td><td><a href='#'>Изменить</a></td></tr>")
        });
    };

    return {
        init: function(menuItem){
            mainMenu = new SubMenu('#mainMenu', 'mainMenu.html');
            mainMenu.init(menuItem);
            render();
        }
    }
}();