var editSales = function() {
    var mainMenu;
    var _sales = [];

    var editSale = function(i) {
        var sale = _sales[i];
        sale.edit();
    };

    var commitEdit = function(i) {
        var sale = _sales[i];
        sale.commit();
    };

    var cancelEdit = function(i) {
        var sale = _sales[i];
        sale.cancel();
    };

    var getSales = function(){
        jQuery.ajax({
            url: "editSales",
            data: "command=get",
            type: "GET",
            dataType: "json",
            error: function(){alert("Сервер недоступен")},
            success: function(response){
                if(response._error != null){
                    alert(response._error);
                    return;
                }

                _sales = [];
                jQuery("#sales > tbody > tr:has(td)").remove();
                jQuery.each(response, function(i, saleTO) {
                    var sale = new Sale(saleTO._id, saleTO._name, saleTO._alias);
                    _sales[sale.getVisibleId()] = sale;
                    createNewTR(sale.getVisibleId());
                    sale.render();
                });
            }
        });
    };

    var render = function() {
        getSales();
    };

    var createNewTR = function(i) {
        jQuery("#sales > tbody").append("<tr index='" + i + "'></tr>");
    };

    var addSale = function() {
        var sale = new Sale("new", "", "");
        _sales[sale.getVisibleId()] = sale;
        createNewTR(sale.getVisibleId());
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

var visibleIdCounter = 0;
function Sale(aId, aName, aAlias) {
    var visibleId = visibleIdCounter++;
    var id = aId;
    var name = aName;
    var alias = aAlias;
    var state = new ViewState();

    var render = function() {
        jQuery("#sales > tbody > tr[index=" + visibleId + "]").html(state.render(visibleId, name, alias));
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
            var newName = jQuery("#sales > tbody > tr[index=" + visibleId + "] > td[name=name] > input").val();
            var newAlias = jQuery("#sales > tbody > tr[index=" + visibleId + "] > td[name=alias] > input").val();

            jQuery.ajax({
                url: "editSales",
                data: "command=" + state.getCommand() + "&saleId=" + id + "&saleName=" + newName + "&saleAlias=" + newAlias,
                type: "POST",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: "json",
                error: function(error){
                    alert(error.statusText);
                },
                success: function(response){
                    if(response._error != null){
                        alert(response._error);
                        return;
                    }

                    id = response._id;
                    name = response._name;
                    alias = response._alias;
                    state = viewState;
                    
                    render();
                }
            });
        },
        getVisibleId: function(){
            return visibleId;
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
        },
        getCommand: function(){
            return "update";
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
        },
        getCommand: function(){
            return "create";
        }
    }
}

var newState = new NewState();
var viewState = new ViewState();
var editState = new EditState();
