package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class StaffTable {

	public static final String TABLE_STAFF = "Staffs";
	public static final String COLUMN_STAFF_ID = "staff_id";
	public static final String COLUMN_STAFF_CODE = "staff_code";
	public static final String COLUMN_STAFF_NAME = "staff_name";
	public static final String COLUMN_STAFF_PASS = "staff_password";

	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_STAFF + " ( " 
			+ COLUMN_STAFF_ID + " INTEGER, " 
			+ COLUMN_STAFF_CODE + " TEXT, " 
			+ COLUMN_STAFF_NAME + " TEXT, " 
			+ COLUMN_STAFF_PASS + " TEXT, " 
			+ "PRIMARY KEY (" + COLUMN_STAFF_ID + "));";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
		onCreate(db);
	}
}