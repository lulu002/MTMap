package com.hltc.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Parameter {
	/**
	 * 参数的名字
	 * @return
	 */
	String name();
	
	/**
	 * 参数的类型
	 * @return
	 */
	Class type();
}
