(function($){
  var baseUrl = window.location.protocol + "//" + window.location.host +"/backyard/v1";
  Friend = {
    bind : function(){
      $('.double-way button').click(function(){
    	var data = {
    		"userIdA" : $('.double-way .userIdA').val(),
    		"userIdB" : $('.double-way .userIdB').val()
    	};
        var promise = $.ajax({
        	"url" : baseUrl + "/friend/add_friend.json",
        	"type" : "post",
        	"dataType" : "json",
        	"contentType" : "application/json",
        	"data" : JSON.stringify(data)
        });
        
        var tip = $('.double-way .tip-info');
        	
        promise.done(function(data){
        	if(data.success){
        		tip.html("ok");
        	}else{
        		tip.html("error_code:"+data.error_code+","+"error_message:"+data.error_message);
        	}
        });
        
        promise.fail(function(data){
        	tip.html("错误："+data.status);
        });
      });
    },
    init : function(){
      var t = this;
      t.bind();
    }
  };
  Friend.init();
})(jQuery);