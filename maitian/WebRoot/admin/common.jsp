<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%    
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/admin";
	request.setAttribute("path",path);
  Double rand = Math.random();
  request.setAttribute("rand",rand);
%>
