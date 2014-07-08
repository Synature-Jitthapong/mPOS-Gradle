package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class BankTable {

	public static final String TABLE_BANK = "BankName";
	public static final String COLUMN_BANK_ID = "bank_id";
	public static final String COLUMN_BANK_NAME = "bank_name";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_BANK
			+ " ( " + COLUMN_BANK_ID + " INTEGER, " 
			+ COLUMN_BANK_NAME + " TEXT, " 
			+ "PRIMARY KEY (" + COLUMN_BANK_ID + ") );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK);
		onCreate(db);
	}
	
}