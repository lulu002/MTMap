package com.hltc.service;

import java.util.Map;
/**
 *  消息推送Service
 * @author Hengbin Chen
  */
public interface IPushService {
	
	/**
	 * IOS自定义广播
	 * @param alias 自有的alias
	 * @param alert 在通知栏显示的内容
	 * @param keyvalue 需要发送的键值对
	 * @throws Exception
	 */
	public void sendIOSCustomizedcast(String alias, String alert, Map<String, String> keyvalue) throws Exception;
	
	public void sendAndroidBroadcast(String alias, String alert, Map keyvalue) throws Exception;
}
