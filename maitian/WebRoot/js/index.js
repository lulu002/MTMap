(function($){ 

  var Nav = {
    data : [
      {
        "id" : "M030000",
        "txt" : "数据概览", 
        "link" : "./html/data.html", 
        "icon" : "",
        "hasChild" : "false"
      },
      {
        "id" : "M010000",
        "txt" : "麦田地图", 
        "link" : "", 
        "icon" : "",
        "hasChild" : "true",
        "children" : [{
          "id" : "M010100",
          "txt" : "麦粒", 
          "link" : "./html/grain.html?menu_id=M010100", 
          "icon" : "",
          "hasChild" : "false"
        },{
          "id" : "M010200",
          "txt" : "POI收集器", 
          "link" : "poi_collecter.html?menu_id=M010200", 
          "icon" : "",
          "hasChild" : "false"
        }]
      },
      {
        "id" : "M020000",
        "txt" : "其它菜单", 
        "link" : "", 
        "icon" : "",
        "hasChild" : "false"
      }
    ],

    sideBar : $('.navbar-nav.side-nav'),

    createMenuItem : function(obj){
      var t= this, html = '';

      if(obj.hasChild == 'false'){
       html +=
           '<li>' +
           '  <a href="'+obj.link+'"><i class="fa fa-fw fa-edit"></i> '+ obj.txt +'</a>' +
           '</li>';
      }else{
        var arr = obj.children, childHtml = '';
        for (var i = 0; i <= arr.length -1; i++) {
          var item = arr[i];
          childHtml += '<li>' +
                       '  <a href="'+item.link+'">'+item.txt+'</a>' +
                       '</li>';
        };
        html +=
           '<li>' +
           '  <a href="javascript:;" data-toggle="collapse" data-target="#'+obj.id+'"><i class="fa fa-fw fa-arrows-v"></i>'+obj.txt+'<i class="fa fa-fw fa-caret-down"></i></a>'+
                        '<ul id="'+obj.id+'" class="collapse">'+
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

    init : function(){
      var t = this;
       //step1 : setData

       //step2 : render
       t.render();
    }
  }

  Nav.init();
  
})(jQuery);