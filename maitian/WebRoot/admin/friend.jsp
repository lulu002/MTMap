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

    <!-- Morris Charts CSS -->
    <link href="css/plugins/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" >
    <link  href="${path}/css/global.css?v=${rand}" rel="stylesheet" >

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
                                <i class="fa fa-dashboard"></i> 朋友模块
                            </li>
                        </ol>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="panel panel-primary one-way">
                      <div class="panel-heading">
                        <h3 class="panel-title" id="panel-title">单向添加朋友(A加B)<a class="anchorjs-link" href="#panel-title"><span class="anchorjs-icon"></span></a></h3>
                      </div>
                      <div class="panel-body">
                        <div class="input-group">
                          <span class="input-group-addon" id="sizing-addon2">A</span>
                          <input type="text" class="form-control" placeholder="用户A的userId" aria-describedby="sizing-addon2">
                        </div>
                        <div class="mt20"></div>
                        <div class="input-group">
                          <span class="input-group-addon" id="sizing-addon2">B</span>
                          <input type="text" class="form-control" placeholder="用户B的userId" aria-describedby="sizing-addon2">
                        </div>
                        <div class="mt20"></div>
                        <button type="button" class="btn btn-default fr mr20">添加</button>
                        <div class="alert alert-danger fl p6 mb0 error-info hidden" role="alert">
                        </div>
                      </div>
                    </div>
                    <div class="panel panel-primary double-way">
                      <div class="panel-heading">
                        <h3 class="panel-title" id="panel-title">双向添加朋友(A和B互添)<a class="anchorjs-link" href="#panel-title"><span class="anchorjs-icon"></span></a></h3>
                      </div>
                      <div class="panel-body">
                        <div class="input-group">
                          <span class="input-group-addon" id="sizing-addon2">A</span>
                          <input type="text" class="form-control" placeholder="用户A的userId" aria-describedby="sizing-addon2">
                        </div>
                        <div class="mt20"></div>
                        <div class="input-group">
                          <span class="input-group-addon" id="sizing-addon2">B</span>
                          <input type="text" class="form-control" placeholder="用户B的userId" aria-describedby="sizing-addon2">
                        </div>
                        <div class="mt20"></div>
                        <button type="button" class="btn btn-default fr mr20">添加</button>
                        <div class="alert alert-danger fl p6 mb0 error-info hidden" role="alert">
                        </div>
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
    <script src="${path}/js/global.js?v=${rand}"></script>
    <script src="${path}/js/friend.js?v=${rand}"></script>

</body>

</html>