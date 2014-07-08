package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.database.table.CreditCardTable;
import com.synature.pos.CreditCardType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CreditCard extends MPOSDatabase{
	
	public CreditCard(Context context) {
		super(context);
	}

	public String getCreditCardType(int typeId){
		String cardType = "";
		Cursor cursor = getReadableDatabase().query(CreditCardTable.TABLE_CREDIT_CARD_TYPE, 
				new String[]{ 
					CreditCardTable.COLUMN_CREDITCARD_TYPE_NAME
				}, 
				CreditCardTable.COLUMN_CREDITCARD_TYPE_ID + "=?", 
				new String[]{
					String.valueOf(typeId)
				}, null, null, null);
		if(cursor.moveToFirst()){
			cardType = cursor.getString(
					cursor.getColumnIndex(CreditCardTable.COLUMN_CREDITCARD_TYPE_NAME));
		}
		cursor.close();
		return cardType;
	}
	
	public List<CreditCardType> listAllCreditCardType(){
		List<CreditCardType> creditCardLst = 
				new ArrayList<CreditCardType>();
		Cursor cursor = getReadableDatabase().query(CreditCardTable.TABLE_CREDIT_CARD_TYPE, 
				new String[]{CreditCardTable.COLUMN_CREDITCARD_TYPE_ID, 
				CreditCardTable.COLUMN_CREDITCARD_TYPE_NAME}, 
				null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				CreditCardType credit = new CreditCardType(
						cursor.getInt(cursor.getColumnIndex(CreditCardTable.COLUMN_CREDITCARD_TYPE_ID)),
						cursor.getString(cursor.getColumnIndex(CreditCardTable.COLUMN_CREDITCARD_TYPE_NAME)));
				creditCardLst.add(credit);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return creditCardLst;
	}
	
	public void insertCreditCardType(List<CreditCardType> creditCardLst){
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(CreditCardTable.TABLE_CREDIT_CARD_TYPE, null, null);
			for(CreditCardType credit : creditCardLst){
				ContentValues cv = new ContentValues();
				cv.put(CreditCardTable.COLUMN_CREDITCARD_TYPE_ID, credit.getCreditCardTypeId());
				cv.put(CreditCardTable.COLUMN_CREDITCARD_TYPE_NAME, credit.getCreditCardTypeName());
				getWritableDatabase().insertOrThrow(CreditCardTable.TABLE_CREDIT_CARD_TYPE, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
}
