<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common.jsp" %>    
<!DOCTYPE html>
<html lang="zh">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>麦田的后花园</title>
    <link rel="shortcut icon" href="images/favicon.ico"/>
    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" >
    <link  href="${path}/css/global.css?v=${rand}" rel="stylesheet" >
    <link  href="css/grain.css" rel="stylesheet" >

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
		<%@include file="nav.jsp" %>

        <div id="page-wrapper" class="grain">

            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <!--
                        <h1 class="page-header">
                            麦田地图 <small>麦粒</small>
                        </h1>
                        -->
                        <ol class="breadcrumb" style="margin-top:20px">
                            <li class="active">
                                <i class="fa fa-dashboard"></i> 麦粒列表
                            </li>
                        </ol>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-lg-12">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped" id="grainList">
                                <thead>
                                    <tr>
                                        <th style="min-width:75px;">麦粒类别</th>
                                        <th>场所</th>
                                        <th>用户</th>
                                        <th style="min-width:45px;">公开</th>
                                        <th>评论</th>
                                        <th style="min-width:45px;">操作</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                            <nav>
                              <ul class="pagination" style="margin:0px;"></ul>
                              <div class="input-group search-by-page">
                                  <input type="text" class="form-control" placeholder="1" id="pageNum">
                                  <span class="input-group-btn">
                                    <button class="btn btn-default" type="button" onclick="Grain.queryByIpt();">Go!</button>
                                  </span>
                              </div>
                            </nav>
                        </div>
                    </div>
                </div>
                <!-- /.row -->
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="${path}/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${path}/js/bootstrap.min.js"></script>
    <script src="${path}/js/global.js"></script>
    <script src="${path}/js/grain.js?v=${rand}"></script>
</body>

</html>