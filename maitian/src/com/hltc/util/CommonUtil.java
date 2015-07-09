package com.hltc.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

import com.hltc.entity.User;

  
public class CommonUtil {  
	public static Long[] getDistinct(List<Long> nums) {
        List<Long> list = new java.util.ArrayList<Long>();
        for (Long num : nums) {
            if (!list.contains(num)) {//如果list数组不包括num[i]中的值的话，就返回true。
                list.add(num); //在list数组中加入num[i]的值。已经过滤过。
            }
        }
        return list.toArray(new Long[0]);
	}
	
	public static List<String> getDistinct(List<String> strs){
		List<String> result = new ArrayList<String>();
		for(String str : strs){
			if(!result.contains(str)){
				result.add(str);
			}
		}
		return result;
	}
	
	/** 
	 * ip地址转成整数. 
	 * @param ip 
	 * @return 
	 */  
	public static long ip2long(String ip) {  
	    String[] ips = ip.split("[.]");  
	    long num =  16777216L*Long.parseLong(ips[0]) + 65536L*Long.parseLong(ips[1]) + 256*Long.parseLong(ips[2]) + Long.parseLong(ips[3]);  
	    return num;  
	}  
	  
	/** 
	 * 整数转成ip地址. 
	 * @param ipLong 
	 * @return 
	 */  
	public static String long2ip(long ipLong) {  
	    //long ipLong = 1037591503;  
	    long mask[] = {0x000000FF,0x0000FF00,0x00FF0000,0xFF000000};  
	    long num = 0;  
	    StringBuffer ipInfo = new StringBuffer();  
	    for(int i=0;i<4;i++){  
	        num = (ipLong & mask[i])>>(i*8);  
	        if(i>0) ipInfo.insert(0,".");  
	        ipInfo.insert(0,Long.toString(num,10));  
	    }  
	    return ipInfo.toString();  
	}
	
	 /** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     * @param request 
     * @return 
     * @throws IOException 
     */  
	public static String getRemortIP(HttpServletRequest request) {  
		 String ip = request.getHeader("X-Forwarded-For");
		  if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
		              //多次反向代理后会有多个ip值，第一个ip才是真实ip
		                 int index = ip.indexOf(",");
		                 if(index != -1){
		                     return ip.substring(0,index);
		                 }else{
		                     return ip;
		                }
		            }
		            ip = request.getHeader("X-Real-IP");
		            if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
		                return ip;
		            }
		            return request.getRemoteAddr();
	}  
}  