(function($){ 
  Utils = {};
  Utils.getUrlParam =  function(url, name){
     var pattern = new RegExp("[?&]"+name+"\=([^&]+)", "g");  
     var matcher = pattern.exec(url);  
     var items = null;  
     if(null != matcher){  
             try{  
                    items = decodeURIComponent(decodeURIComponent(matcher[1]));  
             }catch(e){  
                     try{  
                             items = decodeURIComponent(matcher[1]);  
                     }catch(e){  
                             items = matcher[1];  
                     }  
             }  
     }  
     return items;
  }

  var Nav = {
    currentMenuId : Utils.getUrlParam(window.location.href, 'menu_id'),
    data : [
      {
        "id" : "M010000",
        "txt" : "麦田地图", 
        "link" : "", 
        "icon" : "",
        "hasChild" : "true",
        "children" : [{
          "id" : "M010100",
          "txt" : "麦粒", 
          "link" : "grain.jsp?menu_id=M010100", 
          "icon" : "",
          "hasChild" : "false"
        },{
          "id" : "M010200",
          "txt" : "朋友", 
          "link" : "friend.jsp?menu_id=M010200", 
          "icon" : "",
          "hasChild" : "false"
        },{
          "id" : "M010300",
          "txt" : "POI收集器", 
          "link" : "poi_collector.jsp?menu_id=M010300", 
          "icon" : "",
          "hasChild" : "false"
        }]
      },
      {
        "id" : "M020000",
        "txt" : "数据概览", 
        "link" : "data.jsp?menu_id=M020000", 
        "icon" : "",
        "hasChild" : "false"
      }
    ],

    sideBar : $('.navbar-nav.side-nav'),

    createMenuItem : function(obj){
      var t= this, html = '';

      if(obj.hasChild == 'false'){
       html +=
           '<li id="'+obj.id+'" data-hasparent="false">' +
           '  <a href="'+obj.link+'"><i class="fa fa-fw fa-edit"></i> '+ obj.txt +'</a>' +
           '</li>';
      }else{
        var arr = obj.children, childHtml = '';
        for (var i = 0; i <= arr.length -1; i++) {
          var item = arr[i];
          childHtml += '<li id="'+item.id+'" data-hasparent="true">' +
                       '  <a href="'+item.link+'">'+item.txt+'</a>' +
                       '</li>';
        };
        html +=
           '<li id="'+obj.id+'">' +
           '  <a href="javascript:;" data-toggle="collapse" data-target="#c'+obj.id+'"><i class="fa fa-fw fa-arrows-v"></i>'+obj.txt+'<i class="fa fa-fw fa-caret-down"></i></a>'+
                        '<ul id="c'+obj.id+'" class="collapse">'+
                            childHtml +
                        '</ul>'+
            '</li>';
      }

      t.sideBar.append(html);
    },

    render : function(){
      var t = this, data = t.data, html = '';

      for (var i = 0; i <= data.length - 1; i++) {
        t.createMenuItem(data[i]);
      };
    },

    setCurrentMenu : function(){
      var t = this, currentMenuId = t.currentMenuId, data = t.data;
      if(null == currentMenuId) return;
      var currentMenu = $('#'+ currentMenuId);
      currentMenu.addClass('active');  
      if(currentMenu.data('hasparent')){
        var parent = currentMenu.parents('li');
        parent.addClass('active');
        parent.find('a:first').click();
      }
    },

    init : function(){
      var t = this;
       //step0 verify
       if(!t.sideBar.length) return;

       //step1 : setData

       //step2 : render
       t.render();
       //step3:
       t.setCurrentMenu();
    }
  }

  Nav.init();
  
})(jQuery);