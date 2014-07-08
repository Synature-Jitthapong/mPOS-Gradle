package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class OrderSetTable {

	public static final String TABLE_ORDER_SET = "OrderSet";
	public static final String COLUMN_ORDER_SET_ID = "order_set_id";
	public static final String COLUMN_ORDER_SET_QTY = "order_set_qty";
	public static final String COLUMN_ORDER_SET_PRICE = "order_set_price";

	private static final String SQL_CREATE = "CREATE TABLE "
			+ TABLE_ORDER_SET + "( " + COLUMN_ORDER_SET_ID + " INTEGER NOT NULL," 
			+ OrderDetailTable.COLUMN_ORDER_ID + " INTEGER NOT NULL, "
			+ OrderTransactionTable.COLUMN_TRANSACTION_ID + " INTEGER NOT NULL, "
			+ ProductComponentTable.COLUMN_PGROUP_ID + " INTEGER NOT NULL, " 
			+ ProductTable.COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " 
			+ ProductComponentGroupTable.COLUMN_REQ_AMOUNT + " REAL NOT NULL DEFAULT 0, " 
			+ COLUMN_ORDER_SET_QTY + " REAL NOT NULL DEFAULT 0, " 
			+ COLUMN_ORDER_SET_PRICE + " REAL DEFAULT 0, "
			+ "PRIMARY KEY ("
			+ COLUMN_ORDER_SET_ID + ", " + OrderDetailTable.COLUMN_ORDER_ID
			+ ", " + OrderTransactionTable.COLUMN_TRANSACTION_ID + ")"
			+ ");";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
	}
}