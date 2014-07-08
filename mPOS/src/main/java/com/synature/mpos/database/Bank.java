package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.database.table.BankTable;
import com.synature.pos.BankName;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Bank extends MPOSDatabase{

	public Bank(Context context) {
		super(context);
	}
	
	public List<BankName> listAllBank(){
		List<BankName> bankLst = 
				new ArrayList<BankName>();
		Cursor cursor = getReadableDatabase().query(BankTable.TABLE_BANK, 
				new String[]{BankTable.COLUMN_BANK_ID, BankTable.COLUMN_BANK_NAME}, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				BankName bank = new BankName(
						cursor.getInt(cursor.getColumnIndex(BankTable.COLUMN_BANK_ID)),
						cursor.getString(cursor.getColumnIndex(BankTable.COLUMN_BANK_NAME)));
				bankLst.add(bank);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return bankLst;
	}
	
	public void insertBank(List<BankName> bankLst){
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(BankTable.TABLE_BANK, null, null);
			for(BankName bank : bankLst){
				ContentValues cv = new ContentValues();
				cv.put(BankTable.COLUMN_BANK_ID, bank.getBankNameId());
				cv.put(BankTable.COLUMN_BANK_NAME, bank.getBankName());
				getWritableDatabase().insertOrThrow(BankTable.TABLE_BANK, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally{
			getWritableDatabase().endTransaction();
		}
	}
}
