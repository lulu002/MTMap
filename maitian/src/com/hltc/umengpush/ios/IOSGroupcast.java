package com.hltc.umengpush.ios;

import com.hltc.umengpush.IOSNotification;


public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
