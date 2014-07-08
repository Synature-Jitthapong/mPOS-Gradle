package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class ShopTable{

	public static final String TABLE_SHOP = "Shop";
	public static final String COLUMN_SHOP_ID = "shop_id";
	public static final String COLUMN_SHOP_CODE = "shop_code";
	public static final String COLUMN_SHOP_NAME = "shop_name";
	public static final String COLUMN_SHOP_TYPE = "shop_type";
	public static final String COLUMN_FAST_FOOD_TYPE = "fast_food_type";
	public static final String COLUMN_VAT_TYPE = "vat_type";
	public static final String COLUMN_OPEN_HOUR = "open_hour";
	public static final String COLUMN_CLOSE_HOUR = "close_hour";
	public static final String COLUMN_COMPANY = "company";
	public static final String COLUMN_ADDR1 = "addr1";
	public static final String COLUMN_ADDR2 = "addr2";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_PROVINCE_ID = "province_id";
	public static final String COLUMN_ZIPCODE = "zip_code";
	public static final String COLUMN_TELEPHONE = "telephone";
	public static final String COLUMN_FAX = "fax";
	public static final String COLUMN_TAX_ID = "tax_id";
	public static final String COLUMN_REGISTER_ID = "register_id";
	public static final String COLUMN_VAT = "vat";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_SHOP + " ( " 
			+ COLUMN_SHOP_ID + " INTEGER, " 
			+ COLUMN_SHOP_CODE + " TEXT, " 
			+ COLUMN_SHOP_NAME + " TEXT, " 
			+ COLUMN_SHOP_TYPE + " INTEGER, " 
			+ COLUMN_FAST_FOOD_TYPE + " INTEGER, "
			+ COLUMN_VAT_TYPE + " INTEGER, " 
			+ COLUMN_OPEN_HOUR + " TEXT, "
			+ COLUMN_CLOSE_HOUR + " TEXT, " 
			+ COLUMN_COMPANY + " TEXT, "
			+ COLUMN_ADDR1 + " TEXT, " 
			+ COLUMN_ADDR2 + " TEXT, "
			+ COLUMN_CITY + " TEXT, " 
			+ COLUMN_PROVINCE_ID + " INTEGER, "
			+ COLUMN_ZIPCODE + " TEXT, " 
			+ COLUMN_TELEPHONE + " TEXT, "
			+ COLUMN_FAX + " TEXT, " 
			+ COLUMN_TAX_ID + " TEXT, "
			+ COLUMN_REGISTER_ID + " TEXT, " 
			+ COLUMN_VAT + " REAL );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
		onCreate(db);
	}
}
