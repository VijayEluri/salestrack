var editSales = function() {
    var mainMenu;
    var sales = [
            new Sale("Оля", "О"),
            new Sale("Саша", "С")
    ];
    var name;
    var alias;

    var editSale = function(i) {
        var sale = sales[i];
        sale.edit();
        renderSale(i);
        jQuery("input[focus]").focus();
        jQuery("input[focus]").select();
    };

    var commitEdit = function(i) {
        var name = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name] > input").val();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=name]").html(name);
        var alias = jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias] > input").val();
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=alias]").html(alias);
        jQuery("#sales > tbody > tr[index=" + i + "] > td[name=link]").html("<a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.remove(" + i + ")'>Удалить</a>");
    };

    var cancelEdit = function(i) {
        var sale = sales[i];
        sale.cancel();
        renderSale(i);
    };

    var render = function() {
        jQuery("#sales > tbody > tr:has(td)").remove();
        jQuery.each(sales, function(i, sale) {
            appendSale(i);
        });
    };

    var appendSale = function(i) {
        var sale = sales[i];
        jQuery("#sales > tbody").append("<tr index='" + i + "'></tr>");
        renderSale(i);
    };

    var renderSale = function(i) {
        var sale = sales[i];
        jQuery("#sales > tbody > tr[index=" + i + "]").html(sale.render(i));
    };

    var addSale = function() {
        var length = sales.length;
        sales.push({name: "", alias: ""});
        appendSale(length, sales[length]);
        editSale(length);
    };

    var removeSale = function(i) {
        if (confirm("Подтвердите удаление")) {
            jQuery("#sales > tbody > tr[index=" + i + "]").remove();
        }
    };

    return {
        init: function(menuItem) {
            mainMenu = new SubMenu('#mainMenu', 'mainMenu.html');
            mainMenu.init(menuItem);
            render();
        },
        edit: function(i) {
            editSale(i);
        },
        add: function() {
            addSale();
        },
        commit: function(i) {
            commitEdit(i);
        },
        cancel: function(i) {
            cancelEdit(i);
        },
        remove: function(i) {
            removeSale(i);
        }
    }
}();

function Sale(name, alias) {
    var i;
    var name = name;
    var alias = alias;
    var state = new ViewState();

    return {
        render: function(newI) {
            i = newI;
            return state.render(i, name, alias);
        },
        getName: function(){
            return name;
        },
        getAlias: function(){
            return alias;
        },
        setName: function(newName){
            name = newName;
        },
        setAlias: function(newAlias){
            alias = newAlias;
        },
        edit: function(){
            state = new EditState();
        },
        view: function(){
            state = new ViewState();
        },
        cancel: function(){
            state = new ViewState();
        }
    }
}

function ViewState() {
    return {
        render: function(i, name, alias) {
                            return "<td style='height: 29px;'>" + i + "</td>" +
                            "<td name='name'>" + name + "</td>" +
                            "<td name='alias'>" + alias + "</td>" +
                            "<td name='link'><a href='#' onclick='editSales.edit(" + i + ");'>Изменить</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.remove(" + i + ")'>Удалить</a></td>"; 
        }
    }
}

function EditState() {
    return {
        render: function(i, name, alias) {
                            return "<td style='height: 29px;'>" + i + "</td>" +
                            "<td name='name'><input focus style='width: 9em;' value='" + name + "'/></td>" +
                            "<td name='alias'><input style='width: 9em;' value='" + alias + "'/></td>" +
                            "<td name='link'><a href='#' onclick='editSales.commit(" + i + ");'>Принять</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.cancel(" + i + ")'>Отменить</a></td>";
        }
    }
}

