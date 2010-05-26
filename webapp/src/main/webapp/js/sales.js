var editSales = function() {
    var mainMenu;
    var sales = [
            new Sale(0, "Оля", "О"),
            new Sale(1, "Саша", "С")
    ];

    var editSale = function(i) {
        var sale = sales[i];
        sale.edit();
    };

    var commitEdit = function(i) {
        var sale = sales[i];
        sale.commit();
    };

    var cancelEdit = function(i) {
        var sale = sales[i];
        sale.cancel();
    };

    var render = function() {
        jQuery("#sales > tbody > tr:has(td)").remove();
        jQuery.each(sales, function(i, sale) {
            createNewTR(i);
            sale.render();
        });
    };

    var createNewTR = function(i) {
        jQuery("#sales > tbody").append("<tr index='" + i + "'></tr>");
    };

    var addSale = function() {
        var length = sales.length;
        var sale = new Sale(length, "", "");
        sales.push(sale);
        createNewTR(length);
        sale.init();
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

function Sale(aI, aName, aAlias) {
    var name = aName;
    var alias = aAlias;
    var state = new ViewState();

    var render = function() {
        jQuery("#sales > tbody > tr[index=" + aI + "]").html(state.render(aI, name, alias));
    };

    var focusAndSelect = function(){
        jQuery("input[focus]").focus();
        jQuery("input[focus]").select();
    };

    return {
        render: function() {
            render();
        },
        edit: function(){
            state = editState;
            render();
            focusAndSelect();
        },
        init: function(){
            state = newState;
            render();
            focusAndSelect();
        },
        cancel: function(){
            state = viewState;
            render();
        },
        commit: function(){
            name = jQuery("#sales > tbody > tr[index=" + aI + "] > td[name=name] > input").val();
            alias = jQuery("#sales > tbody > tr[index=" + aI + "] > td[name=alias] > input").val();

            state = viewState;
            render();
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


function NewState() {
    return {
        render: function(i, name, alias) {
                            return "<td style='height: 29px;'>" + i + "</td>" +
                            "<td name='name'><input focus style='width: 9em;' value='" + name + "'/></td>" +
                            "<td name='alias'><input style='width: 9em;' value='" + alias + "'/></td>" +
                            "<td name='link'><a href='#' onclick='editSales.commit(" + i + ");'>Принять</a>&nbsp;|&nbsp;<a href='#' onclick='editSales.remove(" + i + ")'>Отменить</a></td>";
        }
    }
}

var newState = new NewState();
var viewState = new ViewState();
var editState = new EditState();
