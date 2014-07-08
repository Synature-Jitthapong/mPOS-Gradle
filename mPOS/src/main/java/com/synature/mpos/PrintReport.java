package com.synature.mpos;

import java.text.NumberFormat;
import java.util.List;

import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.mpos.database.PaymentDetail;
import com.synature.mpos.database.Products;
import com.synature.mpos.database.Reporting;
import com.synature.mpos.database.Session;
import com.synature.mpos.database.Shop;
import com.synature.mpos.database.Staffs;
import com.synature.mpos.database.Transaction;
import com.synature.mpos.database.Reporting.SimpleProductData;
import com.synature.pos.Payment;
import com.synature.pos.Report;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;

public class PrintReport extends AsyncTask<Void, Void, Void> {

	public static enum WhatPrint{
		SUMMARY_SALE,
		PRODUCT_REPORT,
		BILL_REPORT
	};
	
	private Context mContext;
	private Transaction mTrans;
	private PaymentDetail mPayment;
	private Shop mShop;
	private Staffs mStaff;
	private Formater mFormat;
	private WhatPrint mWhatPrint;
	private String mDateFrom;
	private String mDateTo;
	
	private int mStaffId;
	
	public PrintReport(Context context, String dateFrom, String dateTo, 
			int staffId, WhatPrint whatPrint){
		mContext = context;
		mTrans = new Transaction(context.getApplicationContext());
		mPayment = new PaymentDetail(context.getApplicationContext());
		mShop = new Shop(context.getApplicationContext());
		mStaff = new Staffs(context.getApplicationContext());
		mFormat = new Formater(context.getApplicationContext());
		mWhatPrint = whatPrint;
		
		mDateFrom = dateFrom;
		mDateTo = dateTo;
		mStaffId = staffId;
	}
	
	public PrintReport(Context context, String dateFrom, String dateTo, WhatPrint whatPrint){
		this(context, dateFrom, dateTo, 0, whatPrint);
	}
	
	public PrintReport(Context context, int staffId, WhatPrint whatPrint){
		this(context, "", "", staffId, whatPrint);
	}
	
	protected class EPSONPrintBillReport extends EPSONPrinter{

		public EPSONPrintBillReport(Context context) {
			super(context);
		}

		@Override
		public void onBatteryStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void onStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}

