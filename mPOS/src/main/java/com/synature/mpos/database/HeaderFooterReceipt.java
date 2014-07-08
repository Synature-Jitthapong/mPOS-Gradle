package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.database.table.HeaderFooterReceiptTable;
import com.synature.pos.ShopData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class HeaderFooterReceipt extends MPOSDatabase{
	
	public static final int HEADER_LINE_TYPE = 0;
	public static final int FOOTER_LINE_TYPE = 1;

	public HeaderFooterReceipt(Context context) {
		super(context);
	}
	
	public List<ShopData.HeaderFooterReceipt> listHeaderFooter(int lineType){
		List<ShopData.HeaderFooterReceipt> hfLst = 
				new ArrayList<ShopData.HeaderFooterReceipt>();

		Cursor cursor = getReadableDatabase().query(HeaderFooterReceiptTable.TABLE_HEADER_FOOTER_RECEIPT, 
				new String[]{HeaderFooterReceiptTable.COLUMN_TEXT_IN_LINE, 
				HeaderFooterReceiptTable.COLUMN_LINE_TYPE, 
				HeaderFooterReceiptTable.COLUMN_LINE_ORDER}, 
				HeaderFooterReceiptTable.COLUMN_LINE_TYPE + "=?", 
				new String[]{String.valueOf(lineType)}, null, null, 
				HeaderFooterReceiptTable.COLUMN_LINE_ORDER);
		if(cursor.moveToFirst()){
			do{
				ShopData.HeaderFooterReceipt hf = new ShopData.HeaderFooterReceipt();
				hf.setTextInLine(cursor.getString(cursor.getColumnIndex(HeaderFooterReceiptTable.COLUMN_TEXT_IN_LINE)));
				hf.setLineType(cursor.getInt(cursor.getColumnIndex(HeaderFooterReceiptTable.COLUMN_LINE_TYPE)));
				hf.setLineOrder(cursor.getInt(cursor.getColumnIndex(HeaderFooterReceiptTable.COLUMN_LINE_ORDER)));
				hfLst.add(hf);
			}while(cursor.moveToNext());
		}
		cursor.close();

		return hfLst;
	}
	
	public void insertHeaderFooterReceipt(
		List<ShopData.HeaderFooterReceipt> headerFooterLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(HeaderFooterReceiptTable.TABLE_HEADER_FOOTER_RECEIPT, null, null);
			for(ShopData.HeaderFooterReceipt hf : headerFooterLst){
				ContentValues cv = new ContentValues();
				cv.put(HeaderFooterReceiptTable.COLUMN_TEXT_IN_LINE, hf.getTextInLine());
				cv.put(HeaderFooterReceiptTable.COLUMN_LINE_TYPE, hf.getLineType());
				cv.put(HeaderFooterReceiptTable.COLUMN_LINE_ORDER, hf.getLineOrder());
				getWritableDatabase().insertOrThrow(HeaderFooterReceiptTable.TABLE_HEADER_FOOTER_RECEIPT, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
}
