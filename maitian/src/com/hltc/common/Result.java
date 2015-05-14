package com.hltc.common;

import java.util.HashMap;

/**
 * 接口直接返回的结果类
 * @author Hengbin Chen
  */
public class Result {
	
	public static final String SUCCESS = "success";
	
	public static HashMap fail(Object errorCode, String message){
		HashMap result = new HashMap();
		result.put("error_code", errorCode);
		
		if(null != message){
			result.put("error_message", message);
		}else{
			result.put("error_message", ErrorCode.errorMessage.get(errorCode));
		}
		
		return result;
	}
		
	public static HashMap fail(Object errorCode){
		return fail(errorCode, null);
	}
	
	public static HashMap success(Object dataObject){
		HashMap result = new HashMap();
		result.put("success", "true");
		if(null != dataObject){
			result.put("data", dataObject);
		}
		return result;
	}
	
	public static HashMap success(){
		return success(null);
	}
	
	public static void main(String[] args){
		HashMap data = new HashMap();
		data.put("token", "");
		System.out.println(null != data);
		System.out.println(new HashMap().put("data","data"));
	}
}
