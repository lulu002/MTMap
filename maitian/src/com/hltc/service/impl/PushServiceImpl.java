package com.hltc.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hltc.common.GlobalConstant;
import com.hltc.service.IPushService;
import com.hltc.umengpush.android.AndroidBroadcast;
import com.hltc.umengpush.android.AndroidCustomizedcast;
import com.hltc.umengpush.android.AndroidUnicast;
import com.hltc.umengpush.ios.IOSCustomizedcast;



@Service("pushService")
public class PushServiceImpl implements IPushService{
	private String IOSAppkey = null;
	private String IOSAppMasterSecret = null;
	private String AndroidAppkey = null;
	private String AndroidAppMasterSecret = null;
	
	public PushServiceImpl(){
		try {
			IOSAppkey = GlobalConstant.UMENG_MESSAGE_IOS_APPKEY;
			IOSAppMasterSecret = GlobalConstant.UMENG_MESSAGE_IOS_APP_MASTER_SECRET;
			AndroidAppkey = GlobalConstant.UMENG_MESSAGE_ANDROID_APPKEY;
			AndroidAppMasterSecret = GlobalConstant.UMENG_MESSAGE_ANDROID_APP_MASTER_SECRET;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private String getTimestamp(){
		return Integer.toString((int)(System.currentTimeMillis() / 1000));
	}

	public void sendIOSCustomizedcast(String alias, String alert, Map<String, String> keyvalue) throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast();
		customizedcast.setAppMasterSecret(IOSAppMasterSecret);
		customizedcast.setPredefinedKeyValue("appkey", this.IOSAppkey);
		customizedcast.setPredefinedKeyValue("timestamp", this.getTimestamp());
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setPredefinedKeyValue("alias", alias);
		// TODO Set your alias_type here
		customizedcast.setPredefinedKeyValue("alias_type", "TYPE_MTMAP");
		customizedcast.setPredefinedKeyValue("alert", alert);
		customizedcast.setPredefinedKeyValue("badge", 0);
		customizedcast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		customizedcast.setPredefinedKeyValue("production_mode", "false");
		for(Map.Entry<String, String> entry : keyvalue.entrySet()){    
			customizedcast.setPredefinedKeyValue(entry.getKey(), entry.getValue());
		}  
		customizedcast.send();
	}
	
	public void sendAndroidBroadcast(String alias, String alert, Map keyvalue) throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(AndroidAppMasterSecret);
		broadcast.setPredefinedKeyValue("appkey", this.AndroidAppkey);
		broadcast.setPredefinedKeyValue("timestamp", this.getTimestamp());
		broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
		broadcast.setPredefinedKeyValue("title",  "这是提示");
		broadcast.setPredefinedKeyValue("text",   "这是提示内容");
		broadcast.setPredefinedKeyValue("after_open", "go_app");
		broadcast.setPredefinedKeyValue("display_type", "notification");
		broadcast.setPredefinedKeyValue("alias", alias);
		broadcast.setPredefinedKeyValue("alias_type", "TYPE_MTMAP");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		broadcast.setPredefinedKeyValue("production_mode", "false");
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		broadcast.send();
	}
	
	public void sendAndroidCustomizedcast(String alias, String alert, Map<String,String> keyvalue) throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
		customizedcast.setAppMasterSecret(AndroidAppMasterSecret);
		customizedcast.setPredefinedKeyValue("appkey", this.AndroidAppkey);
		customizedcast.setPredefinedKeyValue("timestamp", this.getTimestamp());
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setPredefinedKeyValue("alias", alias);
		// TODO Set your alias_type here
		customizedcast.setPredefinedKeyValue("alias_type", "TYPE_MTMAP");
		customizedcast.setPredefinedKeyValue("ticker", "Android customizedcast ticker");
		customizedcast.setPredefinedKeyValue("builder_id", 1);
		customizedcast.setPredefinedKeyValue("title",  "中文的title");
		customizedcast.setPredefinedKeyValue("text",   "Android customizedcast text");
		customizedcast.setPredefinedKeyValue("after_open", "go_app");
		customizedcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		customizedcast.setPredefinedKeyValue("production_mode", "true");
		
		for(Map.Entry<String, String> entry : keyvalue.entrySet()){
			customizedcast.setExtraField(entry.getKey(), entry.getValue());
		}
		customizedcast.send();
	}
	
	public static void main(String[] args) {
		PushServiceImpl service = new PushServiceImpl();
		Map<String, Object> map = new HashMap();
		map.put("fromId", "sdfsdfds");
		try {
			service.sendAndroidCustomizedcast("700002", "nihao",null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
