package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author j1tth4
 * MenuComment Table
 */
public class MenuFixCommentTable{
	
	public static final String TABLE_MENU_FIX_COMMENT = "MenuFixComment";
	
	private static final String SQL_CREATE = 
			"CREATE TABLE " + TABLE_MENU_FIX_COMMENT + " ( "
			+ ProductTable.COLUMN_PRODUCT_ID + " INTEGER NOT NULL, "
			+ MenuCommentTable.COLUMN_COMMENT_ID + " INTEGER NOT NULL);";
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_FIX_COMMENT);
		onCreate(db);
	}
}