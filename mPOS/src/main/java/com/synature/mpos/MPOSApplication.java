package com.synature.mpos;

import android.app.Application;

public class MPOSApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		if(Utils.isEnableWintecCustomerDisplay(getApplicationContext()))
			showCustomerDisplay();
	}
	
	private void showCustomerDisplay(){
		WintecCustomerDisplay wd = new WintecCustomerDisplay(getApplicationContext());
		wd.displayWelcome();
	}
}
