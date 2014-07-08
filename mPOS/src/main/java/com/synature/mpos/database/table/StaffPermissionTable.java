package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

public class StaffPermissionTable {

	public static final String TABLE_STAFF_PERMISSION = "StaffPermission";
	public static final String COLUMN_STAFF_ROLE_ID = "staff_role_id";
	public static final String COLUMN_PERMMISSION_ITEM_ID = "permission_item_id";
	
	private static final String SQL_CREATE =
			"CREATE TABLE " + TABLE_STAFF_PERMISSION + " ( " 
			+ COLUMN_STAFF_ROLE_ID + " INTEGER DEFAULT 0, " 
			+ COLUMN_PERMMISSION_ITEM_ID + " INTEGER DEFAULT 0);";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF_PERMISSION);
		onCreate(db);
	}
}