package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ProductTable extends BaseColumn{
	
	public static final String TABLE_PRODUCT = "Product";
	public static final String COLUMN_PRODUCT_ID = "product_id";
	public static final String COLUMN_PRODUCT_DEPT_ID = "product_dept_id";
	public static final String COLUMN_PRODUCT_GROUP_ID = "product_group_id";
	public static final String COLUMN_PRODUCT_CODE = "product_code";
	public static final String COLUMN_PRODUCT_BAR_CODE = "product_barcode";
	public static final String COLUMN_PRODUCT_NAME = "product_name";
	public static final String COLUMN_PRODUCT_DESC = "product_desc";
	public static final String COLUMN_PRODUCT_TYPE_ID = "product_type_id";
	public static final String COLUMN_PRODUCT_PRICE = "product_price";
	public static final String COLUMN_PRODUCT_UNIT_NAME = "product_unitname";
	public static final String COLUMN_DISCOUNT_ALLOW = "discount_allow";
	public static final String COLUMN_VAT_TYPE = "vat_type";
	public static final String COLUMN_VAT_RATE = "vat_rate";
	public static final String COLUMN_ISOUTOF_STOCK = "isoutof_stock";
	public static final String COLUMN_IMG_URL = "image_url";
	public static final String COLUMN_ACTIVATE = "activate";
	public static final String COLUMN_SALE_MODE = "sale_mode";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PRODUCT + " ( " 
			+ COLUMN_PRODUCT_ID + " INTEGER, " 
			+ COLUMN_PRODUCT_DEPT_ID + " INTEGER, " 
			+ COLUMN_PRODUCT_CODE + " TEXT, "
			+ COLUMN_PRODUCT_BAR_CODE + " TEXT, " 
			+ COLUMN_PRODUCT_NAME + " TEXT, " 
			+ COLUMN_PRODUCT_DESC + " TEXT, "
			+ COLUMN_PRODUCT_TYPE_ID + " INTEGER DEFAULT 0, "
			+ COLUMN_PRODUCT_PRICE + " REAL DEFAULT 0, "
			+ COLUMN_PRODUCT_UNIT_NAME + " TEXT, " 
			+ COLUMN_DISCOUNT_ALLOW + " INTEGER DEFAULT 1, " 
			+ COLUMN_VAT_TYPE + " INTEGER DEFAULT 1, "
			+ COLUMN_VAT_RATE + " REAL DEFAULT 0, " 
			+ COLUMN_ISOUTOF_STOCK + " INTEGER DEFAULT 0, " 
			+ COLUMN_IMG_URL + " TEXT, " 
			+ COLUMN_ACTIVATE + " INTEGER DEFAULT 0, " 
			+ COLUMN_ORDERING + " INTEGER DEFAULT 0, " 
			+ "PRIMARY KEY (" + COLUMN_PRODUCT_ID + " ASC));";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
		onCreate(db);
	}
}
