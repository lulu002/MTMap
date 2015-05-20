package com.hltc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LogUtil {
	private static Logger info = LoggerFactory.getLogger("InfoLogger");  
    private static Logger error = LoggerFactory.getLogger("ErrorLogger");  
      
    /** 
     * 一般情况记录到/logs/infoLog.txt 
     */  
    public static void info(String infomation){  
    	
        info.info(infomation);  
    }  
      
    /** 
     * 错误信息记录到/logs/errorLog.txt 
     */  
    public static void error(String infomation){
    	System.out.println(error.getName());
        error.error(infomation);  
    }  
    
} 