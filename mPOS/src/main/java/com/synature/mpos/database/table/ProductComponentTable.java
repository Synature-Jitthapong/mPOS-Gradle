package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ProductComponentTable{
	
	public static final String TABLE_PCOMPONENT = "ProductComponent";
	public static final String COLUMN_PGROUP_ID = "pgroup_id";
	public static final String COLUMN_CHILD_PRODUCT_ID = "child_product_id";
	public static final String COLUMN_CHILD_PRODUCT_AMOUNT = "child_product_amount";
	public static final String COLUMN_FLEXIBLE_PRODUCT_PRICE = "flexible_product_price";
	public static final String COLUMN_FLEXIBLE_INCLUDE_PRICE = "flexible_include_price";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PCOMPONENT + " ( " 
			+ COLUMN_PGROUP_ID + " INTEGER, " 
			+ ProductTable.COLUMN_PRODUCT_ID + " INTEGER, "
			+ ProductTable.COLUMN_SALE_MODE + " INTEGER DEFAULT 0, "
			+ COLUMN_CHILD_PRODUCT_ID + " INTEGER, "
			+ COLUMN_CHILD_PRODUCT_AMOUNT + " REAL, "
			+ COLUMN_FLEXIBLE_PRODUCT_PRICE + " REAL DEFAULT 0, "
			+ COLUMN_FLEXIBLE_INCLUDE_PRICE + " INTEGER DEFAULT 0 " + ");";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PCOMPONENT);
		onCreate(db);
	}
}
