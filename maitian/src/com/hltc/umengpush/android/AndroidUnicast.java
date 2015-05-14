package com.hltc.umengpush.android;

import com.hltc.umengpush.AndroidNotification;

public class AndroidUnicast extends AndroidNotification {
	public AndroidUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}