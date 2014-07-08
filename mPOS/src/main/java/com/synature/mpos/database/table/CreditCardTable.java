package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class CreditCardTable{

	public static final String TABLE_CREDIT_CARD_TYPE = "CreditCardType";
	public static final String COLUMN_CREDITCARD_TYPE_ID = "creditcard_type_id";
	public static final String COLUMN_CREDITCARD_TYPE_NAME = "creditcard_type_name";
	public static final String COLUMN_CREDITCARD_NO = "creditcard_no";
	public static final String COLUMN_EXP_MONTH = "exp_month";
	public static final String COLUMN_EXP_YEAR = "exp_year";

	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_CREDIT_CARD_TYPE + " ( " 
			+ COLUMN_CREDITCARD_TYPE_ID
			+ " INTEGER, " + COLUMN_CREDITCARD_TYPE_NAME + " TEXT, "
			+ " PRIMARY KEY (" + COLUMN_CREDITCARD_TYPE_ID + ") );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDIT_CARD_TYPE);
		onCreate(db);
	}
}