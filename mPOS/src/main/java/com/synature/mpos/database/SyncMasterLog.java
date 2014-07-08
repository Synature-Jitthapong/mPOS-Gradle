package com.synature.mpos.database;

import com.synature.mpos.Utils;
import com.synature.mpos.database.table.SyncMasterLogTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SyncMasterLog extends MPOSDatabase{

	public static final int SYNC_SHOP_TYPE = 1;
	
	public static final int SYNC_PRODUCT_TYPE = 2;
	
	public static final int SYNC_STATUS_SUCCESS = 1;
	
	public static final int SYNC_STATUS_FAIL = 0;
	
	public SyncMasterLog(Context context) {
		super(context);
	}

	/**
	 * @return true if all sync_status = 1
	 */
	public boolean IsAlreadySync(){
		boolean isSync = true;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT " + SyncMasterLogTable.COLUMN_SYNC_TYPE + ","
				+ SyncMasterLogTable.COLUMN_SYNC_STATUS
				+ " FROM " + SyncMasterLogTable.TABLE_SYNC_MASTER
				+ " WHERE " + SyncMasterLogTable.COLUMN_SYNC_DATE + "=?",
				new String[]{
						String.valueOf(Utils.getDate().getTimeInMillis())
				});
		if(cursor.moveToFirst()){
			if(cursor.getInt(cursor.getColumnIndex(
					SyncMasterLogTable.COLUMN_SYNC_STATUS)) == SYNC_STATUS_FAIL)
				isSync = false;
		}else{
			isSync = false;
		}
		cursor.close();
		return isSync;
	}
	
	public void insertSyncLog(int type, int status){
		deleteSyncLog(type);
		ContentValues cv = new ContentValues();
		cv.put(SyncMasterLogTable.COLUMN_SYNC_TYPE, type);
		cv.put(SyncMasterLogTable.COLUMN_SYNC_STATUS, status);
		cv.put(SyncMasterLogTable.COLUMN_SYNC_DATE, Utils.getDate().getTimeInMillis());
		getWritableDatabase().insert(SyncMasterLogTable.TABLE_SYNC_MASTER, null, cv);
	}
	
	private void deleteSyncLog(int type){
		getWritableDatabase().delete(SyncMasterLogTable.TABLE_SYNC_MASTER, 
				SyncMasterLogTable.COLUMN_SYNC_TYPE + "=?", 
				new String[]{
					String.valueOf(type)
				});
	}
}
