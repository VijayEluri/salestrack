var period = function(){
    var menu;

    var periodBegin;
    var periodEnd;
    var _overall;
    var _income;
    var _salary;

    var render = function (){
        jQuery("#overall").text(_overall);
        jQuery("#income").text(_income);
        jQuery("#salary").text(_salary);
    };

    return {
        init: function(){
            menu = new Menu("period", 7);

            _overall = "1000";
            _income = "800";
            _salary = "500";
            
            jQuery("#start").dateinput({selectors: true, trigger: true, format: "dd/mm/yyyy"});
            jQuery("#finish").dateinput({selectors: true, trigger: true, format: "dd/mm/yyyy"});
            jQuery("#start").data("dateinput").setValue(new Date());
            jQuery("#start").change(function(event, date){
                if(initFinished){
                    today = jQuery("#start").data("dateinput").getValue();
                }

                return true;
            });

            render();
        }
    }
}();