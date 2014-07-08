package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.synature.mpos.Utils;
import com.synature.mpos.database.MPOSOrderTransaction.OrderSet;
import com.synature.mpos.database.StockDocument.DocumentTypeTable;
import com.synature.mpos.database.table.BaseColumn;
import com.synature.mpos.database.table.ComputerTable;
import com.synature.mpos.database.table.MenuCommentTable;
import com.synature.mpos.database.table.OrderCommentTable;
import com.synature.mpos.database.table.OrderDetailTable;
import com.synature.mpos.database.table.OrderSetTable;
import com.synature.mpos.database.table.OrderTransactionTable;
import com.synature.mpos.database.table.ProductComponentGroupTable;
import com.synature.mpos.database.table.ProductComponentTable;
import com.synature.mpos.database.table.ProductTable;
import com.synature.mpos.database.table.SessionTable;
import com.synature.mpos.database.table.ShopTable;
import com.synature.mpos.database.table.StaffTable;

/**
 * 
 * @author j1tth4
 * 
 */
public class Transaction extends MPOSDatabase {

	/**
	 * New transaction status
	 */
	public static final int TRANS_STATUS_NEW = 1;

	/**
	 * Success transaction status
	 */
	public static final int TRANS_STATUS_SUCCESS = 2;

	/**
	 * void transaction status
	 */
	public static final int TRANS_STATUS_VOID = 8;

	/**
	 * hold transaction status
	 */
	public static final int TRANS_STATUS_HOLD = 9;

	public Transaction(Context context) {
		super(context);
	}

