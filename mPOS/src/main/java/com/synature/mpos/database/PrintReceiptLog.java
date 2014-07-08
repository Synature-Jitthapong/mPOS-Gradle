package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.Utils;
import com.synature.mpos.database.table.OrderTransactionTable;
import com.synature.mpos.database.table.PrintReceiptLogTable;
import com.synature.mpos.database.table.StaffTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class PrintReceiptLog extends MPOSDatabase{
	
	public static final int PRINT_NOT_SUCCESS = 0;
	public static final int PRINT_SUCCESS = 1;

	public PrintReceiptLog(Context context) {
		super(context);
	}
	
	/**
	 * @return List<PrintReceipt>
	 */
	public List<PrintReceipt> listPrintReceiptLog(){
		List<PrintReceipt> printLst = new ArrayList<PrintReceipt>();
		Cursor cursor = getReadableDatabase().query(PrintReceiptLogTable.TABLE_PRINT_LOG, 
				new String[]{
					OrderTransactionTable.COLUMN_TRANSACTION_ID,
					StaffTable.COLUMN_STAFF_ID,
					PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_ID,
					PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_TIME,
					PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_STATUS	
				}, PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_STATUS + "=?", 
				new String[]{
					String.valueOf(PRINT_NOT_SUCCESS)
				}, null, null, PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_TIME);
		if(cursor.moveToFirst()){
			do{
				PrintReceipt print = new PrintReceipt();
				print.setTransactionId(cursor.getInt(cursor.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
				print.setStaffId(cursor.getInt(cursor.getColumnIndex(StaffTable.COLUMN_STAFF_ID)));
				print.setPriceReceiptLogId(cursor.getInt(cursor.getColumnIndex(PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_ID)));
				print.setPrintReceiptLogTime(cursor.getString(cursor.getColumnIndex(PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_TIME)));
				print.setPrintReceiptLogStatus(cursor.getInt(cursor.getColumnIndex(PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_STATUS)));
				printLst.add(print);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return printLst;
	}
	
	/**
	 * @param printReceiptLogId
	 */
	public void deletePrintStatus(int printReceiptLogId){
		getWritableDatabase().delete(PrintReceiptLogTable.TABLE_PRINT_LOG, 
				PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_ID + "=?", 
				new String[]{String.valueOf(printReceiptLogId)}  );
	}
	
	/**
	 * @param printReceiptLogId
	 * @param status
	 */
	public void updatePrintStatus(int printReceiptLogId, int status){
		ContentValues cv = new ContentValues();
		cv.put(PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_STATUS, status);
		getWritableDatabase().update(PrintReceiptLogTable.TABLE_PRINT_LOG, cv, 
				PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_ID + "=?", 
				new String[]{
					String.valueOf(printReceiptLogId)
				}
		);
	}
	
	/**
	 * @param transactionId
	 * @param staffId
	 * @throws SQLException
	 */
	public void insertLog(int transactionId, int staffId) throws SQLException{
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_TRANSACTION_ID, transactionId);
		cv.put(StaffTable.COLUMN_STAFF_ID, staffId);
		cv.put(PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_TIME, Utils.getCalendar().getTimeInMillis());
		cv.put(PrintReceiptLogTable.COLUMN_PRINT_RECEIPT_LOG_STATUS, PRINT_NOT_SUCCESS);
		getWritableDatabase().insertOrThrow(PrintReceiptLogTable.TABLE_PRINT_LOG, null, cv);
	}
	
	public static class PrintReceipt{
		private int priceReceiptLogId;
		private int transactionId;
		private int computerId;
		private int staffId;
		private String printReceiptLogTime;
		private int printReceiptLogStatus;
		
		public int getStaffId() {
			return staffId;
		}
		public void setStaffId(int staffId) {
			this.staffId = staffId;
		}
		public String getPrintReceiptLogTime() {
			return printReceiptLogTime;
		}
		public void setPrintReceiptLogTime(String printReceiptLogTime) {
			this.printReceiptLogTime = printReceiptLogTime;
		}
		public int getPrintReceiptLogStatus() {
			return printReceiptLogStatus;
		}
		public void setPrintReceiptLogStatus(int printReceiptLogStatus) {
			this.printReceiptLogStatus = printReceiptLogStatus;
		}
		public int getPriceReceiptLogId() {
			return priceReceiptLogId;
		}
		public void setPriceReceiptLogId(int priceReceiptLogId) {
			this.priceReceiptLogId = priceReceiptLogId;
		}
		public int getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(int transactionId) {
			this.transactionId = transactionId;
		}
		public int getComputerId() {
			return computerId;
		}
		public void setComputerId(int computerId) {
			this.computerId = computerId;
		}
	}
}
