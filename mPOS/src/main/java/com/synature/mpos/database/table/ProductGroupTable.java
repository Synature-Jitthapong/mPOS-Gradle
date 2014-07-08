package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ProductGroupTable extends BaseColumn{
	
	public static final String TABLE_PRODUCT_GROUP = "ProductGroup";
	public static final String COLUMN_PRODUCT_GROUP_CODE = "product_group_code";
	public static final String COLUMN_PRODUCT_GROUP_NAME = "product_group_name";
	public static final String COLUMN_PRODUCT_GROUP_TYPE = "product_group_type";
	public static final String COLUMN_IS_COMMENT = "is_comment";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PRODUCT_GROUP + " ( "
			+ ProductTable.COLUMN_PRODUCT_GROUP_ID + " INTEGER, "
			+ COLUMN_PRODUCT_GROUP_CODE + " TEXT, " 
			+ COLUMN_PRODUCT_GROUP_NAME + " TEXT, " 
			+ COLUMN_PRODUCT_GROUP_TYPE + " INTEGER DEFAULT 0, "
			+ COLUMN_IS_COMMENT + " INTEGER DEFAULT 0, "
			+ ProductTable.COLUMN_ACTIVATE + " INTEGER DEFAULT 0, "
			+ COLUMN_ORDERING + " INTEGER DEFAULT 0, " 
			+ "PRIMARY KEY (" + ProductTable.COLUMN_PRODUCT_GROUP_ID + "));";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_GROUP);
		onCreate(db);
	}		
}
