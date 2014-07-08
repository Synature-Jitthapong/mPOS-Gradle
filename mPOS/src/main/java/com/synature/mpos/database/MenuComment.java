package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.database.table.MenuCommentGroupTable;
import com.synature.mpos.database.table.MenuCommentTable;
import com.synature.mpos.database.table.MenuFixCommentTable;
import com.synature.mpos.database.table.ProductTable;
import com.synature.pos.MenuGroups;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class MenuComment extends MPOSDatabase{

	public MenuComment(Context context) {
		super(context);
	}

	/**
	 * @return List<Comment> 
	 */
	public List<Comment> listMenuComment(){
		List<Comment> commentLst = new ArrayList<Comment>();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT a." + MenuCommentTable.COLUMN_COMMENT_ID + ","
				+ " a." + MenuCommentTable.COLUMN_COMMENT_NAME + ","
				+ " b." + ProductTable.COLUMN_PRODUCT_PRICE
				+ " FROM " + MenuCommentTable.TABLE_MENU_COMMENT + " a "
				+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
				+ " ON a." + MenuCommentTable.COLUMN_COMMENT_ID
				+ " =b." + ProductTable.COLUMN_PRODUCT_ID
				+ " ORDER BY a." + COLUMN_ORDERING, null);
		if(cursor.moveToFirst()){
			do{
				Comment comment = new Comment();
				comment.setCommentId(cursor.getInt(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_ID)));
				comment.setCommentName(cursor.getString(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_NAME)));
				comment.setCommentPrice(cursor.getDouble(
						cursor.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
				commentLst.add(comment);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return commentLst;
	}
	
	/**
	 * @param groupId
	 * @return List<Comment>
	 */
	public List<Comment> listMenuComment(int groupId){
		List<Comment> commentLst = new ArrayList<Comment>();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT a." + MenuCommentTable.COLUMN_COMMENT_ID + ","
				+ " a." + MenuCommentTable.COLUMN_COMMENT_NAME + ","
				+ " b." + ProductTable.COLUMN_PRODUCT_PRICE
				+ " FROM " + MenuCommentTable.TABLE_MENU_COMMENT + " a "
				+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
				+ " ON a." + MenuCommentTable.COLUMN_COMMENT_ID
				+ " =b." + ProductTable.COLUMN_PRODUCT_ID
				+ " WHERE a." + MenuCommentTable.COLUMN_COMMENT_GROUP_ID + "=?"
				+ " ORDER BY a." + COLUMN_ORDERING, 
				new String[]{
						String.valueOf(groupId)
				});
		if(cursor.moveToFirst()){
			do{
				Comment comment = new Comment();
				comment.setCommentId(cursor.getInt(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_ID)));
				comment.setCommentName(cursor.getString(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_NAME)));
				comment.setCommentPrice(cursor.getDouble(
						cursor.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
				commentLst.add(comment);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return commentLst;
	}
	
	/**
	 * @return List<CommentGroup> 
	 */
	public List<CommentGroup> listMenuCommentGroup(){
		List<CommentGroup> comGroupLst = new ArrayList<CommentGroup>();
		Cursor cursor = getReadableDatabase().query(
				MenuCommentGroupTable.TABLE_MENU_COMMENT_GROUP, 
				new String[]{
					MenuCommentTable.COLUMN_COMMENT_GROUP_ID,
					MenuCommentGroupTable.COLUMN_COMMENT_GROUP_NAME
				}, 
				null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				CommentGroup comGroup = new CommentGroup();
				comGroup.setCommentGroupId(cursor.getInt(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_GROUP_ID)));
				comGroup.setCommentGroupName(cursor.getString(
						cursor.getColumnIndex(MenuCommentGroupTable.COLUMN_COMMENT_GROUP_NAME)));
				comGroupLst.add(comGroup);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return comGroupLst;
	}
	
	/**
	 * @param commentFixLst
	 */
	public void insertMenuFixComment(List<MenuGroups.MenuFixComment> commentFixLst){
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(MenuFixCommentTable.TABLE_MENU_FIX_COMMENT, null, null);
			for(MenuGroups.MenuFixComment commentFix : commentFixLst){
				ContentValues cv = new ContentValues();
				cv.put(ProductTable.COLUMN_PRODUCT_ID, commentFix.getMenuItemID());
				cv.put(MenuCommentTable.COLUMN_COMMENT_ID, commentFix.getMenuCommentID());
				getWritableDatabase().insertOrThrow(MenuFixCommentTable.TABLE_MENU_FIX_COMMENT, 
						null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
	
	/**
	 * @param commentGroupLst
	 * @throws SQLException
	 */
	public void insertMenuCommentGroup(List<MenuGroups.MenuCommentGroup> commentGroupLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(MenuCommentGroupTable.TABLE_MENU_COMMENT_GROUP, null, null);
			for(MenuGroups.MenuCommentGroup commentGroup : commentGroupLst){
				ContentValues cv = new ContentValues();
				cv.put(MenuCommentTable.COLUMN_COMMENT_GROUP_ID, 
						commentGroup.getMenuCommentGroupID());
				cv.put(MenuCommentGroupTable.COLUMN_COMMENT_GROUP_NAME, 
						commentGroup.getMenuCommentGroupName_0());
				getWritableDatabase().insertOrThrow(MenuCommentGroupTable.TABLE_MENU_COMMENT_GROUP, 
						null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
	
	/**
	 * @param commentLst
	 * @throws SQLException
	 */
	public void insertMenuComment(List<MenuGroups.MenuComment> commentLst) throws SQLException{
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(MenuCommentTable.TABLE_MENU_COMMENT, null, null);
			for(MenuGroups.MenuComment comment : commentLst){
				ContentValues cv = new ContentValues();
				cv.put(MenuCommentTable.COLUMN_COMMENT_ID, comment.getMenuCommentID());
				cv.put(MenuCommentTable.COLUMN_COMMENT_GROUP_ID, comment.getMenuCommentGroupID());
				cv.put(MenuCommentTable.COLUMN_COMMENT_NAME, comment.getMenuCommentName_0());
				cv.put(COLUMN_ORDERING, comment.getMenuCommentOrdering());
				getWritableDatabase().insertOrThrow(MenuCommentTable.TABLE_MENU_COMMENT, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally{
			getWritableDatabase().endTransaction();
		}
	}
	
	public static class Comment{
		private int commentId;
		private String commentName;
		private double commentQty;
		private double commentPrice;
		private boolean isSelected;
		
		public double getCommentQty() {
			return commentQty;
		}
		public void setCommentQty(double commentQty) {
			this.commentQty = commentQty;
		}
		public double getCommentPrice() {
			return commentPrice;
		}
		public void setCommentPrice(double commentPrice) {
			this.commentPrice = commentPrice;
		}
		public boolean isSelected() {
			return isSelected;
		}
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
		public int getCommentId() {
			return commentId;
		}
		public void setCommentId(int commentId) {
			this.commentId = commentId;
		}
		public String getCommentName() {
			return commentName;
		}
		public void setCommentName(String commentName) {
			this.commentName = commentName;
		}
	}
	
	public static class CommentGroup{
		private int commentGroupId;
		private String commentGroupName;
		
		public int getCommentGroupId() {
			return commentGroupId;
		}
		public void setCommentGroupId(int commentGroupId) {
			this.commentGroupId = commentGroupId;
		}
		public String getCommentGroupName() {
			return commentGroupName;
		}
		public void setCommentGroupName(String commentGroupName) {
			this.commentGroupName = commentGroupName;
		}
		
		@Override
		public String toString() {
			return commentGroupName;
		}
	}
}
