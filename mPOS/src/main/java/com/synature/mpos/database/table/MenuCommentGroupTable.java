package com.synature.mpos.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author j1tth4
 * MenuCommentGroup Table
 */
public class MenuCommentGroupTable{
	
	public static final String TABLE_MENU_COMMENT_GROUP = "MenuCommentGroup";
	public static final String COLUMN_COMMENT_GROUP_NAME = "menu_comment_group_name";
	
	private static final String SQL_CREATE = 
			"CREATE TABLE " + TABLE_MENU_COMMENT_GROUP + " ( "
			+ MenuCommentTable.COLUMN_COMMENT_GROUP_ID + " INTEGER NOT NULL, "
			+ COLUMN_COMMENT_GROUP_NAME + " TEXT, "
			+ " PRIMARY KEY (" + MenuCommentTable.COLUMN_COMMENT_GROUP_ID + "));";
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_COMMENT_GROUP);
		onCreate(db);
	}
}
