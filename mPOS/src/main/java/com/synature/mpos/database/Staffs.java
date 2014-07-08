package com.synature.mpos.database;

import java.util.List;

import com.synature.mpos.database.table.StaffTable;
import com.synature.pos.ShopData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class Staffs extends MPOSDatabase{
	
	public Staffs(Context context) {
		super(context);
	}

	public int countStaffs(){
		int totalStaff = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT COUNT(*) FROM " + StaffTable.TABLE_STAFF, 
				null);
		if(cursor.moveToFirst()){
			totalStaff = cursor.getInt(0);
		}
		return totalStaff;
	}
	
	/**
	 * @param staffId
	 * @return ShopData.Staff
	 */
	public ShopData.Staff getStaff(int staffId){
		ShopData.Staff s = null;
		Cursor cursor = getReadableDatabase().query(StaffTable.TABLE_STAFF, 
				new String[]{StaffTable.COLUMN_STAFF_CODE, 
				StaffTable.COLUMN_STAFF_NAME}, 
				StaffTable.COLUMN_STAFF_ID + "=?", 
				new String[]{String.valueOf(staffId)}, null, null, null);
		if(cursor.moveToFirst()){
			s = new ShopData.Staff();
			s.setStaffCode(cursor.getString(cursor.getColumnIndex(StaffTable.COLUMN_STAFF_CODE)));
			s.setStaffName(cursor.getString(cursor.getColumnIndex(StaffTable.COLUMN_STAFF_NAME)));
		}
		cursor.close();
		return s;
	}
	
	/**
	 * @param staffLst
	 * @throws SQLException
	 */
	public void insertStaff(List<ShopData.Staff> staffLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(StaffTable.TABLE_STAFF, null, null);
			for(ShopData.Staff staff : staffLst){
				ContentValues cv = new ContentValues();
				cv.put(StaffTable.COLUMN_STAFF_ID, staff.getStaffID());
				cv.put(StaffTable.COLUMN_STAFF_CODE, staff.getStaffCode());
				cv.put(StaffTable.COLUMN_STAFF_NAME, staff.getStaffName());
				cv.put(StaffTable.COLUMN_STAFF_PASS, staff.getStaffPassword());
				getWritableDatabase().insertOrThrow(StaffTable.TABLE_STAFF, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally{
			getWritableDatabase().endTransaction();
		}
	}
}
