package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

import com.synature.mpos.database.StockDocument.DocumentTypeTable;

public class OrderTransactionTable extends BaseColumn {
	public static final String TABLE_ORDER_TRANS = "OrderTransaction";
	public static final String COLUMN_TRANSACTION_ID = "transaction_id";
	public static final String COLUMN_RECEIPT_YEAR = "receipt_year";
	public static final String COLUMN_RECEIPT_MONTH = "receipt_month";
	public static final String COLUMN_RECEIPT_ID = "receipt_id";
	public static final String COLUMN_RECEIPT_NO = "receipt_no";
	public static final String COLUMN_OPEN_TIME = "open_time";
	public static final String COLUMN_CLOSE_TIME = "close_time";
	public static final String COLUMN_OPEN_STAFF = "open_staff_id";
	public static final String COLUMN_CLOSE_STAFF = "close_staff_id";
	public static final String COLUMN_STATUS_ID = "transaction_status_id";
	public static final String COLUMN_PAID_TIME = "paid_time";
	public static final String COLUMN_PAID_STAFF_ID = "paid_staff_id";
	public static final String COLUMN_SALE_DATE = "sale_date";
	public static final String COLUMN_TRANS_VAT = "transaction_vat";
	public static final String COLUMN_TRANS_VATABLE = "transaction_vatable";
	public static final String COLUMN_TRANS_EXCLUDE_VAT = "transaction_exclude_vat";
	public static final String COLUMN_TRANS_NOTE = "transaction_note";
	public static final String COLUMN_VOID_STAFF_ID = "void_staff_id";
	public static final String COLUMN_VOID_REASON = "void_reason";
	public static final String COLUMN_VOID_TIME = "void_time";
	public static final String COLUMN_OTHER_DISCOUNT = "other_discount";
	public static final String COLUMN_MEMBER_ID = "member_id";

	private static final String SQL_CREATE = "CREATE TABLE "
			+ TABLE_ORDER_TRANS + " ( " + COLUMN_UUID
			+ " TEXT, " + COLUMN_TRANSACTION_ID + " INTEGER, "
			+ ComputerTable.COLUMN_COMPUTER_ID + " INTEGER, "
			+ ShopTable.COLUMN_SHOP_ID + " INTEGER, " + COLUMN_OPEN_TIME
			+ " TEXT, " + COLUMN_OPEN_STAFF + " INTEGER, "
			+ COLUMN_PAID_TIME + " TEXT, " + COLUMN_PAID_STAFF_ID
			+ " INTEGER, " + COLUMN_CLOSE_TIME + " TEXT, "
			+ COLUMN_CLOSE_STAFF + " INTEGER, " + COLUMN_STATUS_ID
			+ " INTEGER DEFAULT 1, " + DocumentTypeTable.COLUMN_DOC_TYPE
			+ " INTEGER DEFAULT 8, " + COLUMN_RECEIPT_YEAR + " INTEGER, "
			+ COLUMN_RECEIPT_MONTH + " INTEGER, " + COLUMN_RECEIPT_ID
			+ " INTEGER, " + COLUMN_RECEIPT_NO + " TEXT, "
			+ COLUMN_SALE_DATE + " TEXT, " + SessionTable.COLUMN_SESS_ID
			+ " INTEGER, " + COLUMN_VOID_STAFF_ID + " INTEGER, "
			+ COLUMN_VOID_REASON + " TEXT, " + COLUMN_VOID_TIME + " TEXT, "
			+ COLUMN_MEMBER_ID + " INTEGER, " + COLUMN_TRANS_VAT
			+ " REAL DEFAULT 0, " + COLUMN_TRANS_EXCLUDE_VAT
			+ " REAL DEFAULT 0, " + COLUMN_TRANS_VATABLE
			+ " REAL DEFAULT 0, " + COLUMN_TRANS_NOTE + " TEXT, "
			+ COLUMN_OTHER_DISCOUNT + " REAL DEFAULT 0, "
			+ COLUMN_SEND_STATUS + " INTEGER DEFAULT 0, "
			+ ProductTable.COLUMN_SALE_MODE + " INTEGER DEFAULT 1, "
			+ ProductTable.COLUMN_VAT_RATE + " REAL DEFAULT 0, "
			+ "PRIMARY KEY (" + COLUMN_TRANSACTION_ID + ") ); ";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
	}
}
