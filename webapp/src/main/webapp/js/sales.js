var editSales = function(){
    var mainMenu;
    var sales = [{name: "Саша", alias: "С"}, {name: "Оля", alias: "О"}];

    var editSale = function(i) {
        var name = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html("<input style='width: 9em;' value='" + name + "'/>");
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").focus();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").select();
        var alias = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html("<input style='width: 9em;' value='" + alias + "'/>");
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=link]").html("<a href='#' onclick='editSales.commit(" + i + ");'>Принять</a>&nbsp;|&nbsp;<a href='#'>Отменить</a>");
    };

    var commitSale = function(i) {
        var name = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").val();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html(name);
        var name = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias] > input").val();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html(name);
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=link]").html("<a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a>");
    };

    var render = function() {
        jQuery("#sales > tbody > tr:has(td)").remove();
        jQuery.each(sales, function(i, sale) {
            jQuery("#sales > tbody").append("<tr index='" + i + "'><td>" + i + "</td><td name='name'>" + sale.name + "</td><td name='alias'>" + sale.alias + "</td><td name='link'><a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a></td></tr>");
        });
    };

    return {
        init: function(menuItem){
            mainMenu = new SubMenu('#mainMenu', 'mainMenu.html');
            mainMenu.init(menuItem);
            render();
        },
        edit: function(i){
            editSale(i);
        },
        commit: function(i){
            commitSale(i);
        }
    }
}();