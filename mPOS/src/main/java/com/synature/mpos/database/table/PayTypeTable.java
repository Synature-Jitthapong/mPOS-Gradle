package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class PayTypeTable{

	public static final String TABLE_PAY_TYPE = "PayType";
	public static final String COLUMN_PAY_TYPE_ID = "pay_type_id";
	public static final String COLUMN_PAY_TYPE_CODE = "pay_type_code";
	public static final String COLUMN_PAY_TYPE_NAME = "pay_type_name";
	public static final String COLUMN_ORDERING = "ordering";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PAY_TYPE + " ( " 
			+ COLUMN_PAY_TYPE_ID + " INTEGER, " 
			+ COLUMN_PAY_TYPE_CODE + " TEXT, " 
			+ COLUMN_PAY_TYPE_NAME + " TEXT, " 
			+ COLUMN_ORDERING + " INTEGER DEFAULT 0, " 
			+ "PRIMARY KEY (" + COLUMN_PAY_TYPE_ID + ") );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAY_TYPE);
		onCreate(db);
	}
}