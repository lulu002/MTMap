package com.hltc.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.hltc.common.ErrorCode;
import com.hltc.common.Result;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.sf.json.JSONObject;

public class SecurityUtil {  
	//MD5加密
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	  public static String getHmacSha1Signature(String value, String key)
	    throws NoSuchAlgorithmException, InvalidKeyException
	  {
	    byte[] keyBytes = key.getBytes();
	    SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(signingKey);

	    byte[] rawHmac = mac.doFinal(value.getBytes());
	    return new String(Base64.encode(rawHmac, 0));
	  }
	  
	  public static String generateOSSToken(String accessKey, String secretKey, String content) {
		    String signature = null;

		    try {
		        signature = getHmacSha1Signature(content, secretKey);
		        signature = signature.trim();
		    } catch (Exception e) {
		    	e.printStackTrace();
//		        OSSLog.logD(e.toString());
		    }

		    return "OSS " + accessKey + ":" + signature; // 注意"OSS"和accessKey之间存在一个空格！！！
		}
	
	/**
	 * 参数验证, 只验证(1)参数名是否存在, (2)参数类型是否正确
	 * @param paramJson 客户端传过来的json参数
	 * @param paramInfo  对参数验证的信息，每个key-value对应着 参数名-参数类型
	 * @return
	 */
	public final static HashMap parametersValidate(JSONObject paramJson, Map<String, Class> paramInfo){
		String entryKey = null;
		Class entryValue = null;
		Object value = null;
		
		for (Map.Entry<String, Class> entry : paramInfo.entrySet()) {
			entryKey = entry.getKey();
			entryValue = entry.getValue();
		    value = paramJson.get(entryKey);
		    
		    if(null == value){
		    	return Result.fail(ErrorCode.PARAMS_ERROR,  "missing paramter:" + entryKey);
		    }
		    
		    if( !entryValue.isInstance(value)){
		    	return Result.fail(ErrorCode.PARAMS_ERROR, "wrong parameter type of " + entryKey ) ;
		    }
		}
		
		return Result.success();
	}
	
		public final static HashMap parametersValidate(JSONObject paramJson, String[] keys, Boolean required, Class clazz){
		Object o = null;
		for(String key: keys){
			o = paramJson.get(key);
			if(required && null == o){
				return Result.fail(ErrorCode.PARAMS_ERROR,  "missing parameter:" + key);
			}
			if(null != o && !clazz.isInstance(o)){
				return Result.fail(ErrorCode.PARAMS_ERROR, "wrong parameter type of " + key ) ;
			}
		}
		
		return Result.success();
	}
	
	public final static HashMap parametersScope(JSONObject paramJson, String key, Boolean required, String[] values){
		Object o = paramJson.get(key);
		if(null == o && required){
			return Result.fail(ErrorCode.PARAMS_ERROR, "missing parameter:"+key);
		}
		String os = o.toString();
		for(String value : values){
			if(value.equalsIgnoreCase(os)) return Result.success();
		}		
		return Result.fail(ErrorCode.PARAMS_ERROR, "wrong value of  parameter " + key);
	}
	
	public static void main(String[] args) {
		System.out.println(MD5("123456"));
	}
	
	
}  