package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class PaymentDetailTable{
	
	public static final String TABLE_PAYMENT_DETAIL = "PaymentDetail";
	public static final String COLUMN_PAY_ID = "pay_detail_id";
	public static final String COLUMN_PAY_AMOUNT = "pay_amount";
	public static final String COLUMN_PAID = "paid";
	public static final String COLUMN_REMARK = "remark";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_PAYMENT_DETAIL + " ( " 
			+ COLUMN_PAY_ID + " INTEGER, "
			+ OrderTransactionTable.COLUMN_TRANSACTION_ID + " INTEGER, "
			+ ComputerTable.COLUMN_COMPUTER_ID + " INTEGER, "
			+ PayTypeTable.COLUMN_PAY_TYPE_ID + " INTEGER DEFAULT 1, "
			+ COLUMN_PAY_AMOUNT + " REAL DEFAULT 0, " 
			+ COLUMN_PAID + " REAL DEFAULT 0, " 
			+ CreditCardTable.COLUMN_CREDITCARD_NO + " TEXT, " 
			+ CreditCardTable.COLUMN_EXP_MONTH + " INTEGER, "
			+ CreditCardTable.COLUMN_EXP_YEAR + " INTEGER, "
			+ BankTable.COLUMN_BANK_ID + " INTEGER, "
			+ CreditCardTable.COLUMN_CREDITCARD_TYPE_ID + " INTEGER, "
			+ COLUMN_REMARK + " TEXT, " 
			+ "PRIMARY KEY (" + COLUMN_PAY_ID + " ASC, " 
			+ OrderTransactionTable.COLUMN_TRANSACTION_ID + " ASC, "
			+ ComputerTable.COLUMN_COMPUTER_ID + " ASC) );";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_DETAIL);
		onCreate(db);
	}
}
