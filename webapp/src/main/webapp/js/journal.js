journal = function() {
	var goods = [];
	
	var focusFilter = function (){
		jQuery("input[class=filter]").focus();
	};
	
	var addNewTransition = function (label, goodId){		
		jQuery("table[class=journal]").append("<tr good_id='" + goodId + "'><td>11.05.09</td><td>" + label + "</td><td><input type='text' class='newTransition' style='width: 30em;' onkeypress='return journal.transitionInputFilter(event);'></td><td><a href='#' onclick='confirm(\"Подтвердите удаление\");'>Удалить</a></td></tr>");
	};
	
	var setupGoodsEventHandlers = function (){
		jQuery("td[class=priceItem]").click(function(){
			addNewTransition(this.innerHTML, this.getAttribute("good_id"));		
			focusNewTransition();
		});
	};
	
	var updateGoods = function (){	
		clearGoods();			
		addGoods();
		setupGoodsEventHandlers();		
	};
	
	var goodsFilter = function (){
		return jQuery("#filter").val();
	};
	
	var addGoods = function (){
		jQuery.each(goods, function(i, good){
			var id="";
			if(i <= 9)
				id = i;
			addGood(good, id);
		});
	};
	
	var focusNewTransition = function (){
		jQuery("input[class=newTransition]").focus();
	};
	
	var clearGoods = function (){
		jQuery("#goods").empty();
	};
	
	var addGood = function (good, number){		
		jQuery("#goods").append("<tr><td>" + number + "</td><td class='priceItem' number='" + number + "' good_id='" + good._id + "'>" + good._name + "</td></tr>");
	};	
	
	var getNewTransitions = function (){
		var transitions = [{}];
		jQuery("table[class=journal] > tbody > tr:has(td)").each(function(){
			var transition = new Object();
			transition.goodId = this.getAttribute("good_id"); 
			transition.text = this.childNodes[2].childNodes[0].value;			
			transitions.push(transition);
		});
	};
	
	return {		
		init: function(){
				journal.refreshGoods();
				focusFilter();								 				
		},
		transitionInputFilter: function (e){
					if(e.keyCode == 13){
						jQuery("input[class=filter]").val("");
						focusFilter();						
					}			
		},	
		refreshGoods: function (){		
			jQuery.ajax({
				url: "getGoods",			
				data: "filter=" + goodsFilter(),
				type: "GET",
				dataType: "json",
				error: function(){jQuery("div[class=error]").text("Ошибка получения прайса");},
				success: function(data){goods = data; updateGoods();}
			});
		},
		goodsSearchFilter: function (e){
			var keynum;
			if(window.event) // IE
	  		{
	  			keynum = e.keyCode;
	  		}else if(e.which){ // Netscape/Firefox/Opera 
	  			keynum = e.which;
	  		}
			var keychar = String.fromCharCode(keynum);
			var numcheck = /\d/;
			
			if(!numcheck.test(keychar)){			
				return true;
			}else{			
				jQuery("td[class=priceItem][number=" + keychar + "]").click();
				return false;						
			}				
		},
		saveAll: function (){
			jQuery.ajax({
				url: "saveAll",			
				data: getNewTransitions(),
				type: "POST",
				dataType: "json",
				error: function(){jQuery("div[class=error]").text("Ошибка сохранения передач");},
				success: function(data){alert("Сохранено");}
			});
		}
	}
}();