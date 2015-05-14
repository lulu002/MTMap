<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js"></script>
	
  </head>
  
  <body>
  	<div class="testContainer"></div>
    <input type="button" value="新增一个API测试" onclick="addNewAPITest();"/>
    
    <script>
    	$.ajaxSetup({  
	        //contentType : 'application/json;charset=utf-8;'  
	    });  
	    
    	function addNewAPITest(){
    		var container = $('.testContainer');
    		var html = '<div class="testCase">' +
					   '测试地址:<input class="testUrl" style="width:400px;"/> <br/>' +
		          	   '请求方式：' +
		    			'<select class="requestMethod">' +
							'<option value="GET">GET</option> ' +
							'<option value="POST">POST</option>'+   
		    			'</select>'+
		    			'<br/>'+
		         		'请求参数：'+
		    			'<textarea class="params" style="width:600px;height:100px;">'+
					    '</textarea>'+
		    			'<br/>'+
		         		'返回结果:'+
		    			'<textarea class="result" style="width:600px;height:150px;">'+
					    '</textarea>'+
		    			'<br/>'+
		    			'<input type="button" class="sendBtn" value="发送" onclick="sendRequest(this);"/>'+
	    				'</div>';
	    				
	    	container.append(html);
    	};
    	
    	
    	function sendRequest(obj){
    		var parent = $(obj).parent();
    		var url = parent.find('.testUrl').val(),
	  			method = parent.find('.requestMethod').val(),
	  			params = JSON.parse(parent.find('.params').val());
	  		var data;
	  			
	  			
	  		if(method == "POST"){
	  			data = JSON.stringify(params);
	  		}else{
	  			data = params;
	  		}
	  			
	  		$.ajax({
	  			url : url,
	  			type : method,
	  			dataType : "json",
	  			contentType: "application/json",
	  			data : data,
	  			success : function(data){
	  				parent.find(".result").val(JSON.stringify(data));
	  			},
	  			error: function(data){
	  				parent.find(".result").val(JSON.stringify(data));
	  			}
	  		});
    	}
	  </script>
  </body>
  

</html>
