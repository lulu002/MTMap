package com.hltc.common;

import java.util.HashMap;

public class ErrorCode {
	//注册模块
	//手机号已被使用
	public static final String PHONE_HAS_REGISTERED = "10000";
	//验证错误
	public static final String VERIFY_CODE_WRONG = "10001";
	//验证码发送失败
	public static final String VERIFY_CODE_SEND_FAIL = "10002";
	//该手机号码今日获取验证码已超过限制
	public static final String VERIFY_CODE_COUNT_LIMIT = "10003";
	//验证码验证超时
	public static final String VERIFY_CODE_TIMEOUT = "10004";
		
	//用户创建失败
	public static final String USRE_CREATE_FAIL = "10005";
	//Token验证失败
	public static final String TOKEN_INVALID = "10006";
	//用户名或者密码错误
	public static final String ACCOUNT_AUTHORIZED_FAILED = "10007"; 
	//Token登录失败
	public static final String TOKEN_LOGIN_FAIL = "10008"; 
	//该手机号码未注册
	public static final String PHONE_NOT_REGISTERED = "10009";
	//该密码hash不存在
	public static final String PWD_HASH_NOT_EXIST = "10010";
	//用户不存在
	public static final String USER_NOT_EXIST = "10011";
	
	//麦粒不存在
	public static final String GRAIN_NOT_EXIST = "20001";
	//评论不存在
	public static final String COMMENT_NOT_EXIST = "20002";
	//评论删除失败
	public static final String COMMENT_DEL_FAILED = "20003";
	
	//请求失败
	public static final String REQUEST_FAIL = "10011";
	
	//参数错误
	public static final String PARAMS_ERROR = "e00001";
	
	public static final HashMap<Object, Object> errorMessage = new HashMap<Object, Object>();
	
	static{
		errorMessage.put(PHONE_HAS_REGISTERED, "手机号已被使用");
		errorMessage.put(VERIFY_CODE_WRONG, "验证错误");
		errorMessage.put(VERIFY_CODE_SEND_FAIL, "验证码发送失败");
		errorMessage.put(VERIFY_CODE_COUNT_LIMIT, "该手机号码今日获取验证码已超过限制");
		errorMessage.put(VERIFY_CODE_TIMEOUT, "验证超时");
		errorMessage.put(USRE_CREATE_FAIL, "用户创建失败");
		errorMessage.put(TOKEN_INVALID, "Token验证失败");
		errorMessage.put(ACCOUNT_AUTHORIZED_FAILED, "用户名或者密码错误");
		errorMessage.put(TOKEN_LOGIN_FAIL, "Token登录失败");
		errorMessage.put(PHONE_NOT_REGISTERED, "该手机号码未注册");
		errorMessage.put(PWD_HASH_NOT_EXIST, "该密码hash不存在");
		errorMessage.put(REQUEST_FAIL, "请求失败");
		errorMessage.put(USER_NOT_EXIST, "用户不存在");
		errorMessage.put(GRAIN_NOT_EXIST, "麦粒不存在");
		errorMessage.put(COMMENT_NOT_EXIST, "评论不存在");
		errorMessage.put(COMMENT_DEL_FAILED, "评论删除失败");
		
		
		errorMessage.put(PARAMS_ERROR, "参数错误");
		
		
		
	}
}
