package com.hltc.umengpush.ios;

import com.hltc.umengpush.IOSNotification;



public class IOSBroadcast extends IOSNotification {
	public IOSBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
