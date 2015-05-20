package com.hltc.common;

/**
 * 全局常量类
 * @author Hengbin Chen
  */
public class GlobalConstant {
	
	//发送语音验证码的时候，用户手机上显示的号码
	public static final String VERIFY_CODE_DISPLAY_NUMBER = "13767442857";
	//语音验证码重播的次数
	public static final String VERIFY_CODE_PLAY_COUNT = "3";
	//验证码的有效时间, 单位为分钟
	public static final Integer VERIFY_CODE_VALID_TIME = 10;
	//每个手机号码每天可以获得验证码的次数
	public static final Integer VERIFY_CDOE_MAX_COUNT_PERDAY = 10;
	//临时Token的有效时间, 单位为分钟
	public static final Integer TMP_TOKEN_VALID_TIME = 120;
	//用户token的有效时间, 单位为天
	public static final Integer TOKEN_VALID_TIME = 7;
	
	//默认的用户封面图
	public static final String DEFAULT_COVER_IMG = "http://maitian.com";
	//默认的用户头像
	public static final String DEFAULT_POTRAIT = "http://maitian.com";
	//默认的用户头像缩略图
	public static final String DEFAULT_POTRAIT_SMALL = "http://maitian.com";
	
	//UMeng
	public static final String UMENG_MESSAGE_IOS_APPKEY = "551cdcb6fd98c58835000b49";
	public static final String UMENG_MESSAGE_IOS_APP_MASTER_SECRET = "wqf6bxt3cyrdb6abfwiy0octzqef06m4";
	public static final String UMENG_MESSAGE_ANDROID_APPKEY = "5549747467e58ed626001c0a";
	public static final String UMENG_MESSAGE_ANDROID_APP_MASTER_SECRET = "wfxoibaylo1nxqvkzcovoiumy4wuaelp";
	
	public static final String ENV = "test";
}