		@Override
		public void prepareDataToPrint() {
			String date = mFormat.dateFormat(mDateTo);
			if(!mDateFrom.equals(mDateTo)){
				date = mFormat.dateFormat(mDateFrom) + " - " + 
						mFormat.dateFormat(mDateTo);
			}
			try {
				mBuilder.addTextAlign(Builder.ALIGN_CENTER);
				// header
				mBuilder.addText(mContext.getString(R.string.sale_by_bill_report) + "\n");
				mBuilder.addText(date + "\n");
				mBuilder.addText(mContext.getString(R.string.shop) + " " + mShop.getShopProperty().getShopName() + "\n");
				mBuilder.addText(mContext.getString(R.string.print_date) + " " + mFormat.dateTimeFormat(Utils.getCalendar().getTime()) + "\n");

				String receiptHeader = mContext.getString(R.string.receipt);
				String totalSaleHeader = mContext.getString(R.string.total);
				String closeTimeHeader = mContext.getString(R.string.time) + 
						createQtySpace(calculateLength(totalSaleHeader));
				
				// line
				mBuilder.addText(createLine("-") + "\n");
				mBuilder.addText(receiptHeader);
				mBuilder.addText(createHorizontalSpace(
						calculateLength(receiptHeader) 
						+ calculateLength(closeTimeHeader) 
						+ calculateLength(totalSaleHeader)));
				mBuilder.addText(closeTimeHeader);
				mBuilder.addText(totalSaleHeader + "\n");
				mBuilder.addText(createLine("-") + "\n");
				
				Reporting reporting = new Reporting(mContext, mDateFrom, mDateTo);
				List<Reporting.SaleTransactionReport> saleReportLst = 
						reporting.listTransactionReport();
				for(Reporting.SaleTransactionReport report : saleReportLst){
					String saleDate = mFormat.dateFormat(report.getSaleDate());
					mBuilder.addText(saleDate);
					mBuilder.addText(createHorizontalSpace(calculateLength(saleDate)) + "\n");
					for(MPOSOrderTransaction trans : report.getTransLst()){
						String receiptNo = trans.getReceiptNo();
						String totalSale = mFormat.currencyFormat(trans.getTransactionVatable());
						String closeTime = mFormat.timeFormat(trans.getCloseTime()) + 
								createQtySpace(calculateLength(totalSale));
						mBuilder.addText(receiptNo);
						mBuilder.addText(createHorizontalSpace(calculateLength(receiptNo) + 
								calculateLength(totalSale) 
								+ calculateLength(closeTime)));
						mBuilder.addText(closeTime);
						mBuilder.addText(totalSale + "\n");
					}
					mBuilder.addText("\n");
				}
			} catch (EposException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	protected class WintecPrintBillReport extends WintecPrinter{

		public WintecPrintBillReport(Context context) {
			super(context);
		}

		@Override
		public void prepareDataToPrint() {
			String date = mFormat.dateFormat(mDateTo);
			if(!mDateFrom.equals(mDateTo)){
				date = mFormat.dateFormat(mDateFrom) + " - " + 
						mFormat.dateFormat(mDateTo);
			}
			// header
			mBuilder.append("<c>" + mContext.getString(R.string.sale_by_bill_report) + "\n");
			mBuilder.append("<c>" + date + "\n");
			mBuilder.append("<c>" + mContext.getString(R.string.shop) + " " + mShop.getShopProperty().getShopName() + "\n");
			mBuilder.append(mContext.getString(R.string.print_date) + " " + mFormat.dateTimeFormat(Utils.getCalendar().getTime()) + "\n");
			
			String receiptHeader = mContext.getString(R.string.receipt);
			String totalSaleHeader = mContext.getString(R.string.total);
			String closeTimeHeader = mContext.getString(R.string.time) + 
					createQtySpace(calculateLength(totalSaleHeader));
			
			// line
			mBuilder.append(createLine("-") + "\n");
			mBuilder.append(receiptHeader);
			mBuilder.append(createHorizontalSpace(
					calculateLength(receiptHeader) 
					+ calculateLength(closeTimeHeader) 
					+ calculateLength(totalSaleHeader)));
			mBuilder.append(closeTimeHeader);
			mBuilder.append(totalSaleHeader + "\n");
			mBuilder.append(createLine("-") + "\n");
			
			Reporting reporting = new Reporting(mContext, mDateFrom, mDateTo);
			List<Reporting.SaleTransactionReport> saleReportLst = 
					reporting.listTransactionReport();
			for(Reporting.SaleTransactionReport report : saleReportLst){
				mBuilder.append(mFormat.dateFormat(report.getSaleDate()) + "\n");
				for(MPOSOrderTransaction trans : report.getTransLst()){
					String receiptNo = trans.getReceiptNo();
					String totalSale = mFormat.currencyFormat(trans.getTransactionVatable());
					String closeTime = mFormat.timeFormat(trans.getCloseTime()) + 
							createQtySpace(calculateLength(totalSale));
					mBuilder.append(receiptNo);
					mBuilder.append(createHorizontalSpace(calculateLength(receiptNo) + 
							calculateLength(totalSale) 
							+ calculateLength(closeTime)));
					mBuilder.append(closeTime);
					mBuilder.append(totalSale + "\n");
				}
				mBuilder.append("\n");
			}
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}
		
	}
	
	protected class EPSONPrintSaleByProduct extends EPSONPrinter{

		public EPSONPrintSaleByProduct(Context context) {
			super(context);
		}

		@Override
		public void onBatteryStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void onStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}

		@Override
		public void prepareDataToPrint() {
			MPOSOrderTransaction.MPOSOrderDetail summOrder 
			= mTrans.getSummaryOrderInDay(mDateFrom, mDateTo);
		
			String date = mFormat.dateFormat(mDateTo);
			if(!mDateFrom.equals(mDateTo)){
				date = mFormat.dateFormat(mDateFrom) + " - " + 
						mFormat.dateFormat(mDateTo);
			}
			
			try {
				mBuilder.addTextAlign(Builder.ALIGN_CENTER);
				// header
				mBuilder.addText(mContext.getString(R.string.sale_by_product_report) + "\n");
				mBuilder.addText( date + "\n");
				mBuilder.addText(mContext.getString(R.string.shop) + " " + mShop.getShopProperty().getShopName() + "\n");
				mBuilder.addText(mContext.getString(R.string.print_date) + " " + mFormat.dateTimeFormat(Utils.getCalendar().getTime()) + "\n\n");

				// Product Summary
				Reporting reporting = new Reporting(mContext, mDateFrom, mDateTo);
				Report reportData = reporting.getProductDataReport();
				for(Report.GroupOfProduct group : reportData.getGroupOfProductLst()){
					String groupDept = group.getProductGroupName() + ": " + group.getProductDeptName();
					mBuilder.addText(groupDept);
					mBuilder.addText(createHorizontalSpace(calculateLength(groupDept)) + "\n");
					for(Report.ReportDetail detail : group.getReportDetail()){
						String itemName = detail.getProductName();
						if(detail.getProductName() == Reporting.SUMM_DEPT){
							itemName = group.getProductDeptName() + " " +
									mContext.getString(R.string.summary);
							mBuilder.addText(itemName);
						}else if(detail.getProductName() == Reporting.SUMM_GROUP){
							itemName = group.getProductGroupName() + " " +
									mContext.getString(R.string.summary);
							mBuilder.addText(itemName);
						}else{
							mBuilder.addText(itemName);
						}
						String itemTotalPrice = mFormat.currencyFormat(detail.getSubTotal());
						String itemTotalQty = mFormat.qtyFormat(detail.getQty()) + 
								createQtySpace(calculateLength(itemTotalPrice));
						mBuilder.addText(createHorizontalSpace(calculateLength(itemName) + 
								calculateLength(itemTotalQty) 
								+ calculateLength(itemTotalPrice)));
						mBuilder.addText(itemTotalQty);
						mBuilder.addText(itemTotalPrice + "\n");
						if(detail.getProductName() == Reporting.SUMM_GROUP){
							mBuilder.addText(createLine("-") + "\n");
						}
					}
					mBuilder.addText("\n");
				}
				
				String discountText = mContext.getString(R.string.discount);
				String discount = mFormat.currencyFormat(summOrder.getPriceDiscount());
				String subTotalText = mContext.getString(R.string.sub_total) + " ";
				String subTotal = mFormat.currencyFormat(summOrder.getTotalRetailPrice());
				
				mBuilder.addText(subTotalText);
				mBuilder.addText(createHorizontalSpace(calculateLength(subTotalText) 
						+ calculateLength(subTotal)));
				mBuilder.addText(subTotal + "\n");
				mBuilder.addText(discountText);
				mBuilder.addText(createHorizontalSpace(calculateLength(discountText) 
						+ calculateLength(discount)));
				mBuilder.addText(discount + "\n");
				
				// Vat Exclude
				if(summOrder.getVatExclude() > 0){
					String vatExcludeText = mContext.getString(R.string.vat_exclude) + " " +
							NumberFormat.getInstance().format(mShop.getCompanyVatRate()) + "%";
					String vatExclude = mFormat.currencyFormat(summOrder.getVatExclude());
					mBuilder.addText(vatExcludeText);
					mBuilder.addText(createHorizontalSpace(
							calculateLength(vatExcludeText) 
							+ calculateLength(vatExclude)));
					mBuilder.addText(vatExclude + "\n\n");
				}else{
					mBuilder.addText("\n");
				}
				
				String grandTotalText = mContext.getString(R.string.grand_total);
				String grandTotal = mFormat.currencyFormat(summOrder.getTotalSalePrice() + summOrder.getVatExclude());
				mBuilder.addText(grandTotalText);
				mBuilder.addText(createHorizontalSpace(
						calculateLength(grandTotalText) 
						+ calculateLength(grandTotal)));
				mBuilder.addText(grandTotal + "\n");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EposException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	protected class WintecPrintSaleByProduct extends WintecPrinter{

		public WintecPrintSaleByProduct(Context context) {
			super(context);
		}

		@Override
		public void prepareDataToPrint() {
			MPOSOrderTransaction.MPOSOrderDetail summOrder 
				= mTrans.getSummaryOrderInDay(mDateFrom, mDateTo);
			
			String date = mFormat.dateFormat(mDateTo);
			if(!mDateFrom.equals(mDateTo)){
				date = mFormat.dateFormat(mDateFrom) + " - " + 
						mFormat.dateFormat(mDateTo);
			}
			
			// header
			mBuilder.append("<c>" + mContext.getString(R.string.sale_by_product_report) + "\n");
			mBuilder.append("<c>" + date + "\n");
			mBuilder.append("<c>" + mContext.getString(R.string.shop) + " " + mShop.getShopProperty().getShopName() + "\n");
			mBuilder.append(mContext.getString(R.string.print_date) + " " + mFormat.dateTimeFormat(Utils.getCalendar().getTime()) + "\n\n");
			
			// Product Summary
			Reporting reporting = new Reporting(mContext, mDateFrom, mDateTo);
			Report reportData = reporting.getProductDataReport();
			for(Report.GroupOfProduct group : reportData.getGroupOfProductLst()){
				mBuilder.append("<b>" + group.getProductGroupName() + ": " + group.getProductDeptName()+ "\n");
				for(Report.ReportDetail detail : group.getReportDetail()){
					String itemName = detail.getProductName();
					if(detail.getProductName() == Reporting.SUMM_DEPT){
						itemName = group.getProductDeptName() + " " +
								mContext.getString(R.string.summary);
						mBuilder.append(itemName);
					}else if(detail.getProductName() == Reporting.SUMM_GROUP){
						itemName = group.getProductGroupName() + " " +
								mContext.getString(R.string.summary);
						mBuilder.append("<b>" + itemName);
					}else{
						mBuilder.append(itemName);
					}
					String itemTotalPrice = mFormat.currencyFormat(detail.getSubTotal());
					String itemTotalQty = mFormat.qtyFormat(detail.getQty()) + 
							createQtySpace(calculateLength(itemTotalPrice));
					mBuilder.append(createHorizontalSpace(calculateLength(itemName) + 
							calculateLength(itemTotalQty) + 
							calculateLength(itemTotalPrice)));
					mBuilder.append(itemTotalQty);
					mBuilder.append(itemTotalPrice + "\n");
					if(detail.getProductName() == Reporting.SUMM_GROUP){
						mBuilder.append(createLine("-") + "\n");
					}
				}
				mBuilder.append("\n");
			}
			
			String discountText = mContext.getString(R.string.discount);
			String discount = mFormat.currencyFormat(summOrder.getPriceDiscount());
			String subTotalText = mContext.getString(R.string.sub_total) + " ";
			String subTotal = mFormat.currencyFormat(summOrder.getTotalRetailPrice());
			
			mBuilder.append(subTotalText);
			mBuilder.append(createHorizontalSpace(
					calculateLength(subTotalText) 
					+ calculateLength(subTotal)));
			mBuilder.append(subTotal + "\n");
			mBuilder.append(discountText);
			mBuilder.append(createHorizontalSpace(calculateLength(discountText) 
					+ calculateLength(discount)));
			mBuilder.append(discount + "\n");
			
			// Vat Exclude
			if(summOrder.getVatExclude() > 0){
				String vatExcludeText = mContext.getString(R.string.vat_exclude) + " " +
						NumberFormat.getInstance().format(mShop.getCompanyVatRate()) + "%";
				String vatExclude = mFormat.currencyFormat(summOrder.getVatExclude());
				mBuilder.append(vatExcludeText);
				mBuilder.append(createHorizontalSpace(calculateLength(vatExcludeText) 
						+ calculateLength(vatExclude)));
				mBuilder.append(vatExclude + "\n\n");
			}else{
				mBuilder.append("\n");
			}
			
			String grandTotalText = mContext.getString(R.string.grand_total);
			String grandTotal = mFormat.currencyFormat(summOrder.getTotalSalePrice() + summOrder.getVatExclude());
			mBuilder.append(grandTotalText);
			mBuilder.append(createHorizontalSpace(calculateLength(grandTotalText) 
					+ calculateLength(grandTotal)));
			mBuilder.append(grandTotal + "\n");
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}
	}
	
	protected class EPSONPrintSummarySale extends EPSONPrinter{

		public EPSONPrintSummarySale(Context context) {
			super(context);
		}

		@Override
		public void onBatteryStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void onStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}

		@Override
		public void prepareDataToPrint() {
			Session session = new Session(mContext.getApplicationContext());
			MPOSOrderTransaction trans = mTrans.getTransaction(session.getSessionDate()); 
			MPOSOrderTransaction.MPOSOrderDetail summOrder 
				= mTrans.getSummaryOrderInDay(session.getSessionDate(), session.getSessionDate());

			try {
				mBuilder.addTextAlign(Builder.ALIGN_CENTER);
				// header
				mBuilder.addText(mContext.getString(R.string.endday_report) + "\n");
				mBuilder.addText(mFormat.dateFormat(session.getSessionDate()) + "\n");
				mBuilder.addText(mContext.getString(R.string.shop) + " " + mShop.getShopProperty().getShopName() + "\n");
				mBuilder.addText(mContext.getString(R.string.print_by) + " " + mStaff.getStaff(mStaffId).getStaffName() + "\n");
				mBuilder.addText(mContext.getString(R.string.print_date) + " " + mFormat.dateTimeFormat(Utils.getCalendar().getTime()) + "\n");
				
				// ReceiptNo.
				String receiptNo = mContext.getString(R.string.receipt_no);
				String from = mTrans.getMinReceiptNo(session.getSessionDate()) + " - ";
				String to = mTrans.getMaxReceiptNo(session.getSessionDate());
				mBuilder.addText(receiptNo);
				mBuilder.addText(createHorizontalSpace(calculateLength(receiptNo)) + "\n");
				mBuilder.addText(from);
				mBuilder.addText(createHorizontalSpace(calculateLength(from)) + "\n");
				mBuilder.addText(to);
				mBuilder.addText(createHorizontalSpace(calculateLength(to)) + "\n\n");
				
				// Product Summary
				Reporting report = new Reporting(mContext, session.getSessionDate(), session.getSessionDate());
				List<SimpleProductData> simpleLst = report.listSummaryProductGroupInDay();
				if(simpleLst != null){
					for(SimpleProductData sp : simpleLst){
						String groupName = sp.getDeptName();
						String groupTotalPrice = mFormat.currencyFormat(sp.getDeptTotalPrice());
						String groupTotalQty = mFormat.qtyFormat(sp.getDeptTotalQty()) + 
								createQtySpace(calculateLength(groupTotalPrice));
						mBuilder.addText(groupName);
						mBuilder.addText(createHorizontalSpace(calculateLength(groupName) + 
								calculateLength(groupTotalQty) 
								+ calculateLength(groupTotalPrice)));
						mBuilder.addText(groupTotalQty);
						mBuilder.addText(groupTotalPrice + "\n");
						if(sp.getItemLst() != null){
							for(SimpleProductData.Item item : sp.getItemLst()){
								String itemName = "-" + item.getItemName();
								String itemTotalPrice = mFormat.currencyFormat(item.getTotalPrice());
								String itemTotalQty = mFormat.qtyFormat(item.getTotalQty()) + 
										createQtySpace(calculateLength(itemTotalPrice));
								mBuilder.addText(itemName);
								mBuilder.addText(createHorizontalSpace(calculateLength(itemName) + 
										calculateLength(itemTotalQty) 
										+ calculateLength(itemTotalPrice)));
								mBuilder.addText(itemTotalQty);
								mBuilder.addText(itemTotalPrice + "\n");
							}
						}
					}
					// Sub Total
					mBuilder.addText("\n");
					String subTotalText = mContext.getString(R.string.sub_total);
					String subTotalPrice = mFormat.currencyFormat(summOrder.getTotalRetailPrice());
					String subTotalQty = mFormat.qtyFormat(summOrder.getQty()) + 
							createQtySpace(calculateLength(subTotalPrice));
					mBuilder.addText(subTotalText);
					mBuilder.addText(createHorizontalSpace(calculateLength(subTotalText) 
							+ calculateLength(subTotalQty) 
							+ calculateLength(subTotalPrice)));
					mBuilder.addText(subTotalQty);
					mBuilder.addText(subTotalPrice + "\n\n");
				}
				
				String discountText = mContext.getString(R.string.discount);
				String discount = mFormat.currencyFormat(summOrder.getPriceDiscount());
				String subTotalText = mContext.getString(R.string.sub_total) + " ";
				String subTotal = mFormat.currencyFormat(summOrder.getTotalSalePrice());
				
				mBuilder.addText(discountText);
				mBuilder.addText(createHorizontalSpace(calculateLength(discountText) 
						+ calculateLength(discount)));
				mBuilder.addText(discount + "\n");
				mBuilder.addText(subTotalText);
				mBuilder.addText(createHorizontalSpace(calculateLength(subTotalText) 
						+ calculateLength(subTotal)));
				mBuilder.addText(subTotal + "\n");
				
				// Vat Exclude
				if(summOrder.getVatExclude() > 0){
					String vatExcludeText = mContext.getString(R.string.vat_exclude) + " " +
							NumberFormat.getInstance().format(mShop.getCompanyVatRate()) + "%";
					String vatExclude = mFormat.currencyFormat(summOrder.getVatExclude());
					mBuilder.addText(vatExcludeText);
					mBuilder.addText(createHorizontalSpace(
							calculateLength(vatExcludeText) 
							+ calculateLength(vatExclude)));
					mBuilder.addText(vatExclude + "\n\n");
				}
				
				String totalSaleText = mContext.getString(R.string.total_sale);
				String totalSale = mFormat.currencyFormat(summOrder.getTotalSalePrice() + summOrder.getVatExclude());
				mBuilder.addText(totalSaleText);
				mBuilder.addText(createHorizontalSpace(
						calculateLength(totalSaleText) 
						+ calculateLength(totalSale)));
				mBuilder.addText(totalSale + "\n");
				
				if(mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
					String beforeVatText = mContext.getString(R.string.before_vat);
					String beforeVat = mFormat.currencyFormat(trans.getTransactionVatable() - trans.getTransactionVat());
					String totalVatText = mContext.getString(R.string.total_vat);
					String totalVat = mFormat.currencyFormat(trans.getTransactionVat());
					mBuilder.addText(beforeVatText);
					mBuilder.addText(createHorizontalSpace(
							calculateLength(beforeVatText) 
							+ calculateLength(beforeVat)));
					mBuilder.addText(beforeVat + "\n");
					mBuilder.addText(totalVatText);
					mBuilder.addText(createHorizontalSpace(
							calculateLength(totalVatText) 
							+ calculateLength(totalVat)));
					mBuilder.addText(totalVat + "\n\n");
				}
				
				List<Payment.PaymentDetail> summaryPaymentLst = 
						mPayment.listSummaryPayment(
								mTrans.getSeperateTransactionId(session.getSessionDate()));
				if(summaryPaymentLst != null){
					String paymentDetailText = mContext.getString(R.string.payment_detail);
					mBuilder.addText(paymentDetailText);
					mBuilder.addText(createHorizontalSpace(
							calculateLength(paymentDetailText)) + "\n");
					for(Payment.PaymentDetail payment : summaryPaymentLst){
						String payTypeName = payment.getPayTypeName();
						String payAmount = mFormat.currencyFormat(payment.getPayAmount());
						mBuilder.addText(payTypeName);
						mBuilder.addText(createHorizontalSpace(
								calculateLength(payTypeName) 
								+ calculateLength(payAmount)));
						mBuilder.addText(payAmount + "\n\n");
					}
				}
				String totalReceiptInDay = mContext.getString(R.string.total_receipt_in_day);
				String totalReceipt = String.valueOf(mTrans.getTotalReceipt(session.getSessionDate()));
				mBuilder.addText(totalReceiptInDay);
				mBuilder.addText(createHorizontalSpace(
						calculateLength(totalReceiptInDay) 
						+ calculateLength(totalReceipt)));
				mBuilder.addText(totalReceipt + "\n\n");
				
				MPOSOrderTransaction.MPOSOrderDetail summVoidOrder = 
						mTrans.getSummaryVoidOrderInDay(session.getSessionDate());
				String voidBillText = mContext.getString(R.string.void_bill);
				mBuilder.addText(voidBillText);
				mBuilder.addText(createHorizontalSpace(calculateLength(voidBillText)) + "\n");
				String voidBill = mContext.getString(R.string.void_bill_after_paid);
				String totalVoidPrice = mFormat.currencyFormat(summVoidOrder.getTotalSalePrice());
				String totalVoidQty = mFormat.qtyFormat(summVoidOrder.getQty()) +
						createQtySpace(calculateLength(totalVoidPrice));
				mBuilder.addText(voidBill);
				mBuilder.addText(createHorizontalSpace(
						calculateLength(voidBill) 
						+ calculateLength(totalVoidQty) 
						+ calculateLength(totalVoidPrice)));
				mBuilder.addText(totalVoidQty);
				mBuilder.addText(totalVoidPrice);
			} catch (EposException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	protected class WintecPrintSummarySale extends WintecPrinter{
		
		public WintecPrintSummarySale(Context context) {
			super(context);
		}

		@Override
		public void prepareDataToPrint() {
			Session session = new Session(mContext.getApplicationContext());
			MPOSOrderTransaction trans = mTrans.getTransaction(session.getSessionDate()); 
			MPOSOrderTransaction.MPOSOrderDetail summOrder 
				= mTrans.getSummaryOrderInDay(session.getSessionDate(), session.getSessionDate());

			// header
			mBuilder.append("<c>" + mContext.getString(R.string.endday_report) + "\n");
			mBuilder.append("<c>" + mFormat.dateFormat(session.getSessionDate()) + "\n");
			mBuilder.append("<c>" + mContext.getString(R.string.shop) + " " + mShop.getShopProperty().getShopName() + "\n");
			mBuilder.append(mContext.getString(R.string.print_by) + " " + mStaff.getStaff(mStaffId).getStaffName() + "\n");
			mBuilder.append(mContext.getString(R.string.print_date) + " " + mFormat.dateTimeFormat(Utils.getCalendar().getTime()) + "\n");
			
			// ReceiptNo.
			mBuilder.append("<u>" + mContext.getString(R.string.receipt_no) + "\n");
			mBuilder.append(mTrans.getMinReceiptNo(session.getSessionDate()) + " -\n");
			mBuilder.append(mTrans.getMaxReceiptNo(session.getSessionDate()) + "\n\n");
			
			// Product Summary
			Reporting report = new Reporting(mContext, session.getSessionDate(), session.getSessionDate());
			List<SimpleProductData> simpleLst = report.listSummaryProductGroupInDay();
			if(simpleLst != null){
				for(SimpleProductData sp : simpleLst){
					String groupName = sp.getDeptName();
					String groupTotalPrice = mFormat.currencyFormat(sp.getDeptTotalPrice());
					String groupTotalQty = mFormat.qtyFormat(sp.getDeptTotalQty()) + 
							createQtySpace(calculateLength(groupTotalPrice));
					mBuilder.append("<b>" + groupName);
					mBuilder.append(createHorizontalSpace(
							calculateLength(groupName) 
							+ calculateLength(groupTotalQty) 
							+ calculateLength(groupTotalPrice)));
					mBuilder.append("<b>" + groupTotalQty);
					mBuilder.append("<b>" + groupTotalPrice + "\n");
					if(sp.getItemLst() != null){
						for(SimpleProductData.Item item : sp.getItemLst()){
							String itemName = "-" + item.getItemName();
							String itemTotalPrice = mFormat.currencyFormat(item.getTotalPrice());
							String itemTotalQty = mFormat.qtyFormat(item.getTotalQty()) + 
									createQtySpace(calculateLength(itemTotalPrice));
							mBuilder.append(itemName);
							mBuilder.append(createHorizontalSpace(
									calculateLength(itemName) 
									+ calculateLength(itemTotalQty) 
									+ calculateLength(itemTotalPrice)));
							mBuilder.append(itemTotalQty);
							mBuilder.append(itemTotalPrice + "\n");
						}
					}
				}
				// Sub Total
				mBuilder.append("\n");
				String subTotalText = mContext.getString(R.string.sub_total);
				String subTotalPrice = mFormat.currencyFormat(summOrder.getTotalRetailPrice());
				String subTotalQty = mFormat.qtyFormat(summOrder.getQty()) + 
						createQtySpace(calculateLength(subTotalPrice));
				mBuilder.append(subTotalText);
				mBuilder.append(createHorizontalSpace(
						calculateLength(subTotalText) 
						+ calculateLength(subTotalQty) 
						+ calculateLength(subTotalPrice)));
				mBuilder.append(subTotalQty);
				mBuilder.append(subTotalPrice + "\n\n");
			}
			
			String discountText = mContext.getString(R.string.discount);
			String discount = mFormat.currencyFormat(summOrder.getPriceDiscount());
			String subTotalText = mContext.getString(R.string.sub_total) + " ";
			String subTotal = mFormat.currencyFormat(summOrder.getTotalSalePrice());
			
			mBuilder.append(discountText);
			mBuilder.append(createHorizontalSpace(
					calculateLength(discountText) 
					+ calculateLength(discount)));
			mBuilder.append(discount + "\n");
			mBuilder.append(subTotalText);
			mBuilder.append(createHorizontalSpace(
					calculateLength(subTotalText) 
					+ calculateLength(subTotal)));
			mBuilder.append(subTotal + "\n");
			
			// Vat Exclude
			if(summOrder.getVatExclude() > 0){
				String vatExcludeText = mContext.getString(R.string.vat_exclude) + " " +
						NumberFormat.getInstance().format(mShop.getCompanyVatRate()) + "%";
				String vatExclude = mFormat.currencyFormat(summOrder.getVatExclude());
				mBuilder.append(vatExcludeText);
				mBuilder.append(createHorizontalSpace(
						calculateLength(vatExcludeText) 
						+ calculateLength(vatExclude)));
				mBuilder.append(vatExclude + "\n\n");
			}
			
			String totalSaleText = mContext.getString(R.string.total_sale);
			String totalSale = mFormat.currencyFormat(summOrder.getTotalSalePrice() + summOrder.getVatExclude());
			mBuilder.append(totalSaleText);
			mBuilder.append(createHorizontalSpace(
					calculateLength(totalSaleText) 
					+ calculateLength(totalSale)));
			mBuilder.append(totalSale + "\n");
			
			if(mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
				String beforeVatText = mContext.getString(R.string.before_vat);
				String beforeVat = mFormat.currencyFormat(trans.getTransactionVatable() - trans.getTransactionVat());
				String totalVatText = mContext.getString(R.string.total_vat);
				String totalVat = mFormat.currencyFormat(trans.getTransactionVat());
				mBuilder.append(beforeVatText);
				mBuilder.append(createHorizontalSpace(
						calculateLength(beforeVatText) 
						+ calculateLength(beforeVat)));
				mBuilder.append(beforeVat + "\n");
				mBuilder.append(totalVatText);
				mBuilder.append(createHorizontalSpace(
						calculateLength(totalVatText) 
						+ calculateLength(totalVat)));
				mBuilder.append(totalVat + "\n\n");
			}
			
			List<Payment.PaymentDetail> summaryPaymentLst = 
					mPayment.listSummaryPayment(
							mTrans.getSeperateTransactionId(session.getSessionDate()));
			if(summaryPaymentLst != null){
				mBuilder.append(mContext.getString(R.string.payment_detail) + "\n");
				for(Payment.PaymentDetail payment : summaryPaymentLst){
					String payTypeName = payment.getPayTypeName();
					String payAmount = mFormat.currencyFormat(payment.getPayAmount());
					mBuilder.append(payTypeName);
					mBuilder.append(createHorizontalSpace(
							calculateLength(payTypeName) 
							+ calculateLength(payAmount)));
					mBuilder.append(payAmount + "\n\n");
				}
			}
			String totalReceiptInDay = mContext.getString(R.string.total_receipt_in_day);
			String totalReceipt = String.valueOf(mTrans.getTotalReceipt(session.getSessionDate()));
			mBuilder.append(totalReceiptInDay);
			mBuilder.append(createHorizontalSpace(
					calculateLength(totalReceiptInDay) 
					+ calculateLength(totalReceipt)));
			mBuilder.append(totalReceipt + "\n\n");
			
			MPOSOrderTransaction.MPOSOrderDetail summVoidOrder = 
					mTrans.getSummaryVoidOrderInDay(session.getSessionDate());
			mBuilder.append(mContext.getString(R.string.void_bill) + "\n");
			String voidBill = mContext.getString(R.string.void_bill_after_paid);
			String totalVoidPrice = mFormat.currencyFormat(summVoidOrder.getTotalSalePrice());
			String totalVoidQty = mFormat.qtyFormat(summVoidOrder.getQty()) +
					createQtySpace(calculateLength(totalVoidPrice));
			mBuilder.append(voidBill);
			mBuilder.append(createHorizontalSpace(
					calculateLength(voidBill) 
					+ calculateLength(totalVoidQty) 
					+ calculateLength(totalVoidPrice)));
			mBuilder.append(totalVoidQty);
			mBuilder.append(totalVoidPrice);
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if(Utils.isInternalPrinterSetting(mContext)){
			switch(mWhatPrint){
			case SUMMARY_SALE:
				WintecPrintSummarySale wtPrinter = new WintecPrintSummarySale(mContext);
				wtPrinter.prepareDataToPrint();
				wtPrinter.print();
				break;
			case PRODUCT_REPORT:
				WintecPrintSaleByProduct wtPrintProduct = new WintecPrintSaleByProduct(mContext);
				wtPrintProduct.prepareDataToPrint();
				wtPrintProduct.print();
				break;
			case BILL_REPORT:
				WintecPrintBillReport wtPrintBill = new WintecPrintBillReport(mContext);
				wtPrintBill.prepareDataToPrint();
				wtPrintBill.print();
				break;
			}
		}else{
			switch(mWhatPrint){
			case SUMMARY_SALE:
				EPSONPrintSummarySale epSumSale = new EPSONPrintSummarySale(mContext);
				epSumSale.prepareDataToPrint();
				epSumSale.print();
				break;
			case PRODUCT_REPORT:
				EPSONPrintSaleByProduct epProduct = new EPSONPrintSaleByProduct(mContext);
				epProduct.prepareDataToPrint();
				epProduct.print();
				break;
			case BILL_REPORT:
				EPSONPrintBillReport epBill = new EPSONPrintBillReport(mContext);
				epBill.prepareDataToPrint();
				epBill.print();
				break;
			}
		}
		return null;
	}
}
