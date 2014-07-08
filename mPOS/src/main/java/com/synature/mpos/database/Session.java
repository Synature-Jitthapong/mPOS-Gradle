package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.synature.mpos.Utils;
import com.synature.mpos.database.table.ComputerTable;
import com.synature.mpos.database.table.OrderTransactionTable;
import com.synature.mpos.database.table.SessionDetailTable;
import com.synature.mpos.database.table.SessionTable;
import com.synature.mpos.database.table.ShopTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class Session extends MPOSDatabase{
	
	public static final int NOT_ENDDAY_STATUS = 0;
	public static final int ALREADY_ENDDAY_STATUS = 1;
	
	public Session(Context context) {
		super(context);
	}

	/**
	 * @return List<String> sessionDate 
	 */
	public List<String> listSessionEnddayNotSend(){
		List<String> sessLst = new ArrayList<String>();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT " + SessionTable.COLUMN_SESS_DATE
				+ " FROM " + SessionDetailTable.TABLE_SESSION_ENDDAY_DETAIL
				+ " WHERE " + COLUMN_SEND_STATUS + "=?", 
				new String[]{
					String.valueOf(NOT_SEND)
				}
		);
		if(cursor.moveToFirst()){
			do{
				String sessDate = cursor.getString(0);
				sessLst.add(sessDate);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return sessLst;
	}
	
	/**
	 * @param currentSaleDate
	 * @param closeStaffId
	 */
	public void autoEnddaySession(String currentSaleDate, int closeStaffId){
		ContentValues cv = new ContentValues();
		cv.put(SessionTable.COLUMN_IS_ENDDAY, ALREADY_ENDDAY_STATUS);
		cv.put(OrderTransactionTable.COLUMN_CLOSE_STAFF, closeStaffId);
		cv.put(SessionTable.COLUMN_CLOSE_DATE, Utils.getCalendar().getTimeInMillis());
		
		getWritableDatabase().update(SessionDetailTable.TABLE_SESSION_ENDDAY_DETAIL, cv, 
				SessionTable.COLUMN_IS_ENDDAY + "=? " +
				" AND " + SessionTable.COLUMN_SESS_DATE + "<?", 
				new String[]{
				String.valueOf(NOT_ENDDAY_STATUS),
				currentSaleDate
		});
	}
	
	/**
	 * @param sessionId
	 * @param closeStaffId
	 * @param closeAmount
	 * @param isEndday
	 * @return row affected
	 */
	public int closeShift(int sessionId, int closeStaffId,
			double closeAmount, boolean isEndday) {
		return closeSession(sessionId, closeStaffId,
				closeAmount, isEndday);
	}

	/**
	 * @param sessionId
	 * @return session date specific by sessionId
	 */
	public String getSessionDate(int sessionId){
		String sessionDate = "";
		Cursor cursor = getReadableDatabase().query(SessionTable.TABLE_SESSION, 
				new String[]{SessionTable.COLUMN_SESS_DATE}, 
				SessionTable.COLUMN_SESS_ID + "=?", 
				new String[]{
					String.valueOf(sessionId)
				}, null, null, null);
		
		if(cursor.moveToFirst()){
			sessionDate = cursor.getString(0);
		}
		cursor.close();
		return sessionDate;
	}
	
	/**
	 * @return session date
	 */
	public String getSessionDate(){
		String sessionDate = "";
		Cursor cursor = getReadableDatabase().query(SessionTable.TABLE_SESSION, 
				new String[]{SessionTable.COLUMN_SESS_DATE}, 
				null, null, null, null, SessionTable.COLUMN_SESS_DATE + " DESC ", "1");
		
		if(cursor.moveToFirst()){
			sessionDate = cursor.getString(0);
		}
		cursor.close();
		return sessionDate;
	}
	
	/**
	 * @param sessionId
	 * @return row affected
	 */
	public int deleteSession(int sessionId){
		return getWritableDatabase().delete(SessionTable.TABLE_SESSION, 
				SessionTable.COLUMN_SESS_ID + "=?", 
				new String[]{String.valueOf(sessionId)});
	}
	
	/**
	 * @return max sessionId
	 */
	public int getMaxSessionId() {
		int sessionId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT MAX(" + SessionTable.COLUMN_SESS_ID + ") " + 
				" FROM " + SessionTable.TABLE_SESSION, null);
		if (cursor.moveToFirst()) {
			sessionId = cursor.getInt(0);
		}
		cursor.close();
		return sessionId + 1;
	}

	public int openSession(Calendar date, int shopId, int computerId, 
			int openStaffId, double openAmount){
		int sessionId = getMaxSessionId();
		Calendar dateTime = Utils.getCalendar();
		ContentValues cv = new ContentValues();
		cv.put(SessionTable.COLUMN_SESS_ID, sessionId);
		cv.put(ComputerTable.COLUMN_COMPUTER_ID, computerId);
		cv.put(ShopTable.COLUMN_SHOP_ID, shopId);
		cv.put(SessionTable.COLUMN_SESS_DATE, date.getTimeInMillis());
		cv.put(SessionTable.COLUMN_OPEN_DATE, dateTime.getTimeInMillis());
		cv.put(OrderTransactionTable.COLUMN_OPEN_STAFF, openStaffId);
		cv.put(SessionTable.COLUMN_OPEN_AMOUNT, openAmount);
		cv.put(SessionTable.COLUMN_IS_ENDDAY, 0);
		try {
			getWritableDatabase().insertOrThrow(SessionTable.TABLE_SESSION, null, cv);
		} catch (Exception e) {
			sessionId = 0;
			e.printStackTrace();
		}
		return sessionId;
	}

	/**
	 * Update set send status
	 * @param sessionDate
	 * @param status
	 * @return rows affected
	 */
	public int updateSessionEnddayDetail(String sessionDate, int status){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_SEND_STATUS, status);
		return getWritableDatabase().update(SessionDetailTable.TABLE_SESSION_ENDDAY_DETAIL,
				cv, SessionTable.COLUMN_SESS_DATE + "=?", new String[]{sessionDate});
	}
	
	/**
	 * @param sessionDate
	 * @param totalQtyReceipt
	 * @param totalAmountReceipt
	 * @return the row ID of newly insert
	 * @throws SQLException
	 */
	public long addSessionEnddayDetail(String sessionDate, double totalQtyReceipt, 
			double totalAmountReceipt) throws SQLException {
		Calendar dateTime = Utils.getCalendar();
		ContentValues cv = new ContentValues();
		cv.put(SessionTable.COLUMN_SESS_DATE, sessionDate);
		cv.put(SessionDetailTable.COLUMN_ENDDAY_DATE, dateTime.getTimeInMillis());
		cv.put(SessionDetailTable.COLUMN_TOTAL_QTY_RECEIPT, totalQtyReceipt);
		cv.put(SessionDetailTable.COLUMN_TOTAL_AMOUNT_RECEIPT, totalAmountReceipt);
		return getWritableDatabase().insertOrThrow(SessionDetailTable.TABLE_SESSION_ENDDAY_DETAIL, null, cv);
	}

	/**
	 * @param sessionId
	 * @param closeStaffId
	 * @param closeAmount
	 * @param isEndday
	 * @return row affected
	 */
	public int closeSession(int sessionId, int closeStaffId, 
			double closeAmount, boolean isEndday){
		Calendar dateTime = Utils.getCalendar();
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_CLOSE_STAFF, closeStaffId);
		cv.put(SessionTable.COLUMN_CLOSE_DATE, dateTime.getTimeInMillis());
		cv.put(SessionTable.COLUMN_CLOSE_AMOUNT, closeAmount);
		cv.put(SessionTable.COLUMN_IS_ENDDAY, isEndday == true ? 1 : 0);
		return getWritableDatabase().update(SessionTable.TABLE_SESSION, cv, 
				SessionTable.COLUMN_SESS_ID + "=?", 
				new String[]{
					String.valueOf(sessionId)
				});
	}

	
	/**
	 * @param sessionDate
	 * @return true if already end day
	 */
	public boolean checkEndday(String sessionDate){
		boolean isEndday = false;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT COUNT(*) " 
				+ " FROM " + SessionTable.TABLE_SESSION
				+ " WHERE " + SessionTable.COLUMN_SESS_DATE + "=?"
				+ " AND " + SessionTable.COLUMN_IS_ENDDAY + "=?", 
				new String[]{
						sessionDate,
						String.valueOf(ALREADY_ENDDAY_STATUS)
				});
		if(cursor.moveToFirst()){
			if(cursor.getInt(0) > 0)
				isEndday = true;
		}
		cursor.close();
		return isEndday;
	}
	
	/**
	 * @param staffId
	 * @param saleDate
	 * @return sessionId
	 */
	public int getCurrentSession(int staffId, String saleDate) {
		int sessionId = 0;
		Cursor cursor = getReadableDatabase().query(
				SessionTable.TABLE_SESSION,
				new String[] { SessionTable.COLUMN_SESS_ID },
				OrderTransactionTable.COLUMN_OPEN_STAFF + " =? " + " AND "
						+ SessionTable.COLUMN_SESS_DATE + " =? " + " AND "
						+ SessionTable.COLUMN_IS_ENDDAY + " =? ",
				new String[] { String.valueOf(staffId), saleDate,
						String.valueOf(NOT_ENDDAY_STATUS) }, null, null, null);
		if (cursor.moveToFirst()) {
			sessionId = cursor.getInt(0);
		}
		cursor.close();
		return sessionId;
	}
	
	/**
	 * @return current sessionId
	 */
	public int getCurrentSessionId() {
		int sessionId = 0;
		Cursor cursor = getReadableDatabase().query(
				SessionTable.TABLE_SESSION,
				new String[] { SessionTable.COLUMN_SESS_ID },
				SessionTable.COLUMN_IS_ENDDAY + "=?",
				new String[] {
						String.valueOf(NOT_ENDDAY_STATUS) }, null, null, 
						SessionTable.COLUMN_SESS_ID + " DESC ", "1");
		if (cursor.moveToFirst()) {
			sessionId = cursor.getInt(0);
		}
		cursor.close();
		return sessionId;
	}
	
	/**
	 * @param staffId
	 * @return current sessionId by staff
	 */
	public int getCurrentSessionId(int staffId) {
		int sessionId = 0;
		Cursor cursor = getReadableDatabase().query(
				SessionTable.TABLE_SESSION,
				new String[] { SessionTable.COLUMN_SESS_ID },
				OrderTransactionTable.COLUMN_OPEN_STAFF + "=?" + " AND "
						+ SessionTable.COLUMN_IS_ENDDAY + "=?",
				new String[] { String.valueOf(staffId),
						String.valueOf(NOT_ENDDAY_STATUS) }, null, null, null);
		if (cursor.moveToFirst()) {
			sessionId = cursor.getInt(0);
		}
		cursor.close();
		return sessionId;
	}
}
