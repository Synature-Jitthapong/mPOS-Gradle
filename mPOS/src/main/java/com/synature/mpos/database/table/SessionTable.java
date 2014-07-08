package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class SessionTable{
	
	public static final String TABLE_SESSION = "Session";
	public static final String COLUMN_SESS_ID = "session_id";
	public static final String COLUMN_SESS_DATE = "session_date";
	public static final String COLUMN_OPEN_DATE = "open_date_time";
	public static final String COLUMN_CLOSE_DATE = "close_date_time";
	public static final String COLUMN_OPEN_AMOUNT = "open_amount";
	public static final String COLUMN_CLOSE_AMOUNT = "close_amount";
	public static final String COLUMN_IS_ENDDAY = "is_endday";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_SESSION + " ( " 
			+ COLUMN_SESS_ID + " INTEGER, "
			+ ComputerTable.COLUMN_COMPUTER_ID + " INTEGER, "
			+ ShopTable.COLUMN_SHOP_ID + " INTEGER, "
			+ OrderTransactionTable.COLUMN_OPEN_STAFF + " INTEGER, "
			+ OrderTransactionTable.COLUMN_CLOSE_STAFF + " INTEGER, "
			+ COLUMN_SESS_DATE + " TEXT, " 
			+ COLUMN_OPEN_DATE + " TEXT, "
			+ COLUMN_CLOSE_DATE + " TEXT, " 
			+ COLUMN_OPEN_AMOUNT + " REAL NOT NULL DEFAULT 0, " 
			+ COLUMN_CLOSE_AMOUNT + " REAL NOT NULL DEFAULT 0, "
			+ COLUMN_IS_ENDDAY + " INTEGER NOT NULL DEFAULT 0, " 
			+ "PRIMARY KEY (" + COLUMN_SESS_ID + "));";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
		onCreate(db);
	}	
}
