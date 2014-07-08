package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class OrderCommentTable{
	
	public static final String TABLE_ORDER_COMMENT = "OrderComment";
	
	public static final String TEMP_ORDER_COMMENT = "OrderCommentTemp";
	
	public static final String COLUMN_ORDER_COMMENT_QTY = "order_comment_qty";
	public static final String COLUMN_ORDER_COMMENT_PRICE = "order_comment_price";
	
	private static final String SQL_CREATE = "CREATE TABLE " + TABLE_ORDER_COMMENT + " ( "
			+ OrderTransactionTable.COLUMN_TRANSACTION_ID + " INTEGER NOT NULL, "
			+ OrderDetailTable.COLUMN_ORDER_ID + " INTEGER NOT NULL, "
			+ MenuCommentTable.COLUMN_COMMENT_ID + " INTEGER NOT NULL, "
			+ COLUMN_ORDER_COMMENT_QTY + " REAL NOT NULL DEFAULT 0, "
			+ COLUMN_ORDER_COMMENT_PRICE + " REAL NOT NULL DEFAULT 0 );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
	}
}
