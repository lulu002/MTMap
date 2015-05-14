package com.hltc.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

import com.hltc.entity.User;

  
public class BeanUtil {  
	//将javabean转为map类型，然后返回一个map类型的值
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!StringUtils.equals(name, "class")) {
					params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
}  