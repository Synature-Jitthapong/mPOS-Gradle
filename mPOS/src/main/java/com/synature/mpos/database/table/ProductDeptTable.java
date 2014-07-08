package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ProductDeptTable extends BaseColumn{
	
	public static final String TABLE_PRODUCT_DEPT = "ProductDept";
	public static final String COLUMN_PRODUCT_DEPT_CODE = "product_dept_code";
	public static final String COLUMN_PRODUCT_DEPT_NAME = "product_dept_name";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PRODUCT_DEPT + " ( " 
			+ ProductTable.COLUMN_PRODUCT_DEPT_ID + " INTEGER, " 
			+ ProductTable.COLUMN_PRODUCT_GROUP_ID + " INTEGER, " 
			+ COLUMN_PRODUCT_DEPT_CODE + " TEXT, "
			+ COLUMN_PRODUCT_DEPT_NAME + " TEXT, "
			+ ProductTable.COLUMN_ACTIVATE + " INTEGER DEFAULT 0, "
			+ COLUMN_ORDERING + " INTEGER DEFAULT 0, " 
			+ "PRIMARY KEY (" + ProductTable.COLUMN_PRODUCT_DEPT_ID + "));";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_DEPT);
		onCreate(db);
	}
}
