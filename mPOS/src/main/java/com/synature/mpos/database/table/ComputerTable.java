package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ComputerTable{
	
	public static final String TABLE_COMPUTER = "Computer";
	public static final String COLUMN_COMPUTER_ID = "computer_id";
	public static final String COLUMN_COMPUTER_NAME = "computer_name";
	public static final String COLUMN_DEVICE_CODE = "device_code";
	public static final String COLUMN_REGISTER_NUMBER = "register_number";
	public static final String COLUMN_IS_MAIN_COMPUTER = "ismain_computer";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_COMPUTER
			+ " ( " + COLUMN_COMPUTER_ID + " INTEGER, " 
			+ COLUMN_COMPUTER_NAME + " TEXT, " 
			+ COLUMN_DEVICE_CODE + " TEXT, "
			+ COLUMN_REGISTER_NUMBER + " TEXT, " 
			+ COLUMN_IS_MAIN_COMPUTER + " INTEGER DEFAULT 0, " 
			+ "PRIMARY KEY (" + COLUMN_COMPUTER_ID + ") );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPUTER);
		onCreate(db);
	}
}