	/**
	 * get transactionIds in day
	 * @param saleDate
	 * @return transId like "1,2,3"
	 */
	public String getSeperateTransactionId(String saleDate){
		String transactionIds = "";
		Cursor cursor = getReadableDatabase().query(
				OrderTransactionTable.TABLE_ORDER_TRANS, 
				new String[]{OrderTransactionTable.COLUMN_TRANSACTION_ID}, 
				OrderTransactionTable.COLUMN_SALE_DATE + "=?"
				+ " AND " + OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?)", 
				new String[]{
						saleDate,
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID)
				}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				transactionIds += cursor.getString(0);
				if(!cursor.isLast())
					transactionIds += ",";
			}while(cursor.moveToNext());
		}
		cursor.close();
		return transactionIds;
	}
	
	/**
	 * @param saleDate
	 * @return MPOSOrderTransaction
	 */
	public MPOSOrderTransaction getTransaction(String saleDate) {
		MPOSOrderTransaction trans = new MPOSOrderTransaction();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT " + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
				+ ComputerTable.COLUMN_COMPUTER_ID + ", "
				+ " SUM(" + OrderTransactionTable.COLUMN_TRANS_VATABLE + ")"
				+ " AS " + OrderTransactionTable.COLUMN_TRANS_VATABLE + ","
				+ " SUM(" + OrderTransactionTable.COLUMN_TRANS_VAT + ")"
				+ " AS " + OrderTransactionTable.COLUMN_TRANS_VAT + ","
				+ " SUM(" + OrderTransactionTable.COLUMN_TRANS_EXCLUDE_VAT + ")"
				+ " AS " + OrderTransactionTable.COLUMN_TRANS_EXCLUDE_VAT + ","
				+ OrderTransactionTable.COLUMN_STATUS_ID + ","
				+ OrderTransactionTable.COLUMN_PAID_TIME + ","
				+ OrderTransactionTable.COLUMN_VOID_TIME + ","
				+ OrderTransactionTable.COLUMN_VOID_STAFF_ID + ","
				+ OrderTransactionTable.COLUMN_VOID_REASON + ","
				+ OrderTransactionTable.COLUMN_RECEIPT_NO + ","
				+ OrderTransactionTable.COLUMN_OPEN_STAFF
				+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS
				+ " WHERE " + OrderTransactionTable.COLUMN_SALE_DATE + "=?"
				+ " AND " + OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?)",
				new String[] {
						saleDate,
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID)
				});
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				trans.setTransactionId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
				trans.setTransactionVatable(cursor.getDouble(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_VATABLE)));
				trans.setTransactionVat(cursor.getDouble(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_VAT)));
				trans.setTransactionVatExclude(cursor.getDouble(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_EXCLUDE_VAT)));
				trans.setComputerId(cursor.getInt(cursor
						.getColumnIndex(ComputerTable.COLUMN_COMPUTER_ID)));
				trans.setTransactionStatusId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_STATUS_ID)));
				trans.setPaidTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_PAID_TIME)));
				trans.setVoidTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_VOID_TIME)));
				trans.setVoidStaffId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_VOID_STAFF_ID)));
				trans.setVoidReason(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_VOID_REASON)));
				trans.setReceiptNo(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_RECEIPT_NO)));
				trans.setOpenStaffId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_OPEN_STAFF)));
			}
			cursor.close();
		}
		return trans;
	}
	
	/**
	 * @param transactionId
	 * @param computerId
	 * @return MPOSOrderTransaction
	 */
	public MPOSOrderTransaction getTransaction(int transactionId) {
		MPOSOrderTransaction trans = new MPOSOrderTransaction();
		Cursor cursor = getReadableDatabase().query(
				OrderTransactionTable.TABLE_ORDER_TRANS,
				new String[] { OrderTransactionTable.COLUMN_TRANSACTION_ID,
						ComputerTable.COLUMN_COMPUTER_ID,
						OrderTransactionTable.COLUMN_TRANS_VATABLE,
						OrderTransactionTable.COLUMN_TRANS_VAT,
						OrderTransactionTable.COLUMN_TRANS_EXCLUDE_VAT,
						OrderTransactionTable.COLUMN_STATUS_ID,
						OrderTransactionTable.COLUMN_PAID_TIME,
						OrderTransactionTable.COLUMN_VOID_TIME,
						OrderTransactionTable.COLUMN_VOID_STAFF_ID,
						OrderTransactionTable.COLUMN_VOID_REASON,
						OrderTransactionTable.COLUMN_RECEIPT_NO,
						OrderTransactionTable.COLUMN_OPEN_STAFF },
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) }, null, null,
				OrderTransactionTable.COLUMN_SALE_DATE);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				trans.setTransactionId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
				trans.setTransactionVatable(cursor.getDouble(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_VATABLE)));
				trans.setTransactionVat(cursor.getDouble(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_VAT)));
				trans.setTransactionVatExclude(cursor.getDouble(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_EXCLUDE_VAT)));
				trans.setComputerId(cursor.getInt(cursor
						.getColumnIndex(ComputerTable.COLUMN_COMPUTER_ID)));
				trans.setTransactionStatusId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_STATUS_ID)));
				trans.setPaidTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_PAID_TIME)));
				trans.setVoidTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_VOID_TIME)));
				trans.setVoidStaffId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_VOID_STAFF_ID)));
				trans.setVoidReason(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_VOID_REASON)));
				trans.setReceiptNo(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_RECEIPT_NO)));
				trans.setOpenStaffId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_OPEN_STAFF)));
			}
			cursor.close();
		}
		return trans;
	}

	/**
	 * Get summary order for discount
	 * 
	 * @param transactionId
	 * @return
	 */
	public MPOSOrderTransaction.MPOSOrderDetail getSummaryOrderForDiscount(
			int transactionId) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = new MPOSOrderTransaction.MPOSOrderDetail();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT SUM (" + OrderDetailTable.COLUMN_ORDER_QTY + ") "
						+ " AS " + OrderDetailTable.COLUMN_ORDER_QTY + ", "
						+ " SUM (" + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ") "
						+ " AS " + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", " 
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", "
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", " 
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_VAT + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_VAT + ", "
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE + ","
						+ "(SELECT SUM(" + ProductTable.COLUMN_PRODUCT_PRICE + "*" + OrderSetTable.COLUMN_ORDER_SET_QTY + ")"
						+ " FROM " + OrderSetTable.TABLE_ORDER_SET
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?) AS TotalSetPrice,"
						+ "( SELECT SUM(" + ProductTable.COLUMN_PRODUCT_PRICE + "*" + OrderDetailTable.COLUMN_ORDER_QTY + ")"
						+ " FROM " + OrderCommentTable.TABLE_ORDER_COMMENT
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?) AS TotalCommentPrice"
						+ " FROM " + OrderDetailTable.TABLE_ORDER_TMP
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { 
						String.valueOf(transactionId),
						String.valueOf(transactionId), 
						String.valueOf(transactionId) });
		if (cursor.moveToFirst()) {
			double totalSetPrice = cursor.getDouble(cursor.getColumnIndex("TotalSetPrice"));
			double totalCommentPrice = cursor.getDouble(cursor.getColumnIndex("TotalCommentPrice"));
			double totalSalePrice = 
					cursor.getDouble(cursor.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)) 
					+ totalSetPrice + totalCommentPrice;
			double totalRetailPrice = 
					cursor.getDouble(cursor.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE))
					+ totalSetPrice + totalCommentPrice;
			orderDetail.setQty(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_ORDER_QTY)));
			orderDetail.setPriceDiscount(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_PRICE_DISCOUNT)));
			orderDetail.setTotalRetailPrice(totalRetailPrice);
			orderDetail.setTotalSalePrice(totalSalePrice);
			orderDetail.setVat(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT)));
			orderDetail
					.setVatExclude(cursor.getDouble(cursor
							.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE)));
		}
		cursor.close();
		return orderDetail;
	}

	/**
	 * Get summary of void order in day
	 * @param saleDate
	 * @return MPOSOrderTransaction.MPOSOrderDetail
	 */
	public MPOSOrderTransaction.MPOSOrderDetail getSummaryVoidOrderInDay(
			String saleDate) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = 
				new MPOSOrderTransaction.MPOSOrderDetail();
		String sql = "SELECT a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
				+ " COUNT(a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ") "
				+ " AS TotalVoid, "
				+ " ( SELECT SUM (" + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ") "
				+ "  FROM " + OrderDetailTable.TABLE_ORDER
				+ "  WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID 
				+ "  =a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ") "
				+ "  AS " + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE
				+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS + " a "
				+ " WHERE a." + OrderTransactionTable.COLUMN_SALE_DATE + "=?"
				+ " AND a." + OrderTransactionTable.COLUMN_STATUS_ID + "=?";
		Cursor cursor = getReadableDatabase().rawQuery(
				sql, new String[] {saleDate, String.valueOf(Transaction.TRANS_STATUS_VOID)});
		if (cursor.moveToFirst()) {
			orderDetail.setQty(cursor.getDouble(cursor
					.getColumnIndex("TotalVoid")));
			orderDetail.setTotalSalePrice(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)));
		}
		cursor.close();
		return orderDetail;
	}
	
	/**
	 * Get summary order by sale date
	 * @param dateFrom
	 * @return MPOSOrderTransaction.MPOSOrderDetail
	 */
	public MPOSOrderTransaction.MPOSOrderDetail getSummaryOrderInDay(
			String dateFrom, String dateTo) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = 
				new MPOSOrderTransaction.MPOSOrderDetail();
		String sql = "SELECT SUM (b." + OrderDetailTable.COLUMN_ORDER_QTY + ") AS " 
				+ OrderDetailTable.COLUMN_ORDER_QTY + ", "
				+ " SUM (b." + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ") AS " 
				+ OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", " 
				+ " SUM (b." + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", "
				+ " SUM (b." + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", " 
				+ " SUM (b." + OrderDetailTable.COLUMN_TOTAL_VAT + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_VAT + ", "
				+ " SUM (b." + OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE
				+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS + " a " 
				+ " LEFT JOIN " + OrderDetailTable.TABLE_ORDER + " b "
				+ " ON a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=b." + OrderTransactionTable.COLUMN_TRANSACTION_ID
				+ " WHERE a." + OrderTransactionTable.COLUMN_SALE_DATE + " BETWEEN ? AND ?"
				+ " AND a." + OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?)";
		Cursor cursor = getReadableDatabase().rawQuery(
				sql, new String[] {
						dateFrom,
						dateTo,
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID)});
		if (cursor.moveToFirst()) {
			orderDetail.setQty(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_ORDER_QTY)));
			orderDetail.setPriceDiscount(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_PRICE_DISCOUNT)));
			orderDetail
					.setTotalRetailPrice(cursor.getDouble(cursor
							.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE)));
			orderDetail.setTotalSalePrice(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)));
			orderDetail.setVat(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT)));
			orderDetail
					.setVatExclude(cursor.getDouble(cursor
							.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE)));
		}
		cursor.close();
		return orderDetail;
	}
	
	/**
	 * Get summary order
	 * 
	 * @param transactionId
	 * @return
	 */
	public MPOSOrderTransaction.MPOSOrderDetail getSummaryOrder(
			int transactionId) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = 
				new MPOSOrderTransaction.MPOSOrderDetail();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT SUM (" + OrderDetailTable.COLUMN_ORDER_QTY + ") "
						+ " AS " + OrderDetailTable.COLUMN_ORDER_QTY + ", "
						+ " SUM (" + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ") "
						+ " AS " + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", " 
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", "
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", " 
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_VAT + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_VAT + ", "
						+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE + ","
						+ "(SELECT SUM(" + ProductTable.COLUMN_PRODUCT_PRICE + "*" + OrderSetTable.COLUMN_ORDER_SET_QTY + ")"
						+ " FROM " + OrderSetTable.TABLE_ORDER_SET
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?) AS TotalSetPrice,"
						+ "( SELECT SUM(" + ProductTable.COLUMN_PRODUCT_PRICE + "*" + OrderDetailTable.COLUMN_ORDER_QTY + ")"
						+ " FROM " + OrderCommentTable.TABLE_ORDER_COMMENT
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?) AS TotalCommentPrice"
						+ " FROM " + OrderDetailTable.TABLE_ORDER 
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?", 
						new String[] { 
							String.valueOf(transactionId),
							String.valueOf(transactionId), 
							String.valueOf(transactionId)});
		if (cursor.moveToFirst()) {
			double totalSetPrice = cursor.getDouble(cursor.getColumnIndex("TotalSetPrice"));
			double totalCommentPrice = cursor.getDouble(cursor.getColumnIndex("TotalCommentPrice"));
			double totalSalePrice = 
					cursor.getDouble(cursor.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)) 
					+ totalSetPrice + totalCommentPrice;
			double totalRetailPrice = 
					cursor.getDouble(cursor.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE))
					+ totalSetPrice + totalCommentPrice;
			orderDetail.setQty(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_ORDER_QTY)));
			orderDetail.setPriceDiscount(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_PRICE_DISCOUNT)));
			orderDetail.setTotalRetailPrice(totalRetailPrice);
			orderDetail.setTotalSalePrice(totalSalePrice);
			orderDetail.setVat(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT)));
			orderDetail
					.setVatExclude(cursor.getDouble(cursor
							.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE)));
		}
		cursor.close();
		return orderDetail;
	}
	
	/**
	 * @param transactionId
	 * @return max total retail price
	 */
	public double getMaxTotalRetailPrice(int transactionId){
		double maxTotalRetailPrice = 0.0d;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT MAX(" + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ")"
				+ " FROM " + OrderDetailTable.TABLE_ORDER
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?", 
				new String[]{
						String.valueOf(transactionId)
				});
		if(cursor.moveToFirst()){
			maxTotalRetailPrice = cursor.getDouble(0);
		}
		cursor.close();
		return maxTotalRetailPrice;
	}

	public String getMaxReceiptNo(String saleDate){
		String receiptNo = "";
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT " + OrderTransactionTable.COLUMN_RECEIPT_NO
				+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS
				+ " WHERE " + OrderTransactionTable.COLUMN_SALE_DATE + "=?" 
				+ " AND " + OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?) "
				+ " ORDER BY " + OrderTransactionTable.COLUMN_TRANSACTION_ID
				+ " DESC LIMIT 1", new String[]{
						saleDate, 
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID)
					});
		if(cursor.moveToFirst()){
			receiptNo = cursor.getString(0);
		}
		cursor.close();
		return receiptNo;
	}
	
	public String getMinReceiptNo(String saleDate){
		String receiptNo = "";
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT " + OrderTransactionTable.COLUMN_RECEIPT_NO
				+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS
				+ " WHERE " + OrderTransactionTable.COLUMN_SALE_DATE + "=?" 
				+ " AND " + OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?) "
				+ " ORDER BY " + OrderTransactionTable.COLUMN_TRANSACTION_ID
				+ " ASC LIMIT 1", new String[]{
						saleDate, 
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID)
					});
		if(cursor.moveToFirst()){
			receiptNo = cursor.getString(0);
		}
		cursor.close();
		return receiptNo;
	}
	
	private MPOSOrderTransaction.MPOSOrderDetail getSummaryOrderIgnoreNoVat(
			int transactionId) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = 
				new MPOSOrderTransaction.MPOSOrderDetail();
		String sql = "SELECT SUM (" + OrderDetailTable.COLUMN_ORDER_QTY + ") AS " 
				+ OrderDetailTable.COLUMN_ORDER_QTY + ", "
				+ " SUM (" + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ") AS " 
				+ OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", " 
				+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", "
				+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", " 
				+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_VAT + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_VAT + ", "
				+ " SUM (" + OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE + ") AS " 
				+ OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE
				+ " FROM " + OrderDetailTable.TABLE_ORDER 
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + ProductTable.COLUMN_VAT_TYPE + " != ?";
		Cursor cursor = getReadableDatabase().rawQuery(
				sql, new String[] { String.valueOf(transactionId), String.valueOf(Products.NO_VAT) });
		if (cursor.moveToFirst()) {
			orderDetail.setQty(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_ORDER_QTY)));
			orderDetail.setPriceDiscount(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_PRICE_DISCOUNT)));
			orderDetail
					.setTotalRetailPrice(cursor.getDouble(cursor
							.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE)));
			orderDetail.setTotalSalePrice(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)));
			orderDetail.setVat(cursor.getDouble(cursor
					.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT)));
			orderDetail
					.setVatExclude(cursor.getDouble(cursor
							.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE)));
		}
		cursor.close();
		return orderDetail;
	}

	/**
	 * @param transactionId
	 * @return List<MPOSOrderTransaction.MPOSOrderDetail>
	 */
	public List<MPOSOrderTransaction.MPOSOrderDetail> listAllOrderForDiscount(
			int transactionId) {
		List<MPOSOrderTransaction.MPOSOrderDetail> orderDetailLst 
			= new ArrayList<MPOSOrderTransaction.MPOSOrderDetail>();
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_TYPE_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_QTY + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_PRICE + ", "
						+ " a." + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", " 
						+ " a." + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", "
						+ " a." + ProductTable.COLUMN_VAT_TYPE + ", "
						+ " a." + OrderDetailTable.COLUMN_MEMBER_DISCOUNT + ", "
						+ " a." + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", "
						+ " a." + OrderDetailTable.COLUMN_DISCOUNT_TYPE + ", "
						+ " a." + BaseColumn.COLUMN_REMARK + ", "
						+ " b." + ProductTable.COLUMN_PRODUCT_NAME 
						+ " FROM " + OrderDetailTable.TABLE_ORDER_TMP + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a."  + ProductTable.COLUMN_PRODUCT_ID
						+ " =b." + ProductTable.COLUMN_PRODUCT_ID
						+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
		if (cursor.moveToFirst()) {
			do {
				orderDetailLst.add(toOrderDetail(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return orderDetailLst;
	}

	/**
	 * @param transactionId
	 * @return List<MPOSOrderTransaction.MPOSOrderDetail>
	 */
	public List<MPOSOrderTransaction.MPOSOrderDetail> listAllOrderGroupByProduct(
			int transactionId) {
		List<MPOSOrderTransaction.MPOSOrderDetail> orderDetailLst = new ArrayList<MPOSOrderTransaction.MPOSOrderDetail>();
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_TYPE_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_ID + ", " 
						+ " SUM(a." + OrderDetailTable.COLUMN_ORDER_QTY + ") "
						+ " AS " + OrderDetailTable.COLUMN_ORDER_QTY + ", " 
						+ " SUM(a." + ProductTable.COLUMN_PRODUCT_PRICE + ") "
						+ " AS " + ProductTable.COLUMN_PRODUCT_PRICE + ", " 
						+ " SUM(a." + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", "
						+ " SUM(a." + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ") "
						+ " AS " + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", " 
						+ " a." + ProductTable.COLUMN_VAT_TYPE + ", " 
						+ " SUM(a." + OrderDetailTable.COLUMN_MEMBER_DISCOUNT + ") "
						+ " AS " + OrderDetailTable.COLUMN_MEMBER_DISCOUNT + ", "
						+ " SUM(a." + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ") "
						+ " AS " + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", " 
						+ " a." + OrderDetailTable.COLUMN_DISCOUNT_TYPE + ", "
						+ " a." + BaseColumn.COLUMN_REMARK + ", "
						+ " b." + ProductTable.COLUMN_PRODUCT_NAME
						+ " FROM " + OrderDetailTable.TABLE_ORDER + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a." + ProductTable.COLUMN_PRODUCT_ID
						+ " =b." + ProductTable.COLUMN_PRODUCT_ID
						+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
						+ " GROUP BY a." + ProductTable.COLUMN_PRODUCT_ID
						+ " ORDER BY a." + OrderDetailTable.COLUMN_ORDER_ID,
				new String[] { String.valueOf(transactionId) });
		if (cursor.moveToFirst()) {
			do {
				orderDetailLst.add(toOrderDetailGroupByProduct(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return orderDetailLst;
	}

	/**
	 * @param transactionId
	 * @return List<MPOSOrderTransaction.MPOSOrderDetail>
	 */
	public List<MPOSOrderTransaction.MPOSOrderDetail> listAllOrder(
			int transactionId) {
		List<MPOSOrderTransaction.MPOSOrderDetail> orderDetailLst = 
				new ArrayList<MPOSOrderTransaction.MPOSOrderDetail>();
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_TYPE_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_QTY + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_PRICE + ", "
						+ " a." + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", " 
						+ " a." + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", "
						+ " a." + ProductTable.COLUMN_VAT_TYPE + ", "
						+ " a." + OrderDetailTable.COLUMN_MEMBER_DISCOUNT + ", "
						+ " a." + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", "
						+ " a." + OrderDetailTable.COLUMN_DISCOUNT_TYPE + ", "
						+ " a." + BaseColumn.COLUMN_REMARK + ", "
						+ " b." + ProductTable.COLUMN_PRODUCT_NAME 
						+ " FROM " + OrderDetailTable.TABLE_ORDER + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a."  + ProductTable.COLUMN_PRODUCT_ID
						+ " =b." + ProductTable.COLUMN_PRODUCT_ID
						+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
		if (cursor.moveToFirst()) {
			do {
				orderDetailLst.add(toOrderDetail(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return orderDetailLst;
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @return MPOSOrderTransaction.MPOSOrderDetail
	 */
	public MPOSOrderTransaction.MPOSOrderDetail getOrder(int transactionId, int orderDetailId){
		MPOSOrderTransaction.MPOSOrderDetail order = 
				new MPOSOrderTransaction.MPOSOrderDetail();
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_TYPE_ID + ", "
						+ " a." + OrderDetailTable.COLUMN_ORDER_QTY + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_PRICE + ", "
						+ " a." + OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE + ", " 
						+ " a." + OrderDetailTable.COLUMN_TOTAL_SALE_PRICE + ", "
						+ " a." + ProductTable.COLUMN_VAT_TYPE + ", "
						+ " a." + OrderDetailTable.COLUMN_MEMBER_DISCOUNT + ", "
						+ " a." + OrderDetailTable.COLUMN_PRICE_DISCOUNT + ", "
						+ " a." + OrderDetailTable.COLUMN_DISCOUNT_TYPE + ", "
						+ " a." + BaseColumn.COLUMN_REMARK + ", "
						+ " b." + ProductTable.COLUMN_PRODUCT_NAME 
						+ " FROM " + OrderDetailTable.TABLE_ORDER + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a."  + ProductTable.COLUMN_PRODUCT_ID
						+ " =b." + ProductTable.COLUMN_PRODUCT_ID
						+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
						+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=?",
				new String[] { String.valueOf(transactionId), String.valueOf(orderDetailId) });
		if(cursor.moveToFirst()){
			order = toOrderDetail(cursor);
		}
		cursor.close();
		return order;
	}
	
	/**
	 * @param cursor
	 * @return MPOSOrderTransaction.MPOSOrderDetail
	 */
	private MPOSOrderTransaction.MPOSOrderDetail toOrderDetailGroupByProduct(Cursor cursor) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = new MPOSOrderTransaction.MPOSOrderDetail();
		orderDetail.setTransactionId(cursor.getInt(cursor
				.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
		orderDetail.setOrderDetailId(cursor.getInt(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_ORDER_ID)));
		orderDetail.setProductId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
		orderDetail.setProductName(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
		orderDetail.setQty(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_ORDER_QTY)));
		orderDetail.setPricePerUnit(cursor.getFloat(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
		orderDetail.setTotalRetailPrice(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE)));
		orderDetail.setTotalSalePrice(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)));
		orderDetail.setVatType(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_VAT_TYPE)));
		orderDetail.setMemberDiscount(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_MEMBER_DISCOUNT)));
		orderDetail.setPriceDiscount(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_PRICE_DISCOUNT)));
		orderDetail.setDiscountType(cursor.getInt(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_DISCOUNT_TYPE)));
		orderDetail.setOrderComment(cursor.getString(cursor
				.getColumnIndex(BaseColumn.COLUMN_REMARK)));
		// generate order set detail
		List<MPOSOrderTransaction.OrderSet.OrderSetDetail> orderSetDetailLst = listOrderSetDetailGroupByProduct(
				orderDetail.getTransactionId(), orderDetail.getOrderDetailId());
		if (orderSetDetailLst.size() > 0) {
			orderDetail.setOrderSetDetailLst(orderSetDetailLst);
		}
		return orderDetail;
	}
	
	/**
	 * @param cursor
	 * @return MPOSOrderTransaction.MPOSOrderDetail
	 */
	private MPOSOrderTransaction.MPOSOrderDetail toOrderDetail(Cursor cursor) {
		MPOSOrderTransaction.MPOSOrderDetail orderDetail = new MPOSOrderTransaction.MPOSOrderDetail();
		orderDetail.setTransactionId(cursor.getInt(cursor
				.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
		orderDetail.setOrderDetailId(cursor.getInt(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_ORDER_ID)));
		orderDetail.setProductId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
		orderDetail.setProductTypeId(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_TYPE_ID)));
		orderDetail.setProductName(cursor.getString(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
		orderDetail.setQty(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_ORDER_QTY)));
		orderDetail.setPricePerUnit(cursor.getFloat(cursor
				.getColumnIndex(ProductTable.COLUMN_PRODUCT_PRICE)));
		orderDetail.setTotalRetailPrice(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE)));
		orderDetail.setTotalSalePrice(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE)));
		orderDetail.setVatType(cursor.getInt(cursor
				.getColumnIndex(ProductTable.COLUMN_VAT_TYPE)));
		orderDetail.setMemberDiscount(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_MEMBER_DISCOUNT)));
		orderDetail.setPriceDiscount(cursor.getFloat(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_PRICE_DISCOUNT)));
		orderDetail.setDiscountType(cursor.getInt(cursor
				.getColumnIndex(OrderDetailTable.COLUMN_DISCOUNT_TYPE)));
		orderDetail.setOrderComment(cursor.getString(cursor
				.getColumnIndex(BaseColumn.COLUMN_REMARK)));

		// generate order set detail
		List<MPOSOrderTransaction.OrderSet.OrderSetDetail> orderSetDetailLst = listOrderSetDetail(
				orderDetail.getTransactionId(), orderDetail.getOrderDetailId());
		if (orderSetDetailLst.size() > 0) {
			orderDetail.setOrderSetDetailLst(orderSetDetailLst);
		}
		// list order comment
		List<MenuComment.Comment> commentLst = listOrderComment(
				orderDetail.getTransactionId(), orderDetail.getOrderDetailId());
		if(commentLst.size() > 0){
			orderDetail.setOrderCommentLst(commentLst);
		}
		return orderDetail;
	}

	public static String formatReceiptNo(int year, int month, int day, int id) {
		String receiptYear = String.format(Locale.US, "%04d", year);
		String receiptMonth = String.format(Locale.US, "%02d", month);
		String receiptDay = String.format(Locale.US, "%02d", day);
		String receiptId = String.format(Locale.US, "%04d", id);
		return receiptDay + receiptMonth + receiptYear + "/" + receiptId;
	}

	/**
	 * @param saleDate
	 * @return List<MPOSOrderTransaction>
	 */
	public List<MPOSOrderTransaction> listTransaction(String saleDate) {
		List<MPOSOrderTransaction> transLst = new ArrayList<MPOSOrderTransaction>();
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT " + OrderTransactionTable.COLUMN_TRANSACTION_ID + ", "
						+ ComputerTable.COLUMN_COMPUTER_ID + ", "
						+ OrderTransactionTable.COLUMN_PAID_TIME + ", "
						+ OrderTransactionTable.COLUMN_TRANS_NOTE + ", "
						+ OrderTransactionTable.COLUMN_RECEIPT_NO + ", "
						+ OrderTransactionTable.COLUMN_STATUS_ID + " FROM "
						+ OrderTransactionTable.TABLE_ORDER_TRANS + " WHERE "
						+ OrderTransactionTable.COLUMN_SALE_DATE + "=? AND "
						+ OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?)"
						+ " ORDER BY " + OrderTransactionTable.COLUMN_TRANSACTION_ID,
				new String[] { saleDate, String.valueOf(TRANS_STATUS_VOID),
						String.valueOf(TRANS_STATUS_SUCCESS) });
		if (cursor.moveToFirst()) {
			do {
				MPOSOrderTransaction trans = new MPOSOrderTransaction();
				trans.setTransactionId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
				trans.setComputerId(cursor.getInt(cursor
						.getColumnIndex(ComputerTable.COLUMN_COMPUTER_ID)));
				trans.setTransactionStatusId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_STATUS_ID)));
				trans.setTransactionNote(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_NOTE)));
				trans.setPaidTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_PAID_TIME)));
				trans.setReceiptNo(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_RECEIPT_NO)));
				transLst.add(trans);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return transLst;
	}

	/**
	 * @param saleDate
	 * @return List<MPOSOrderTransaction>
	 */
	public List<MPOSOrderTransaction> listSuccessTransaction(String saleDate) {
		List<MPOSOrderTransaction> transLst = new ArrayList<MPOSOrderTransaction>();
		Cursor cursor = getReadableDatabase()
				.rawQuery(
						" SELECT "
								+ OrderTransactionTable.COLUMN_TRANSACTION_ID
								+ ", " + ComputerTable.COLUMN_COMPUTER_ID
								+ ", " + OrderTransactionTable.COLUMN_PAID_TIME
								+ ", "
								+ OrderTransactionTable.COLUMN_TRANS_NOTE
								+ ", "
								+ OrderTransactionTable.COLUMN_RECEIPT_NO
								+ " FROM "
								+ OrderTransactionTable.TABLE_ORDER_TRANS
								+ " WHERE "
								+ OrderTransactionTable.COLUMN_SALE_DATE + "=?"
								+ " AND "
								+ OrderTransactionTable.COLUMN_STATUS_ID + "=?",
						new String[] { saleDate,
								String.valueOf(TRANS_STATUS_SUCCESS) });
		if (cursor.moveToFirst()) {
			do {
				MPOSOrderTransaction trans = new MPOSOrderTransaction();
				trans.setTransactionId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
				trans.setComputerId(cursor.getInt(cursor
						.getColumnIndex(ComputerTable.COLUMN_COMPUTER_ID)));
				trans.setTransactionNote(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_NOTE)));
				trans.setPaidTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_PAID_TIME)));
				trans.setReceiptNo(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_RECEIPT_NO)));
				transLst.add(trans);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return transLst;
	}

	/**
	 * @param saleDate
	 * @return List<MPOSOrderTransaction>
	 */
	public List<MPOSOrderTransaction> listHoldOrder(String saleDate) {
		List<MPOSOrderTransaction> transLst = new ArrayList<MPOSOrderTransaction>();
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT a." + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ ", " + " a." + ComputerTable.COLUMN_COMPUTER_ID
						+ ", " + " a." + OrderTransactionTable.COLUMN_OPEN_TIME
						+ ", " + " a."
						+ OrderTransactionTable.COLUMN_TRANS_NOTE + ", "
						+ " b." + StaffTable.COLUMN_STAFF_CODE + ", " + " b."
						+ StaffTable.COLUMN_STAFF_NAME + " FROM "
						+ OrderTransactionTable.TABLE_ORDER_TRANS + " a "
						+ " LEFT JOIN " + StaffTable.TABLE_STAFF + " b "
						+ " ON a." + OrderTransactionTable.COLUMN_OPEN_STAFF
						+ "=" + " b." + StaffTable.COLUMN_STAFF_ID
						+ " WHERE a." + OrderTransactionTable.COLUMN_SALE_DATE
						+ "=?" + " AND a."
						+ OrderTransactionTable.COLUMN_STATUS_ID + "=?",
				new String[] { saleDate, String.valueOf(TRANS_STATUS_HOLD) });
		if (cursor.moveToFirst()) {
			do {
				MPOSOrderTransaction trans = new MPOSOrderTransaction();
				trans.setTransactionId(cursor.getInt(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANSACTION_ID)));
				trans.setComputerId(cursor.getInt(cursor
						.getColumnIndex(ComputerTable.COLUMN_COMPUTER_ID)));
				trans.setTransactionNote(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_TRANS_NOTE)));
				trans.setOpenTime(cursor.getString(cursor
						.getColumnIndex(OrderTransactionTable.COLUMN_OPEN_TIME)));
				trans.setStaffName(cursor.getString(cursor
						.getColumnIndex(StaffTable.COLUMN_STAFF_CODE))
						+ ":"
						+ cursor.getString(cursor
								.getColumnIndex(StaffTable.COLUMN_STAFF_NAME)));
				transLst.add(trans);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return transLst;
	}

	/**
	 * @return max transactionId
	 */
	public int getMaxTransaction() {
		int transactionId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT MAX(" + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ ") " + " FROM "
						+ OrderTransactionTable.TABLE_ORDER_TRANS, null);
		if (cursor.moveToFirst()) {
			transactionId = cursor.getInt(0);
			cursor.moveToNext();
		}
		cursor.close();
		return transactionId + 1;
	}

	/**
	 * @param year
	 * @param month
	 * @return max receiptId
	 */
	public int getMaxReceiptId(String saleDate) {
		int maxReceiptId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT MAX(" + OrderTransactionTable.COLUMN_RECEIPT_ID + ") "
						+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS
						+ " WHERE " + OrderTransactionTable.COLUMN_SALE_DATE + "=?",
				new String[] { 
					saleDate 
				});
		if (cursor.moveToFirst()) {
			maxReceiptId = cursor.getInt(0);
		}
		cursor.close();
		return maxReceiptId + 1;
	}

	/**
	 * Get current transactionId
	 * 
	 * @param saleDate
	 * @return transactionId
	 */
	public int getCurrTransactionId(String saleDate) {
		int transactionId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT " + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS
						+ " WHERE " + OrderTransactionTable.COLUMN_STATUS_ID
						+ "=?" + " AND "
						+ OrderTransactionTable.COLUMN_SALE_DATE + "=?",
				new String[] { String.valueOf(TRANS_STATUS_NEW), saleDate });
		if (cursor.moveToFirst()) {
			if (cursor.getLong(0) != 0)
				transactionId = cursor.getInt(0);
			cursor.moveToNext();
		}
		cursor.close();
		return transactionId;
	}

	/**
	 * @param saleDate
	 * @param shopId
	 * @param computerId
	 * @param sessionId
	 * @param staffId
	 * @param vatRate
	 * @return current transactionId
	 * @throws SQLException
	 */
	public int openTransaction(String saleDate, int shopId, int computerId, int sessionId,
			int staffId, double vatRate) throws SQLException {
		int transactionId = getMaxTransaction();
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(Long.parseLong(saleDate));
		Calendar dateTime = Utils.getCalendar();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_UUID, getUUID());
		cv.put(OrderTransactionTable.COLUMN_TRANSACTION_ID, transactionId);
		cv.put(ComputerTable.COLUMN_COMPUTER_ID, computerId);
		cv.put(ShopTable.COLUMN_SHOP_ID, shopId);
		cv.put(SessionTable.COLUMN_SESS_ID, sessionId);
		cv.put(OrderTransactionTable.COLUMN_OPEN_STAFF, staffId);
		cv.put(DocumentTypeTable.COLUMN_DOC_TYPE, 8);
		cv.put(OrderTransactionTable.COLUMN_OPEN_TIME,
				dateTime.getTimeInMillis());
		cv.put(OrderTransactionTable.COLUMN_SALE_DATE, date.getTimeInMillis());
		cv.put(OrderTransactionTable.COLUMN_RECEIPT_YEAR,
				date.get(Calendar.YEAR));
		cv.put(OrderTransactionTable.COLUMN_RECEIPT_MONTH,
				date.get(Calendar.MONTH) + 1);
		cv.put(ProductTable.COLUMN_VAT_RATE, vatRate);
		long rowId = getWritableDatabase().insertOrThrow(
				OrderTransactionTable.TABLE_ORDER_TRANS, null, cv);
		if (rowId == -1)
			transactionId = 0;
		return transactionId;
	}

	/**
	 * @param transactionId
	 * @param staffId
	 * @return row affected
	 */
	public int closeTransaction(int transactionId, int staffId) {
		Calendar date = Utils.getDate();
		Calendar dateTime = Utils.getCalendar();
		int receiptId = getMaxReceiptId(String.valueOf(date.getTimeInMillis()));
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_STATUS_ID, TRANS_STATUS_SUCCESS);
		cv.put(OrderTransactionTable.COLUMN_RECEIPT_ID, receiptId);
		cv.put(OrderTransactionTable.COLUMN_CLOSE_TIME,
				dateTime.getTimeInMillis());
		cv.put(OrderTransactionTable.COLUMN_PAID_TIME,
				dateTime.getTimeInMillis());
		cv.put(OrderTransactionTable.COLUMN_PAID_STAFF_ID, staffId);
		cv.put(OrderTransactionTable.COLUMN_CLOSE_STAFF, staffId);
		cv.put(OrderTransactionTable.COLUMN_RECEIPT_NO,
				formatReceiptNo(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, 
						date.get(Calendar.DAY_OF_MONTH), receiptId));
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS, cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @return row affected
	 */
	public int prepareTransaction(int transactionId) {
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_STATUS_ID, TRANS_STATUS_NEW);
		cv.put(OrderTransactionTable.COLUMN_TRANS_NOTE, "");
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS, cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	
	/**
	 * Cancel transaction
	 * @param transactionId
	 */
	public void cancelTransaction(int transactionId) {
		cancelOrder(transactionId);
		deleteTransaction(transactionId);
	}

	/**
	 * Delete OrderDetail and OrderSet by transactionId
	 * @param transactionId
	 */
	public void cancelOrder(int transactionId){
		deleteOrderComment(transactionId);
		deleteOrderSet(transactionId);
		deleteOrderDetail(transactionId);
	}
	
	/**
	 * Delete OrderDetail and OrderSet by transactonId and orderDetailId
	 * @param transactionId
	 * @param orderDetailId
	 */
	public void deleteOrder(int transactionId, int orderDetailId){
		deleteOrderComment(transactionId, orderDetailId);
		deleteOrderSet(transactionId, orderDetailId);
		deleteOrderDetail(transactionId, orderDetailId);
	}
	
	/**
	 * @param transactionId
	 * @return row affected
	 */
	public int deleteTransaction(int transactionId) {
		return getWritableDatabase().delete(
				OrderTransactionTable.TABLE_ORDER_TRANS,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @return total transaction that not sent
	 */
	public int countTransNotSend() {
		int total = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT COUNT(" + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ ") " + " FROM "
						+ OrderTransactionTable.TABLE_ORDER_TRANS + " WHERE "
						+ OrderTransactionTable.COLUMN_STATUS_ID + "=? AND "
						+ COLUMN_SEND_STATUS + "=?",
				new String[] {
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(MPOSDatabase.NOT_SEND) });
		if (cursor.moveToFirst()) {
			total = cursor.getInt(0);
		}
		cursor.close();
		return total;
	}

	/**
	 * @param saleDate
	 * @return number of hold transaction
	 */
	public int countHoldOrder(String saleDate) {
		int total = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT COUNT(" + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ ") " + " FROM "
						+ OrderTransactionTable.TABLE_ORDER_TRANS + " WHERE "
						+ OrderTransactionTable.COLUMN_STATUS_ID + "=?"
						+ " AND " + OrderTransactionTable.COLUMN_SALE_DATE
						+ "=?",
				new String[] { String.valueOf(TRANS_STATUS_HOLD), saleDate });
		if (cursor.moveToFirst()) {
			total = cursor.getInt(0);
		}
		cursor.close();
		return total;
	}

	/**
	 * @param transactionId
	 * @param note
	 * @return row affected
	 */
	public int holdTransaction(int transactionId, String note) {
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_STATUS_ID, TRANS_STATUS_HOLD);
		cv.put(OrderTransactionTable.COLUMN_TRANS_NOTE, note);
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS, cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @param staffId
	 * @return row affected
	 */
	public int updateTransaction(int transactionId, int staffId) {
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_OPEN_STAFF, staffId);
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS, cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @return row affected
	 */
	public int updateTransactionSendStatus(int transactionId, int status) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_SEND_STATUS, status);
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS,
				cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?" + " AND "
						+ OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?) ",
				new String[] { String.valueOf(transactionId),
						String.valueOf(TRANS_STATUS_SUCCESS),
						String.valueOf(TRANS_STATUS_VOID) });
	}

	/**
	 * @param saleDate
	 * @return row affected
	 */
	public int updateTransactionSendStatus(String saleDate, int status) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_SEND_STATUS, status);
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS,
				cv,
				OrderTransactionTable.COLUMN_SALE_DATE + "=?" + " AND "
						+ OrderTransactionTable.COLUMN_STATUS_ID + " IN(?,?) ",
				new String[] { saleDate, String.valueOf(TRANS_STATUS_SUCCESS),
						String.valueOf(TRANS_STATUS_VOID) });
	}

	/**
	 * Update after payment
	 * @param transactionId
	 * @param totalPayment
	 * @param vatRate
	 * @return rows affected
	 */
	public int updateTransactionVatable(int transactionId, double totalPayment, double vatRate, int vatType){
		double vatable = Utils.calculateVatPrice(totalPayment, vatRate, vatType); 
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_TRANS_VATABLE, vatable);
		return getWritableDatabase().update(OrderTransactionTable.TABLE_ORDER_TRANS, cv, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?", new String[]{String.valueOf(transactionId)});
	}
	
	/**
	 * @param transactionId
	 * @param totalSalePrice
	 * @return row affected
	 */
	protected int updateTransactionVat(int transactionId) {
		MPOSOrderTransaction.MPOSOrderDetail summOrder = getSummaryOrderIgnoreNoVat(transactionId);
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_TRANS_VAT, summOrder.getVat());
		cv.put(OrderTransactionTable.COLUMN_TRANS_EXCLUDE_VAT, summOrder.getVatExclude());
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS, cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @return rows affected
	 */
	public int summary(int transactionId) {
		return updateTransactionVat(transactionId);
	}

	/**
	 * @param transactionId
	 * @return can confirm discount ?
	 */
	public boolean confirmDiscount(int transactionId) {
		boolean isSuccess = false;
		getWritableDatabase().beginTransaction();
		try {
			deleteOrderDetail(transactionId);
			try {
				getWritableDatabase().execSQL(
						" INSERT INTO " + OrderDetailTable.TABLE_ORDER
								+ " SELECT * FROM "
								+ OrderDetailTable.TABLE_ORDER_TMP + " WHERE "
								+ OrderTransactionTable.COLUMN_TRANSACTION_ID + "="
								+ transactionId);
				isSuccess = true;
				getWritableDatabase().setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			getWritableDatabase().endTransaction();
		}
		return isSuccess;
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param vatType
	 * @param vatRate
	 * @param salePrice
	 * @param discount
	 * @param discountType
	 * @return row affected
	 */
	public int discountEatchProduct(int transactionId, int orderDetailId,
			int vatType, double vatRate, double salePrice, double discount,
			int discountType) {
		double vat = Utils.calculateVatAmount(salePrice, vatRate, vatType);
		ContentValues cv = new ContentValues();
		cv.put(OrderDetailTable.COLUMN_PRICE_DISCOUNT, discount);
		cv.put(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE, salePrice);
		cv.put(OrderDetailTable.COLUMN_TOTAL_VAT, vat);
		if (vatType == Products.VAT_TYPE_EXCLUDE)
			cv.put(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE, vat);
		cv.put(OrderDetailTable.COLUMN_DISCOUNT_TYPE, discountType);
		return getWritableDatabase().update(
				OrderDetailTable.TABLE_ORDER_TMP,
				cv,
				OrderDetailTable.COLUMN_ORDER_ID + "=? " + " AND "
						+ OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(orderDetailId),
						String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @return row affected
	 */
	private int deleteOrderDetail(int transactionId) {
		return getWritableDatabase().delete(OrderDetailTable.TABLE_ORDER,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @return row affected
	 */
	private int deleteOrderDetail(int transactionId, int orderDetailId) {
		return getWritableDatabase().delete(
				OrderDetailTable.TABLE_ORDER,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=?",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });
	}

	/**
	 * @param transactionId
	 * @return true if create temp table successfully
	 */
	public boolean prepareDiscount(int transactionId) {
		boolean isSuccess = false;
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + OrderDetailTable.TABLE_ORDER_TMP);
			getWritableDatabase().execSQL(
					" CREATE TABLE " + OrderDetailTable.TABLE_ORDER_TMP
							+ " AS SELECT * FROM " + OrderDetailTable.TABLE_ORDER
							+ " WHERE "
							+ OrderTransactionTable.COLUMN_TRANSACTION_ID + "="
							+ transactionId);
			isSuccess = true;
			getWritableDatabase().setTransactionSuccessful();
		} finally{
			isSuccess = false;
			getWritableDatabase().endTransaction();
		}
		return isSuccess;
	}
	
	/**
	 * @param transactionId
	 * @return row affected
	 */
	public int cancelDiscount(int transactionId) {
		return getWritableDatabase().delete(OrderDetailTable.TABLE_ORDER_TMP,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param orderComment
	 */
	public void updateOrderComment(int transactionId, int orderDetailId, String orderComment){
		ContentValues cv = new ContentValues();
		cv.put(BaseColumn.COLUMN_REMARK, orderComment);
		getWritableDatabase().update(OrderDetailTable.TABLE_ORDER, cv, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?", 
				new String[]{
					String.valueOf(transactionId), 
					String.valueOf(orderDetailId)
				});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param vatRate
	 * @param vatType
	 * @param totalPrice
	 * @return rows affected
	 */
	public int updateOrderDetail(int transactionId, int orderDetailId, 
			double vatRate, int vatType, double totalPrice){
		double vat = Utils.calculateVatAmount(totalPrice, vatRate, vatType);
		ContentValues cv = new ContentValues();
		cv.put(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE, totalPrice);
		cv.put(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE, totalPrice);
		cv.put(OrderDetailTable.COLUMN_TOTAL_VAT, vat);
		if (vatType == Products.VAT_TYPE_EXCLUDE)
			cv.put(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE, vat);
		return getWritableDatabase().update(OrderDetailTable.TABLE_ORDER, cv, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId)
				});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param vatType
	 * @param vatRate
	 * @param orderQty
	 * @param pricePerUnit
	 * @return row affected
	 */
	public int updateOrderDetail(int transactionId, int orderDetailId,
			int vatType, double vatRate, double orderQty, double pricePerUnit) {
		double totalRetailPrice = pricePerUnit * orderQty;
		double vat = Utils
				.calculateVatAmount(totalRetailPrice, vatRate, vatType);
		ContentValues cv = new ContentValues();
		cv.put(OrderDetailTable.COLUMN_ORDER_QTY, orderQty);
		cv.put(ProductTable.COLUMN_PRODUCT_PRICE, pricePerUnit);
		cv.put(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE, totalRetailPrice);
		cv.put(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE, totalRetailPrice);
		cv.put(OrderDetailTable.COLUMN_TOTAL_VAT, vat);
		if (vatType == Products.VAT_TYPE_EXCLUDE)
			cv.put(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE, vat);
		cv.put(OrderDetailTable.COLUMN_PRICE_DISCOUNT, 0);
		return getWritableDatabase().update(
				OrderDetailTable.TABLE_ORDER,
				cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=? ",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });
	}

	/**
	 * @param transactionId
	 * @param computerId
	 * @param productId
	 * @param productCode
	 * @param productName
	 * @param productType
	 * @param vatType
	 * @param vatRate
	 * @param orderQty
	 * @param pricePerUnit
	 * @return current orderDetailId
	 */
	public int addOrderDetail(int transactionId, int computerId, 
			int productId, int productType, int vatType, double vatRate, 
			double orderQty, double pricePerUnit) {

//		int orderDetailId = checkAddedOrderDetail(transactionId, productId); 
//		if(orderDetailId > 0){
//			double totalAdded = getTotalAddedOrder(transactionId, productId) + orderQty;
//			updateOrderDetail(transactionId, orderDetailId, 
//					vatType, vatRate, totalAdded, pricePerUnit);
//		}else{
		double totalRetailPrice = pricePerUnit * orderQty;
		double vat = Utils
				.calculateVatAmount(totalRetailPrice, vatRate, vatType);
		int orderDetailId = getMaxOrderDetail(transactionId);
		ContentValues cv = new ContentValues();
		cv.put(OrderDetailTable.COLUMN_ORDER_ID, orderDetailId);
		cv.put(OrderTransactionTable.COLUMN_TRANSACTION_ID, transactionId);
		cv.put(ComputerTable.COLUMN_COMPUTER_ID, computerId);
		cv.put(ProductTable.COLUMN_PRODUCT_ID, productId);
		cv.put(OrderDetailTable.COLUMN_ORDER_QTY, orderQty);
		cv.put(ProductTable.COLUMN_PRODUCT_PRICE, pricePerUnit);
		cv.put(OrderDetailTable.COLUMN_TOTAL_RETAIL_PRICE, totalRetailPrice);
		cv.put(OrderDetailTable.COLUMN_TOTAL_SALE_PRICE, totalRetailPrice);
		cv.put(ProductTable.COLUMN_VAT_TYPE, vatType);
		cv.put(OrderDetailTable.COLUMN_TOTAL_VAT, vat);
		cv.put(ProductTable.COLUMN_PRODUCT_TYPE_ID, productType);
		if (vatType == Products.VAT_TYPE_EXCLUDE)
			cv.put(OrderDetailTable.COLUMN_TOTAL_VAT_EXCLUDE, vat);
		long rowId = getWritableDatabase().insertOrThrow(
				OrderDetailTable.TABLE_ORDER, null, cv);
		if (rowId == -1)
			orderDetailId = 0;	
//		}
		return orderDetailId;
	}

	private int checkAddedOrderDetail(int transactionId, int productId){
		int orderDetailId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT " + OrderDetailTable.COLUMN_ORDER_ID
				+ " FROM " + OrderDetailTable.TABLE_ORDER
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + ProductTable.COLUMN_PRODUCT_ID + "=?", 
				new String[]{String.valueOf(transactionId), String.valueOf(productId)});
		if(cursor.moveToFirst()){
			orderDetailId = cursor.getInt(0);
		}
		cursor.close();
		return orderDetailId;
	}
	
	private double getTotalAddedOrder(int transactionId, int productId){
		double totalAdded = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT SUM(" + OrderDetailTable.COLUMN_ORDER_QTY + ")"
				+ " FROM " + OrderDetailTable.TABLE_ORDER
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + ProductTable.COLUMN_PRODUCT_ID + "=?", 
				new String[]{String.valueOf(transactionId), String.valueOf(productId)});
		if(cursor.moveToFirst()){
			totalAdded = cursor.getDouble(0);
		}
		cursor.close();
		return totalAdded;
	}
	
	/**
	 * @param transactionId
	 * @return max orderDetailId
	 */
	public int getMaxOrderDetail(int transactionId) {
		int orderDetailId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT MAX(" + OrderDetailTable.COLUMN_ORDER_ID + ") "
						+ " FROM " + OrderDetailTable.TABLE_ORDER + " WHERE "
						+ OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?",
				new String[] { String.valueOf(transactionId) });
		if (cursor.moveToFirst()) {
			orderDetailId = cursor.getInt(0);
		}
		cursor.close();
		return orderDetailId + 1;
	}

	/**
	 * @param transactionId
	 * @param staffId
	 * @param reason
	 * @return row affected
	 */
	public int voidTransaction(int transactionId, int staffId, String reason) {
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_STATUS_ID, TRANS_STATUS_VOID);
		cv.put(OrderTransactionTable.COLUMN_VOID_STAFF_ID, staffId);
		cv.put(OrderTransactionTable.COLUMN_VOID_REASON, reason);
		cv.put(COLUMN_SEND_STATUS, MPOSDatabase.NOT_SEND);
		cv.put(OrderTransactionTable.COLUMN_VOID_TIME, Utils.getCalendar()
				.getTimeInMillis());
		return getWritableDatabase().update(
				OrderTransactionTable.TABLE_ORDER_TRANS, cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? ",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param sessionDate
	 * @return total receipt specific by sale date
	 */
	public int getTotalReceipt(String sessionDate) {
		int totalReceipt = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT COUNT (" + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ ") " + " FROM "
						+ OrderTransactionTable.TABLE_ORDER_TRANS + " WHERE "
						+ OrderTransactionTable.COLUMN_SALE_DATE + "=? "
						+ " AND " + OrderTransactionTable.COLUMN_STATUS_ID
						+ " IN (?,?)",
				new String[] { sessionDate,
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID) });
		if (cursor.moveToFirst()) {
			totalReceipt = cursor.getInt(0);
		}
		cursor.close();
		return totalReceipt;
	}

	/**
	 * @param sessionDate
	 * @return total receipt amount
	 */
	public double getTotalReceiptAmount(String sessionDate) {
		double totalReceiptAmount = 0.0f;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT "
				+ " SUM (" + OrderTransactionTable.COLUMN_TRANS_VATABLE + ") " 
				+ " FROM " + OrderTransactionTable.TABLE_ORDER_TRANS 
				+ " WHERE " + OrderTransactionTable.COLUMN_SALE_DATE + "=? "
				+ " AND " + OrderTransactionTable.COLUMN_STATUS_ID
				+ " IN(?,?)",
				new String[] { sessionDate,
						String.valueOf(Transaction.TRANS_STATUS_SUCCESS),
						String.valueOf(Transaction.TRANS_STATUS_VOID)});
		if (cursor.moveToFirst()) {
			totalReceiptAmount = cursor.getFloat(0);
		}
		cursor.close();
		return totalReceiptAmount;
	}

	/**
	 * Orders Set
	 * 
	 * @param transactionId
	 * @param orderDetailId
	 * @return List<MPOSOrderTransaction.OrderSet>
	 */
	public List<MPOSOrderTransaction.OrderSet> listOrderSet(int transactionId,
			int orderDetailId) {
		List<MPOSOrderTransaction.OrderSet> productSetLst = new ArrayList<MPOSOrderTransaction.OrderSet>();
		Cursor mainCursor = getReadableDatabase().rawQuery(
				" SELECT b." + ProductComponentTable.COLUMN_PGROUP_ID + ", "
						+ " b." + ProductComponentGroupTable.COLUMN_SET_GROUP_NO + ", "
						+ " b." + ProductComponentGroupTable.COLUMN_SET_GROUP_NAME + ", " 
						+ " b." + ProductComponentGroupTable.COLUMN_REQ_AMOUNT
						+ " FROM " + OrderSetTable.TABLE_ORDER_SET + " a "
						+ " LEFT JOIN " + ProductComponentGroupTable.TABLE_PCOMPONENT_GROUP + " b " 
						+ " ON a." + ProductComponentTable.COLUMN_PGROUP_ID 
						+ "=b." + ProductComponentTable.COLUMN_PGROUP_ID 
						+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? "
						+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=?"
						+ " GROUP BY b." + ProductComponentTable.COLUMN_PGROUP_ID,
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });

		if (mainCursor.moveToFirst()) {
			do {
				int pcompGroupId = mainCursor
						.getInt(mainCursor
								.getColumnIndex(ProductComponentTable.COLUMN_PGROUP_ID));
				MPOSOrderTransaction.OrderSet group = new MPOSOrderTransaction.OrderSet();
				group.setTransactionId(transactionId);
				group.setOrderDetailId(orderDetailId);
				group.setProductGroupId(pcompGroupId);
				group.setGroupNo(mainCursor.getInt(mainCursor
						.getColumnIndex(ProductComponentGroupTable.COLUMN_SET_GROUP_NO)));
				group.setGroupName(mainCursor.getString(mainCursor
						.getColumnIndex(ProductComponentGroupTable.COLUMN_SET_GROUP_NAME)));
				group.setRequireAmount(mainCursor.getDouble(mainCursor
						.getColumnIndex(ProductComponentGroupTable.COLUMN_REQ_AMOUNT)));
				// query set detail
				Cursor detailCursor = getReadableDatabase()
						.rawQuery("SELECT a." + OrderSetTable.COLUMN_ORDER_SET_ID + ","
								+ " a." + ProductTable.COLUMN_PRODUCT_ID + ","
								+ " a." + OrderSetTable.COLUMN_ORDER_SET_PRICE + ", "
								+ " b." + ProductTable.COLUMN_PRODUCT_NAME + ", "
								+ " a." + OrderSetTable.COLUMN_ORDER_SET_QTY 
								+ " FROM " + OrderSetTable.TABLE_ORDER_SET + " a "
								+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
								+ " ON a." + ProductTable.COLUMN_PRODUCT_ID
								+ " =b." + ProductTable.COLUMN_PRODUCT_ID
								+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? "
								+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=? "
								+ " AND a." + ProductComponentTable.COLUMN_PGROUP_ID + "=?",
								new String[] { String.valueOf(transactionId),
										String.valueOf(orderDetailId),
										String.valueOf(pcompGroupId) });

				if (detailCursor.moveToFirst()) {
					do {
						MPOSOrderTransaction.OrderSet.OrderSetDetail detail = new MPOSOrderTransaction.OrderSet.OrderSetDetail();
						detail.setOrderSetId(detailCursor.getInt(
								detailCursor.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_ID)));
						detail.setProductId(detailCursor.getInt(
								detailCursor.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
						detail.setProductName(detailCursor.getString(
								detailCursor.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
						detail.setOrderSetQty(detailCursor.getDouble(
								detailCursor.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_QTY)));
						detail.setProductPrice(detailCursor.getColumnIndex(
								OrderSetTable.COLUMN_ORDER_SET_PRICE));
						group.getOrderSetDetailLst().add(detail);
					} while (detailCursor.moveToNext());
				}
				detailCursor.close();

				// productSet to list
				productSetLst.add(group);

			} while (mainCursor.moveToNext());
		}
		mainCursor.close();

		return productSetLst;
	}

	public List<OrderSet.OrderSetDetail> listOrderSetDetailGroupByProduct(int transactionId,
			int orderDetailId) {
		List<OrderSet.OrderSetDetail> orderSetDetailLst = new ArrayList<OrderSet.OrderSetDetail>();
		// query set detail
		Cursor detailCursor = getReadableDatabase().rawQuery(
				"SELECT a." + OrderSetTable.COLUMN_ORDER_SET_ID + ", "
						+ " a." + ProductTable.COLUMN_PRODUCT_ID + ", " 
						+ " b." + ProductTable.COLUMN_PRODUCT_NAME + ", "
						+ " SUM (a." + OrderSetTable.COLUMN_ORDER_SET_QTY + ") AS " + OrderSetTable.COLUMN_ORDER_SET_QTY + ", "
						+ " SUM (a." + OrderSetTable.COLUMN_ORDER_SET_PRICE + ") AS " + OrderSetTable.COLUMN_ORDER_SET_PRICE
						+ " FROM " + OrderSetTable.TABLE_ORDER_SET + " a "
						+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
						+ " ON a." + ProductTable.COLUMN_PRODUCT_ID
						+ " =b." + ProductTable.COLUMN_PRODUCT_ID
						+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
						+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=? "
						+ " GROUP BY a." + ProductTable.COLUMN_PRODUCT_ID,
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });

		if (detailCursor.moveToFirst()) {
			do {
				MPOSOrderTransaction.OrderSet.OrderSetDetail detail = new MPOSOrderTransaction.OrderSet.OrderSetDetail();
				detail.setOrderSetId(detailCursor.getInt(detailCursor
						.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_ID)));
				detail.setProductId(detailCursor.getInt(detailCursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
				detail.setProductName(detailCursor.getString(detailCursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
				detail.setOrderSetQty(detailCursor.getDouble(detailCursor
						.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_QTY)));
				detail.setProductPrice(detailCursor.getDouble(detailCursor
						.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_PRICE)));
				orderSetDetailLst.add(detail);
			} while (detailCursor.moveToNext());
		}
		detailCursor.close();
		return orderSetDetailLst;
	}
	
	/**
	 * List order set detail
	 * 
	 * @param transactionId
	 * @param orderDetailId
	 * @return List<OrderSet.OrderSetDetail
	 */
	public List<OrderSet.OrderSetDetail> listOrderSetDetail(int transactionId,
			int orderDetailId) {
		List<OrderSet.OrderSetDetail> orderSetDetailLst = 
				new ArrayList<OrderSet.OrderSetDetail>();
		// query set detail
		Cursor detailCursor = getReadableDatabase().rawQuery(
				"SELECT a." + OrderSetTable.COLUMN_ORDER_SET_ID + ","
				+ " a." + ProductTable.COLUMN_PRODUCT_ID + ","
				+ " b." + ProductTable.COLUMN_PRODUCT_NAME + ","
				+ " a." + OrderSetTable.COLUMN_ORDER_SET_QTY + ","
				+ " a." + OrderSetTable.COLUMN_ORDER_SET_PRICE
				+ " FROM " + OrderSetTable.TABLE_ORDER_SET + " a "
				+ " LEFT JOIN " + ProductTable.TABLE_PRODUCT + " b "
				+ " ON a." + ProductTable.COLUMN_PRODUCT_ID 
				+ "=b." + ProductTable.COLUMN_PRODUCT_ID
				+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? " 
				+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=? ",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });

		if (detailCursor.moveToFirst()) {
			do {
				MPOSOrderTransaction.OrderSet.OrderSetDetail detail = 
						new MPOSOrderTransaction.OrderSet.OrderSetDetail();
				detail.setOrderSetId(detailCursor.getInt(detailCursor
						.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_ID)));
				detail.setProductId(detailCursor.getInt(detailCursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_ID)));
				detail.setProductName(detailCursor.getString(detailCursor
						.getColumnIndex(ProductTable.COLUMN_PRODUCT_NAME)));
				detail.setOrderSetQty(detailCursor.getDouble(detailCursor
						.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_QTY)));
				detail.setProductPrice(detailCursor.getDouble(detailCursor
						.getColumnIndex(OrderSetTable.COLUMN_ORDER_SET_PRICE)));
				orderSetDetailLst.add(detail);
			} while (detailCursor.moveToNext());
		}
		detailCursor.close();
		return orderSetDetailLst;
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param pcompGroupId
	 * @return total qty of group
	 */
	public double getOrderSetTotalQty(int transactionId, int orderDetailId,
			int pcompGroupId) {
		double totalQty = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				" SELECT SUM(" + OrderSetTable.COLUMN_ORDER_SET_QTY + ") "
						+ " FROM " + OrderSetTable.TABLE_ORDER_SET 
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? "
						+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=? "
						+ " AND " + ProductComponentTable.COLUMN_PGROUP_ID + "=? ",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId),
						String.valueOf(pcompGroupId) });

		if (cursor.moveToFirst()) {
			totalQty = cursor.getDouble(0);
		}
		cursor.close();
		return totalQty;
	}

	/**
	 * @param transactionId
	 */
	private void deleteOrderSet(int transactionId) {
		getWritableDatabase().delete(OrderSetTable.TABLE_ORDER_SET,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? ",
				new String[] { String.valueOf(transactionId) });
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 */
	public void deleteOrderSet(int transactionId, int orderDetailId) {
		getWritableDatabase().delete(
				OrderSetTable.TABLE_ORDER_SET,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? " + " AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=? ",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param orderSetId
	 */
	public void deleteOrderSet(int transactionId, int orderDetailId,
			int orderSetId) {
		getWritableDatabase().delete(
				OrderSetTable.TABLE_ORDER_SET,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? " + " AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=? " + " AND "
						+ OrderSetTable.COLUMN_ORDER_SET_ID + "=?",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId),
						String.valueOf(orderSetId) });
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param pCompGroupId
	 */
	public void deleteOrderSetByGroup(int transactionId, int orderDetailId,
			int pCompGroupId) {
		getWritableDatabase().delete(
				OrderSetTable.TABLE_ORDER_SET,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? " + " AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=? " + " AND "
						+ ProductComponentTable.COLUMN_PGROUP_ID + "=?",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId),
						String.valueOf(pCompGroupId) });
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param orderSetId
	 * @param productId
	 * @param orderSetQty
	 */
	public void updateOrderSet(int transactionId, int orderDetailId,
			int orderSetId, int productId, double orderSetQty) {
		ContentValues cv = new ContentValues();
		cv.put(OrderSetTable.COLUMN_ORDER_SET_QTY, orderSetQty);
		getWritableDatabase().update(
				OrderSetTable.TABLE_ORDER_SET,
				cv,
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=? " + " AND "
						+ OrderDetailTable.COLUMN_ORDER_ID + "=? " + " AND "
						+ OrderSetTable.COLUMN_ORDER_SET_ID + "=?",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId),
						String.valueOf(orderSetId) });
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param productId
	 * @param productName
	 * @param pcompGroupId
	 * @param reqAmount
	 */
	public void addOrderSet(int transactionId, int orderDetailId,
			int productId, double orderSetQty, double productPrice, 
			int pcompGroupId, double reqAmount) {
		int maxOrderSetId = getMaxOrderSetId(transactionId, orderDetailId);
		ContentValues cv = new ContentValues();
		cv.put(OrderSetTable.COLUMN_ORDER_SET_ID, maxOrderSetId);
		cv.put(OrderTransactionTable.COLUMN_TRANSACTION_ID, transactionId);
		cv.put(OrderDetailTable.COLUMN_ORDER_ID, orderDetailId);
		cv.put(ProductTable.COLUMN_PRODUCT_ID, productId);
		cv.put(OrderSetTable.COLUMN_ORDER_SET_PRICE, productPrice);
		cv.put(ProductComponentTable.COLUMN_PGROUP_ID, pcompGroupId);
		cv.put(ProductComponentGroupTable.COLUMN_REQ_AMOUNT, reqAmount);
		cv.put(OrderSetTable.COLUMN_ORDER_SET_QTY, orderSetQty);
		getWritableDatabase().insertOrThrow(OrderSetTable.TABLE_ORDER_SET,
				ProductTable.COLUMN_PRODUCT_NAME, cv);
	}

	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param productGroupId
	 * @return rows
	 */
	public int checkAddedOrderSet(int transactionId, int orderDetailId, int productGroupId){
		int added = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT COUNT(*) "
				+ " FROM " + OrderSetTable.TABLE_ORDER_SET
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=? "
				+ " AND " + ProductComponentTable.COLUMN_PGROUP_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId),
					String.valueOf(productGroupId)
				});
		if(cursor.moveToFirst()){
			added = cursor.getInt(0);
		}
		cursor.close();
		return added;
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @return max orderSetId 0 if no row
	 */
	public int getMaxOrderSetId(int transactionId, int orderDetailId) {
		int maxOrderSetId = 0;
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT MAX (" + OrderSetTable.COLUMN_ORDER_SET_ID + ")"
						+ " FROM " + OrderSetTable.TABLE_ORDER_SET + " "
						+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID
						+ " =? AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?",
				new String[] { String.valueOf(transactionId),
						String.valueOf(orderDetailId) });
		if (cursor.moveToFirst()) {
			maxOrderSetId = cursor.getInt(0);
		}
		cursor.close();
		return maxOrderSetId + 1;
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @return List<MenuComment.Comment> 
	 */
	public List<MenuComment.Comment> listOrderComment(int transactionId, int orderDetailId){
		List<MenuComment.Comment> orderCommentLst =
				new ArrayList<MenuComment.Comment>();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT a." + OrderCommentTable.COLUMN_ORDER_COMMENT_QTY + ", "
				+ " a." + OrderCommentTable.COLUMN_ORDER_COMMENT_PRICE + ", "
				+ " b." + MenuCommentTable.COLUMN_COMMENT_NAME
 				+ " FROM " + OrderCommentTable.TABLE_ORDER_COMMENT + " a "
				+ " LEFT JOIN " + MenuCommentTable.TABLE_MENU_COMMENT + " b "
				+ " ON a." + MenuCommentTable.COLUMN_COMMENT_ID
				+ " =b." + MenuCommentTable.COLUMN_COMMENT_ID
				+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId)
				});
		if(cursor.moveToFirst()){
			do{
				MenuComment.Comment comment = new MenuComment.Comment();
				comment.setCommentName(cursor.getString(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_NAME)));
				comment.setCommentQty(cursor.getDouble(
						cursor.getColumnIndex(OrderCommentTable.COLUMN_ORDER_COMMENT_QTY)));
				comment.setCommentPrice(cursor.getDouble(
						cursor.getColumnIndex(OrderCommentTable.COLUMN_ORDER_COMMENT_PRICE)));
				orderCommentLst.add(comment);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return orderCommentLst;
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param commentId
	 * @return MenuComment.Comment
	 */
	public MenuComment.Comment getOrderComment(int transactionId, int orderDetailId, int commentId){
		MenuComment.Comment orderComment = new MenuComment.Comment();
		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT a." + MenuCommentTable.COLUMN_COMMENT_ID + ", "
				+ " a." + OrderCommentTable.COLUMN_ORDER_COMMENT_QTY + ", "
				+ " a." + OrderCommentTable.COLUMN_ORDER_COMMENT_PRICE + ", "
				+ " b." + MenuCommentTable.COLUMN_COMMENT_NAME
				+ " FROM " + OrderCommentTable.TEMP_ORDER_COMMENT + " a "
				+ " LEFT JOIN " + MenuCommentTable.TABLE_MENU_COMMENT + " b "
				+ " ON a." + MenuCommentTable.COLUMN_COMMENT_ID
				+ " =b." + MenuCommentTable.COLUMN_COMMENT_ID
				+ " WHERE a." + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND a." + OrderDetailTable.COLUMN_ORDER_ID + "=?"
				+ " AND a." + MenuCommentTable.COLUMN_COMMENT_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId),
					String.valueOf(commentId)
				});
		if(cursor.moveToFirst()){
			orderComment.setCommentId(cursor.getInt(
					cursor.getColumnIndex( MenuCommentTable.COLUMN_COMMENT_ID )));
			orderComment.setCommentName(cursor.getString(
						cursor.getColumnIndex(MenuCommentTable.COLUMN_COMMENT_NAME)));
			orderComment.setCommentQty(cursor.getDouble(
						cursor.getColumnIndex(OrderCommentTable.COLUMN_ORDER_COMMENT_QTY)));
			orderComment.setCommentPrice(cursor.getDouble(
						cursor.getColumnIndex(OrderCommentTable.COLUMN_ORDER_COMMENT_PRICE)));
		}
		cursor.close();
		return orderComment;
	}
	
	/**
	 * Confirm order comment
	 * @param transactionId
	 * @param orderDetailId
	 */
	public void confirmOrderComment(int transactionId, int orderDetailId){
		getWritableDatabase().delete(OrderCommentTable.TABLE_ORDER_COMMENT, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId)
				});
		getWritableDatabase().execSQL("INSERT INTO " + OrderCommentTable.TABLE_ORDER_COMMENT
				+ " SELECT * FROM " + OrderCommentTable.TEMP_ORDER_COMMENT
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=" + transactionId
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=" + orderDetailId);
	}
	
	/**
	 * @param transactionId
	 */
	public void deleteOrderComment(int transactionId){
		getWritableDatabase().delete(OrderCommentTable.TABLE_ORDER_COMMENT, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?", 
			new String[]{
				String.valueOf(transactionId)
			});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 */
	public void deleteOrderComment(int transactionId, int orderDetailId){
		getWritableDatabase().delete(OrderCommentTable.TABLE_ORDER_COMMENT, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?", 
			new String[]{
				String.valueOf(transactionId),
				String.valueOf(orderDetailId)
			});
	}
	
	/**
	 * @param transactionId
	 */
	public void deleteOrderCommentTemp(int transactionId){
		getWritableDatabase().delete(OrderCommentTable.TEMP_ORDER_COMMENT, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?", 
			new String[]{
				String.valueOf(transactionId)
			});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 */
	public void deleteOrderCommentTemp(int transactionId, int orderDetailId){
		getWritableDatabase().delete(OrderCommentTable.TEMP_ORDER_COMMENT, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?", 
			new String[]{
				String.valueOf(transactionId),
				String.valueOf(orderDetailId)
			});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param commentId
	 */
	public void deleteOrderCommentTemp(int transactionId, int orderDetailId, int commentId){
		getWritableDatabase().delete(OrderCommentTable.TEMP_ORDER_COMMENT, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?"
				+ " AND " + MenuCommentTable.COLUMN_COMMENT_ID + "=?", 
			new String[]{
				String.valueOf(transactionId),
				String.valueOf(orderDetailId),
				String.valueOf(commentId)
			});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param commentId
	 * @param commentQty
	 * @throws SQLException
	 */
	public void updateOrderComment(int transactionId, int orderDetailId, 
			int commentId, double commentQty) throws SQLException{
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_TRANSACTION_ID, transactionId);
		cv.put(OrderDetailTable.COLUMN_ORDER_ID, orderDetailId);
		cv.put(MenuCommentTable.COLUMN_COMMENT_ID, commentId);
		cv.put(OrderCommentTable.COLUMN_ORDER_COMMENT_QTY, commentQty);
		getWritableDatabase().update(OrderCommentTable.TEMP_ORDER_COMMENT, cv, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?"
				+ " AND " + MenuCommentTable.COLUMN_COMMENT_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId),
					String.valueOf(commentId)
				});
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param commentId
	 * @param commentQty
	 * @param commentPrice
	 * @throws SQLException
	 */
	public void addOrderComment(int transactionId, int orderDetailId, int commentId,
			double commentQty, double commentPrice) throws SQLException{
		ContentValues cv = new ContentValues();
		cv.put(OrderTransactionTable.COLUMN_TRANSACTION_ID, transactionId);
		cv.put(OrderDetailTable.COLUMN_ORDER_ID, orderDetailId);
		cv.put(MenuCommentTable.COLUMN_COMMENT_ID, commentId);
		cv.put(OrderCommentTable.COLUMN_ORDER_COMMENT_QTY, commentQty);
		cv.put(OrderCommentTable.COLUMN_ORDER_COMMENT_PRICE, commentPrice);
		getWritableDatabase().insertOrThrow(OrderCommentTable.TEMP_ORDER_COMMENT, 
				null, cv);
	}
	
	/**
	 * @param transactionId
	 * @param orderDetailId
	 * @param commentId
	 * @return
	 */
	public boolean checkAddedComment(int transactionId, int orderDetailId, int commentId){
		boolean isAdded = false;
		Cursor cursor = getReadableDatabase().query(OrderCommentTable.TEMP_ORDER_COMMENT, 
				new String[]{
					MenuCommentTable.COLUMN_COMMENT_ID
				}, 
				OrderTransactionTable.COLUMN_TRANSACTION_ID + "=?"
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=?"
				+ " AND " + MenuCommentTable.COLUMN_COMMENT_ID + "=?", 
				new String[]{
					String.valueOf(transactionId),
					String.valueOf(orderDetailId),
					String.valueOf(commentId)
				}, null, null, null);
		if(cursor.moveToFirst()){
			if(cursor.getInt(0) != 0)
				isAdded = true;
		}
		cursor.close();
		return isAdded;
	}
	
	/**
	 * Create order comment temp when comment menu
	 * @param transactionId
	 * @param orderDetailId
	 */
	public void createOrderCommentTemp(int transactionId, int orderDetailId){
		getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + OrderCommentTable.TEMP_ORDER_COMMENT);
		getWritableDatabase().execSQL(
				"CREATE TABLE " + OrderCommentTable.TEMP_ORDER_COMMENT 
				+ " AS SELECT * FROM " + OrderCommentTable.TABLE_ORDER_COMMENT
				+ " WHERE " + OrderTransactionTable.COLUMN_TRANSACTION_ID + "=" + transactionId
				+ " AND " + OrderDetailTable.COLUMN_ORDER_ID + "=" + orderDetailId);
	}
}
