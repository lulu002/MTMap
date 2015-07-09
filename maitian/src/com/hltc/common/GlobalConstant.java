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
	public static final String DEFAULT_POTRAIT = "http://maitianditu.oss-cn-hangzhou.aliyuncs.com/app/default_portrait.png";
	//默认的用户头像缩略图
	public static final String DEFAULT_POTRAIT_SMALL = "http://maitian.com";
	
	//UMeng
	public static final String UMENG_MESSAGE_IOS_APPKEY = "551cdcb6fd98c58835000b49";
	public static final String UMENG_MESSAGE_IOS_APP_MASTER_SECRET = "wqf6bxt3cyrdb6abfwiy0octzqef06m4";
	public static final String UMENG_MESSAGE_ANDROID_APPKEY = "5549747467e58ed626001c0a";
	public static final String UMENG_MESSAGE_ANDROID_APP_MASTER_SECRET = "wfxoibaylo1nxqvkzcovoiumy4wuaelp";
	
	public static final String ALIYUN_ACCESSKEY_ID = "wxGYeoOqFGIikopt";
	public static final String ALIYUN_ACCESSKEY_SECRET = "eQyS38ArhJo0fIotIuLoiz0FCx0J4N";
	public static final String ALIYUN_USER_ID = "1194627879869635";
	public static final String ALIYUN_USER_NAME = "2337223420@qq.com";
	public static final String OSS_BUCKET_NAME = "maitianditu";
	
	//容联云通讯
	public static final String CLOOPEN_PRO_URL = "app.cloopen.com";  //(生产) Rest URL
	public static final String CLOOPEN_DEV_URL = "sandboxapp.cloopen.com"; //(开发) Rest URL
	public static final String CLOOPEN_PORT = "8883";  
	public static final String CLOOPEN_ACCOUNT_SID = "aaf98f8949d126580149d1efb6ca0095";   //主账号
	public static final String CLOOPEN_AUTH_TOKEN = "1b975439287449bf89e3cc0e3c20d55b";  //主账号令牌
	public static final String CLOOPEN_MAITIN_APP_ID = "aaf98f894c9d994b014cb714c8d81290";  //麦田地图app id
	public static final String CLOOPEN_MAITIN_APP_TOKEN = "569f968bad8faa78e9f0270930bcf625"; //麦田地图app token
	public static final String CLOOPEN_SMS_ID = "21396";   //短信模板id  发送验证码
	
	//游客
	public static final Integer VISITOR_LIMIT_COUNT = 1000;   //单个ip的游客数量 
	public static final String ENV = "pro";
}
