package com.hltc.umengpush.ios;

import com.hltc.umengpush.IOSNotification;


public class IOSUnicast extends IOSNotification {
	public IOSUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}