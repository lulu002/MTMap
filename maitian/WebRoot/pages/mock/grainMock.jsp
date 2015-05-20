<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>麦粒数据测试</title>
</head>
<body>

<form action="http://localhost:8080/maitian/v1/grain/publish_batch.json" enctype="multipart/form-data" method="post">
	需要读取的最小行号：<input type="text" name="minLine" /><br />
	需要读取的最大行号：<input type="text" name="maxLine" /><br />
	城市编码：<input type="text" name="cityCode"/> <br/>
	上传文件: <input type="file" name="file1"/><br/> 
	<input type="submit" value="提交" />
</form>


<script src="http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js"></script>
<script src="http://maitianditu.oss-cn-hangzhou.aliyuncs.com/static/js/md5.js"></script>
<script>
(function($){
		
})(jQuery);
</script>
</body>

</html>