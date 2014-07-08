package com.synature.mpos;

import com.synature.mpos.database.SyncMasterLog;
import com.synature.util.Logger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MasterDataService extends Service{

	public static final String TAG = MasterDataService.class.getSimpleName();
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.appendLog(getApplicationContext(), Utils.LOG_DIR, 
				Utils.LOG_FILE_NAME, 
				TAG + ": Service Start Command");
		SyncMasterLog sync = new SyncMasterLog(getApplicationContext());
		if(!sync.IsAlreadySync()){
			MPOSWebServiceClient.authenDevice(getApplicationContext(), new MPOSWebServiceClient.AuthenDeviceListener() {
				
				@Override
				public void onPre() {
				}
				
				@Override
				public void onPost() {
				}
				
				@Override
				public void onError(String msg) {
				}
				
				@Override
				public void onPost(final int shopId) {
					// load shop data
					MPOSWebServiceClient.loadShopData(getApplicationContext(), shopId, new ProgressListener(){
	
						@Override
						public void onPre() {
						}
	
						@Override
						public void onPost() {
							// load product datat
							MPOSWebServiceClient.loadProductData(getApplicationContext(), shopId, new ProgressListener(){
	
								@Override
								public void onPre() {
								}
	
								@Override
								public void onPost() {
									Logger.appendLog(getApplicationContext(), Utils.LOG_DIR, 
											Utils.LOG_FILE_NAME, 
											TAG + ": Service Success.");
									stopSelf();
								}
	
								@Override
								public void onError(String msg) {
								}
								
							});
						}
	
						@Override
						public void onError(String msg) {
						}
						
					});
				}
			});
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
