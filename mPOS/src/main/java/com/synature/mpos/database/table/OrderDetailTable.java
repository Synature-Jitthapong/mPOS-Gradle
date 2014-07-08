package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class OrderDetailTable extends BaseColumn{

	public static final String TABLE_ORDER = "OrderDetail";
	public static final String TABLE_ORDER_TMP = "OrderDetailTmp";
	public static final String COLUMN_ORDER_ID = "order_detail_id";
	public static final String COLUMN_ORDER_QTY = "qty";
	public static final String COLUMN_TOTAL_RETAIL_PRICE = "total_retail_price";
	public static final String COLUMN_TOTAL_SALE_PRICE = "total_sale_price";
	public static final String COLUMN_TOTAL_VAT = "total_vat_amount";
	public static final String COLUMN_TOTAL_VAT_EXCLUDE = "total_vat_amount_exclude";
	public static final String COLUMN_MEMBER_DISCOUNT = "member_discount_amount";
	public static final String COLUMN_PRICE_DISCOUNT = "price_discount_amount";
	public static final String COLUMN_DISCOUNT_TYPE = "discount_type";

	private static final String ORDER_SQL_CREATE = "CREATE TABLE "
			+ TABLE_ORDER + " ( " + COLUMN_ORDER_ID + " INTEGER, "
			+ OrderTransactionTable.COLUMN_TRANSACTION_ID + " INTEGER, "
			+ ComputerTable.COLUMN_COMPUTER_ID + " INTEGER, "
			+ ProductTable.COLUMN_PRODUCT_ID + " INTEGER, "
			+ ProductTable.COLUMN_PRODUCT_TYPE_ID + " INTEGER DEFAULT 1, "
			+ COLUMN_ORDER_QTY + " REAL DEFAULT 1, "
			+ ProductTable.COLUMN_PRODUCT_PRICE + " REAL DEFAULT 0, "
			+ COLUMN_DISCOUNT_TYPE + " INTEGER DEFAULT 2, "
			+ ProductTable.COLUMN_VAT_TYPE + " INTEGER DEFAULT 1, "
			+ COLUMN_TOTAL_VAT + " REAL DEFAULT 0, "
			+ COLUMN_TOTAL_VAT_EXCLUDE + " REAL DEFAULT 0, "
			+ COLUMN_MEMBER_DISCOUNT + " REAL DEFAULT 0, "
			+ COLUMN_PRICE_DISCOUNT + " REAL DEFAULT 0, "
			+ COLUMN_TOTAL_RETAIL_PRICE + " REAL DEFAULT 0, "
			+ COLUMN_TOTAL_SALE_PRICE + " REAL DEFAULT 0, "
			+ ProductTable.COLUMN_SALE_MODE + " INTEGER DEFAULT 1, "
			+ COLUMN_REMARK + " TEXT, " 
			+ "PRIMARY KEY (" + COLUMN_ORDER_ID + ", "
			+ OrderTransactionTable.COLUMN_TRANSACTION_ID + ") );";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(ORDER_SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
	}
}