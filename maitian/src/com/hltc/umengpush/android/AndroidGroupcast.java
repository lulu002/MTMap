package com.hltc.umengpush.android;

import com.hltc.umengpush.AndroidNotification;


public class AndroidGroupcast extends AndroidNotification {
	public AndroidGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
