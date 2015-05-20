<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户数据测试</title>
</head>
<body>

第一步：注册-》发送验证码:10000000000 - 10000000099 <input type="button" value="开始发送" onclick="Test.register.sendVerifyCode();"/>
<br/>
第二步：注册-》校验验证码:10000000000 - 10000000099 <input type="button" value="开始发送" onclick="Test.register.verify();"/>
<br/>
第三步：注册-》创建用户:10000000000 - 10000000099 <input type="button" value="开始发送" onclick="Test.register.createUser();"/>



<script src="http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js"></script>
<script src="http://maitianditu.oss-cn-hangzhou.aliyuncs.com/static/js/md5.js"></script>

<script>
(function($){
	var urlPrefix = "http://localhost:8080/maitian/v1/";
	Test = {register:{}};
	Test.register.sendVerifyCode = function(){
		var suffix = "",i=0;
		var sendRequest = function(){
		    console.log("第" + i + "个请求");
		    if(i < 10){
			  suffix = "00" + i;
			}else if(i < 100){
			  suffix = "0" + i;
			}
			
			$.ajax({
	  			url : urlPrefix + "user/register/verify_code.json",
	  			type : "GET",
	  			dataType : "json",
	  			contentType: "application/json",
	  			data : {"phone_number": "10000000"+suffix},
	  			success : function(data){
	  			    console.log(JSON.stringify(data));
	  			    i++;
	  			    if(i<100){
	  			      sendRequest();
	  			    }else{
	  			      console.log('100个验证码发送完毕');
	  			    }
	  			},
	  			error: function(data){
	  				console.log(error);
	  			}
	  		});
		};
		sendRequest();
	}
	
	Test.register.verify = function(){
		var suffix = "", i = 0;
		var sendRequest = function(){
			console.log("第" + i + "个请求");
		    if(i < 10){
			  suffix = "00" + i;
			}else if(i < 100){
			  suffix = "0" + i;
			}
			
			$.ajax({
	  			url : urlPrefix + "user/register/verify.json",
	  			type : "GET",
	  			dataType : "json",
	  			contentType: "application/json",
	  			data : {"phone_number": "10000000"+suffix, "verify_code":"8888"},
	  			success : function(data){
	  			    console.log(JSON.stringify(data));
	  			    i++;
	  			    if(i<100){
	  			      sendRequest();
	  			    }else{
	  			      console.log('100个验证发送完毕');
	  			    }
	  			},
	  			error: function(data){
	  				console.log(error);
	  			}
	  		});
		};
		sendRequest();
	}
	
	Test.register.createUser = function(){
		var suffix = "", i = 0;
		var sendRequest = function(){
			console.log("第" + i + "个请求");
		    if(i < 10){
			  suffix = "00" + i;
			}else if(i < 100){
			  suffix = "0" + i;
			}
			
			$.ajax({
	  			url : urlPrefix + "user/register/new_user.json",
	  			type : "POST",
	  			dataType : "json",
	  			contentType: "application/json",
	  			data : JSON.stringify({"phone_number": "10000000"+suffix, "pwd":faultylabs.MD5("123456"), "tmp_token":"8888"}),
	  			success : function(data){
	  			    console.log(JSON.stringify(data));
	  			    i++;
	  			    if(i<100){
	  			      sendRequest();
	  			    }else{
	  			      console.log('100个验证发送完毕');
	  			    }
	  			},
	  			error: function(data){
	  				console.log(error);
	  			}
	  		});
		};
		sendRequest();		
	}
	
	
})(jQuery);

</script>
</body>

</html>