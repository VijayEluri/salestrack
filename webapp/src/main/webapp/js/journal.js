journal = function() {
    var goods = [];
    var transitionCounter = 0;
    var menu;
    var initFinished = false;
    var today = new Date();

    var focusFilter = function () {
        jQuery("input[class=filter]").focus();
    };

    var addNewTransition = function (label, goodId, me) {
        transitionCounter++;
        jQuery("table[class=journal]").append("<tr good_id='" + goodId + "' transition_lid='" + transitionCounter + "' me='" + me + "'><td>11.05.09</td><td>" + label + "</td><td><input type='text' class='newTransition' onkeypress='return journal.transitionInputFilter(event, " + transitionCounter + ");'></td><td><a href='#' onclick='journal.removeTransition(" + transitionCounter + ")'>Удалить</a></td></tr>");
    };

    var setupGoodsEventHandlers = function () {
        jQuery("td[class=priceItem]").click(function() {
            var goodName = jQuery(this).html();
            var goodId = jQuery(this).attr("good_id");
            var me = menu.getMe();
            addNewTransition(goodName, goodId, me);
            focusNewTransition();
        });
    };

    var updateGoods = function () {
        clearGoods();
        addGoods();
        setupGoodsEventHandlers();
        updateGoodsCount(goods.length);
    };

    var updateGoodsCount = function (count){
        jQuery("#goodCount").html(count);
    };

    var goodsFilter = function () {
        return jQuery("#filter").val();
    };

    var addGoods = function () {
        jQuery.each(goods, function(i, good) {
            var id = "";
            if (i <= 9)
                id = i;
            addGood(good, id);
        });
    };

    var focusNewTransition = function () {
        jQuery("input[class=newTransition]").focus();
    };

    var clearGoods = function () {
        jQuery("#goods").empty();
    };

    var addGood = function (good, number) {
        jQuery("#goods").append("<tr><td>" + number + "</td><td class='priceItem' number='" + number + "' good_id='" + good._id + "'>" + good._name + "</td></tr>");
    };

    var getNewTransitions = function () {
        var transitions = [];
        jQuery("table[class=journal] > tbody > tr:has(td)").each(function() {
            transitions.push(createTransitionTO(this));
        });

        return transitions;
    };

    var createTransitionTO = function (tr) {
        var transition = new Object();
        transition._goodId = tr.getAttribute("good_id");
        transition._text = tr.childNodes[2].childNodes[0].value;
        transition._lid = tr.getAttribute("transition_lid");

        return transition;
    }

    var saveTransitions = function (transitions) {
        jQuery.ajax({
            url: "saveAll",
            data: "me=" + menu.getMe() + "&data=" + $.toJSON(transitions).replace(/\+/g, '###'),
            type: "POST",
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            error: function() {
                jQuery("div[class=error]").text("Ошибка сохранения передач");
            },
            success: function(errors) {
                transitions.forEach(function(item) {
                    var tr = jQuery("table[class=journal] > tbody > tr[transition_lid = " + item._lid + "]:visible").get(0);
                    if (contain(errors, item._lid)) {
                        tr.childNodes[2].childNodes[0].className = 'badTransitionText';
                    } else {
                        tr.childNodes[2].childNodes[0].disabled = true;
                    }
                });
            }
        });
    }

    var contain = function (errors, lid) {
        var i;
        for (i in errors) {
            if (errors[i]._lid == lid) {
                return true;
            }
        }

        return false;
    }

    return {
        init: function(activeMenuId) {
            menu = new Menu("journal", activeMenuId);
            menu.getSalesMenu().bind(function(me) {
                jQuery("table[class=journal] > tbody > tr[me!=" + me + "]").hide();
                jQuery("table[class=journal] > tbody > tr[me=" + me + "]").show();

                focusFilter();
                initFinished = true;
            });

            jQuery("#today").dateinput({selectors: true, trigger: true, format: "dd/mm/yyyy"});
            jQuery("#today").data("dateinput").setValue(today);
            jQuery("#today").change(function(event, date){
                if(initFinished){
                    today = jQuery("#today").data("dateinput").getValue();
                }

                return true;
            });

            journal.refreshGoods();
            focusFilter();
        },
        clear: function() {
            jQuery("table[class=journal] > tbody > tr:has(td > input:disabled)").remove();
        },
        transitionInputFilter: function (e, transitionLid) {
            if (e.keyCode == 13) {
                var transition = jQuery("table[class=journal] > tbody > tr[transition_lid = " + transitionLid + "]").get(0);
                saveTransitions([createTransitionTO(transition)]);

                jQuery("input[class=filter]").val("");
                focusFilter();
            }
        },
        refreshGoods: function () {
            jQuery.ajax({
                url: "getGoods",
                data: "filter=" + goodsFilter(),
                type: "GET",
                dataType: "json",
                error: function() {
                    jQuery("div[class=error]").text("Ошибка получения прайса");
                },
                success: function(data) {
                    goods = data;
                    updateGoods();
                }
            });
        },
        goodsSearchFilter: function (e) {
            var keynum;
            if (window.event) // IE
            {
                keynum = e.keyCode;
            } else if (e.which) { // Netscape/Firefox/Opera
                keynum = e.which;
            }
            var keychar = String.fromCharCode(keynum);
            var numcheck = /\d/;

            if (!numcheck.test(keychar)) {
                return true;
            } else {
                jQuery("td[class=priceItem][number=" + keychar + "]").click();
                return false;
            }
        },
        saveAll: function () {
            saveTransitions(getNewTransitions());
        },
        removeTransition: function (transitionLid) {
            if (confirm("Подтвердите удаление")) {
                jQuery("table[class=journal] > tbody > tr[transition_lid = " + transitionLid + "]").remove();
            }
        }
    }
}();