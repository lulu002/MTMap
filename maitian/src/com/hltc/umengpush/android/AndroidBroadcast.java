package com.hltc.umengpush.android;

import com.hltc.umengpush.AndroidNotification;

public class AndroidBroadcast extends AndroidNotification {
	public AndroidBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
