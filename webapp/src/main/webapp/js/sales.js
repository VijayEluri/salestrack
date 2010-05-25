var editSales = function(){
    var mainMenu;
    var sales = [{name: "Саша", alias: "С"}, {name: "Оля", alias: "О"}];
    var name;
    var alias;

    var editSale = function(i) {
        name = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html("<input style='width: 9em;' value='" + name + "'/>");
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").focus();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").select();
        alias = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html("<input style='width: 9em;' value='" + alias + "'/>");
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=link]").html("<a href='#' onclick='editSales.commit(" + i + ");'>Принять</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.cancel(" + i + ")'>Отменить</a>");
    };

    var commitEdit = function(i) {
        var name = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").val();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html(name);
        var alias = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias] > input").val();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html(alias);
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=link]").html("<a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.remove(" + i + ")'>Удалить</a>");
    };

    var cancelEdit = function(i){
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html(name);
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html(alias);
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=link]").html("<a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.remove(" + i + ")'>Удалить</a>");
        if(name == "" && alias == ""){
            removeSale(i);
        }
    };

    var render = function() {
        jQuery("#sales > tbody > tr:has(td)").remove();
        jQuery.each(sales, function(i, sale) {
            appendSale(i, sale);
        });
    };

    var appendSale = function(i, sale) {
        jQuery("#sales > tbody").append("<tr index='" + i + "'><td style='height: 29px;'>" + i + "</td><td name='name'>" + sale.name + "</td><td name='alias'>" + sale.alias + "</td><td name='link'><a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.remove(" + i + ")'>Удалить</a></td></tr>");
    };

    var addSale = function() {
        var length = sales.length;
        sales.push({name: "", alias: ""});
        appendSale(length, sales[length]);
        editSale(length);
    };

    var removeSale = function(i) {
        if (confirm("Подтвердите удаление")){
            jQuery("#sales > tbody > tr[index=" + i + "]").remove();
        }
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
        add: function(){
            addSale();
        },
        commit: function(i){
            commitEdit(i);
        },
        cancel: function(i){
            cancelEdit(i);
        },
        remove: function(i){
            removeSale(i);
        }
    }
}();