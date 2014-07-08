package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ProductComponentGroupTable {
	
	public static final String TABLE_PCOMPONENT_GROUP = "PComponentGroup";
	public static final String COLUMN_SET_GROUP_NO = "set_group_no";
	public static final String COLUMN_SET_GROUP_NAME = "set_group_name";
	public static final String COLUMN_REQ_AMOUNT = "req_amount";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PCOMPONENT_GROUP + " ( "
			+ ProductComponentTable.COLUMN_PGROUP_ID + " INTEGER, "
			+ ProductTable.COLUMN_PRODUCT_ID + " INTEGER, "
			+ ProductTable.COLUMN_SALE_MODE + " INTEGER DEFAULT 0, "
			+ COLUMN_SET_GROUP_NO + " TEXT, " 
			+ COLUMN_SET_GROUP_NAME + " TEXT, " 
			+ COLUMN_REQ_AMOUNT + " REAL DEFAULT 0 " + ");";
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PCOMPONENT_GROUP);
		onCreate(db);
	}
}
