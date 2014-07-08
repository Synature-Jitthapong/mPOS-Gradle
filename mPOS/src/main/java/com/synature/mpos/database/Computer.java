package com.synature.mpos.database;

import java.util.List;

import com.synature.mpos.database.table.ComputerTable;
import com.synature.pos.ShopData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class Computer extends MPOSDatabase{

	public Computer(Context context) {
		super(context);
	}

	public int getComputerId(){
		return getComputerProperty().getComputerID();
	}
	
	public boolean checkIsMainComputer(int computerId){
		boolean isMainComputer = false;
		if(getComputerProperty().getIsMainComputer() != 0)
			isMainComputer = true;
		return isMainComputer;
	}
	
	public ShopData.ComputerProperty getComputerProperty() {
		ShopData.ComputerProperty computer = 
				new ShopData.ComputerProperty();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + ComputerTable.TABLE_COMPUTER, null);
		if (cursor.moveToFirst()) {
			computer.setComputerID(cursor.getInt(cursor.getColumnIndex(ComputerTable.COLUMN_COMPUTER_ID)));
			computer.setComputerName(cursor.getString(cursor.getColumnIndex(ComputerTable.COLUMN_COMPUTER_NAME)));
			computer.setDeviceCode(cursor.getString(cursor.getColumnIndex(ComputerTable.COLUMN_DEVICE_CODE)));
			computer.setIsMainComputer(cursor.getInt(cursor.getColumnIndex(ComputerTable.COLUMN_IS_MAIN_COMPUTER)));
			computer.setRegistrationNumber(cursor.getString(cursor.getColumnIndex(ComputerTable.COLUMN_REGISTER_NUMBER)));
			cursor.moveToNext();
		}
		cursor.close();
		return computer;
	}

	public void insertComputer(List<ShopData.ComputerProperty> compLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(ComputerTable.TABLE_COMPUTER, null, null);
			for (ShopData.ComputerProperty comp : compLst) {
				ContentValues cv = new ContentValues();
				cv.put(ComputerTable.COLUMN_COMPUTER_ID, comp.getComputerID());
				cv.put(ComputerTable.COLUMN_COMPUTER_NAME, comp.getComputerName());
				cv.put(ComputerTable.COLUMN_DEVICE_CODE, comp.getDeviceCode());
				cv.put(ComputerTable.COLUMN_REGISTER_NUMBER, comp.getRegistrationNumber());
				cv.put(ComputerTable.COLUMN_IS_MAIN_COMPUTER, comp.getIsMainComputer());
				getWritableDatabase().insertOrThrow(ComputerTable.TABLE_COMPUTER, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
}
