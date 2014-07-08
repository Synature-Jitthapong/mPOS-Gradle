package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class GlobalPropertyTable{

	public static final String TABLE_GLOBAL_PROPERTY = "GlobalProperty";
	public static final String COLUMN_CURRENCY_SYMBOL = "currency_symbol";
	public static final String COLUMN_CURRENCY_CODE = "currency_code";
	public static final String COLUMN_CURRENCY_NAME = "currency_name";
	public static final String COLUMN_CURRENCY_FORMAT = "currency_format";
	public static final String COLUMN_QTY_FORMAT = "qty_format";
	public static final String COLUMN_DATE_FORMAT = "date_format";
	public static final String COLUMN_TIME_FORMAT = "time_format";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_GLOBAL_PROPERTY + " ( " 
			+ COLUMN_CURRENCY_SYMBOL + " TEXT DEFAULT '$', " 
			+ COLUMN_CURRENCY_CODE + " TEXT DEFAULT 'USD', " 
			+ COLUMN_CURRENCY_NAME + " TEXT, "
			+ COLUMN_CURRENCY_FORMAT + " TEXT DEFAULT '#,##0.00', "
			+ COLUMN_QTY_FORMAT + " TEXT DEFAULT '#,##0', "
			+ COLUMN_DATE_FORMAT + " TEXT DEFAULT 'd MMMM yyyy', "
			+ COLUMN_TIME_FORMAT + " TEXT DEFAULT 'HH:mm:ss' );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLOBAL_PROPERTY);
		onCreate(db);
	}
}