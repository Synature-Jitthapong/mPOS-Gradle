package com.synature.mpos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// Start login activity
		Intent loginIntent = new Intent(context, LoginActivity.class);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(loginIntent);  
	}
}