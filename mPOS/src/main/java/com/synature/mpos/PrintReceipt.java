package com.synature.mpos;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.synature.mpos.database.CreditCard;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.HeaderFooterReceipt;
import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.mpos.database.PaymentDetail;
import com.synature.mpos.database.PrintReceiptLog;
import com.synature.mpos.database.Products;
import com.synature.mpos.database.Shop;
import com.synature.mpos.database.Staffs;
import com.synature.mpos.database.Transaction;
import com.synature.pos.Payment;
import com.synature.pos.ShopData;
import com.synature.util.Logger;

public class PrintReceipt extends AsyncTask<Void, Void, Void>{
	
	public static final String TAG = "PrintReceipt";
	private Transaction mOrders;
	private PaymentDetail mPayment;
	private Shop mShop;
	private HeaderFooterReceipt mHeaderFooter;
	private Formater mFormat;
	private Staffs mStaff;
	private CreditCard mCreditCard;
	private PrintStatusListener mPrintListener;
	private Context mContext;
	
	/**
	 * @param context
	 * @param listener
	 */
	public PrintReceipt(Context context, PrintStatusListener listener){
		mContext = context;
		mOrders = new Transaction(context);
		mPayment = new PaymentDetail(context);
		mShop = new Shop(context);
		mFormat = new Formater(context);
		mHeaderFooter = new HeaderFooterReceipt(context);
		mStaff = new Staffs(context);
		mCreditCard = new CreditCard(context);
		mPrintListener = listener;
	}
	
	protected class EPSONPrintReceipt extends EPSONPrinter{

		
		public EPSONPrintReceipt(Context context) {
			super(context);
		}

		@Override
		public void onBatteryStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void onStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void prepareDataToPrint(int transactionId){
			MPOSOrderTransaction trans = mOrders.getTransaction(transactionId);
			MPOSOrderTransaction.MPOSOrderDetail summOrder = mOrders.getSummaryOrder(transactionId);
			double beforVat = trans.getTransactionVatable() - trans.getTransactionVat();
			double change = mPayment.getTotalPaid(transactionId) - (summOrder.getTotalSalePrice() + summOrder.getVatExclude());

			try {
				mBuilder.addTextAlign(Builder.ALIGN_CENTER);
				// add void header
				if(trans.getTransactionStatusId() == Transaction.TRANS_STATUS_VOID){
					String voidReceipt = mContext.getString(R.string.void_receipt);
					Calendar cVoidTime = Calendar.getInstance();
					cVoidTime.setTimeInMillis(Long.parseLong(trans.getVoidTime()));
					String voidTime = mContext.getString(R.string.void_time) + " " + mFormat.dateTimeFormat(cVoidTime.getTime());
					String voidBy = mContext.getString(R.string.void_by) + " " + mStaff.getStaff(trans.getVoidStaffId()).getStaffName();
					String voidReason = mContext.getString(R.string.reason) + " " + trans.getVoidReason();
					mBuilder.addText(voidReceipt + "\n");
					mBuilder.addText(voidTime);
					mBuilder.addText(createHorizontalSpace(calculateLength(voidTime)) + "\n");
					mBuilder.addText(voidBy);
					mBuilder.addText(createHorizontalSpace(calculateLength(voidBy)) + "\n");
					mBuilder.addText(voidReason);
					mBuilder.addText(createHorizontalSpace(calculateLength(voidReason)) +"\n\n");
				}
				
				// add header
				for(ShopData.HeaderFooterReceipt hf : 
					mHeaderFooter.listHeaderFooter(HeaderFooterReceipt.HEADER_LINE_TYPE)){
					mBuilder.addText(hf.getTextInLine());
					mBuilder.addText("\n");
				}
				
				String saleDate = mContext.getString(R.string.date) + " " +
						mFormat.dateTimeFormat(Utils.getCalendar().getTime());
				String receiptNo = mContext.getString(R.string.receipt_no) + " " +
						trans.getReceiptNo();
				String cashCheer = mContext.getString(R.string.cashier) + " " +
						mStaff.getStaff(trans.getOpenStaffId()).getStaffName();
				mBuilder.addText(saleDate + createHorizontalSpace(calculateLength(saleDate)) + "\n");
				mBuilder.addText(receiptNo + createHorizontalSpace(calculateLength(receiptNo)) + "\n");
				mBuilder.addText(cashCheer + createHorizontalSpace(calculateLength(cashCheer)));
				mBuilder.addText("\n" + createLine("=") + "\n");
				
				List<MPOSOrderTransaction.MPOSOrderDetail> orderLst = 
						mOrders.listAllOrderGroupByProduct(transactionId);
		    	for(int i = 0; i < orderLst.size(); i++){
		    		MPOSOrderTransaction.MPOSOrderDetail order = 
		    				orderLst.get(i);
		    		
		    		String productName = order.getProductName();
		    		String productQty = mFormat.qtyFormat(order.getQty()) + "x ";
		    		String productPrice = mFormat.currencyFormat(order.getPricePerUnit());
		    		
		    		mBuilder.addText(productQty);
		    		mBuilder.addText(productName);
		    		mBuilder.addText(createHorizontalSpace(calculateLength(productQty) + 
		    				calculateLength(productName) + calculateLength(productPrice)));
		    		mBuilder.addText(productPrice);
		    		mBuilder.addText("\n");
		    		
		    		// orderSet
		    		if(order.getOrderSetDetailLst() != null){
		    			for(MPOSOrderTransaction.OrderSet.OrderSetDetail setDetail :
		    				order.getOrderSetDetailLst()){
		    				String setName = setDetail.getProductName();
		    				String setQty = "   " + mFormat.qtyFormat(setDetail.getOrderSetQty()) + "x ";
		    				String setPrice = mFormat.currencyFormat(setDetail.getProductPrice());
		    				mBuilder.addText(setQty);
		    				mBuilder.addText(setName);
		    				mBuilder.addText(createHorizontalSpace(
		    						calculateLength(setQty) + 
		    						calculateLength(setName) + 
		    						calculateLength(setPrice)));
		    				mBuilder.addText(setPrice);
		    				mBuilder.addText("\n");
		    			}
		    		}
		    	}
		    	mBuilder.addText(createLine("-") + "\n");
		    	
		    	String itemText = mContext.getString(R.string.items) + ": ";
		    	String totalText = mContext.getString(R.string.total) + "...............";
		    	String changeText = mContext.getString(R.string.change) + " ";
		    	String beforeVatText = mContext.getString(R.string.before_vat);
		    	String discountText = mContext.getString(R.string.discount);
		    	String vatRateText = mContext.getString(R.string.vat) + " " +
		    			mFormat.currencyFormat(mShop.getCompanyVatRate(), "#,###.##") + "%";
		    	
		    	String strTotalRetailPrice = mFormat.currencyFormat(summOrder.getTotalRetailPrice());
		    	String strTotalSale = mFormat.currencyFormat(summOrder.getTotalSalePrice() + summOrder.getVatExclude());
		    	String strTotalDiscount = "-" + mFormat.currencyFormat(summOrder.getPriceDiscount());
		    	String strTotalChange = mFormat.currencyFormat(change);
		    	String strBeforeVat = mFormat.currencyFormat(beforVat);
		    	String strTransactionVat = mFormat.currencyFormat(trans.getTransactionVat());
		    	
		    	// total item
		    	String strTotalQty = NumberFormat.getInstance().format(summOrder.getQty());
		    	mBuilder.addText(itemText);
		    	mBuilder.addText(strTotalQty);
		    	mBuilder.addText(createHorizontalSpace(
		    			calculateLength(itemText) + 
		    			calculateLength(strTotalQty) + 
		    			calculateLength(strTotalRetailPrice)));
		    	mBuilder.addText(strTotalRetailPrice + "\n");
		    	
		    	// total discount
		    	if(summOrder.getPriceDiscount() > 0){
			    	mBuilder.addText(discountText);
			    	mBuilder.addText(createHorizontalSpace(
			    			calculateLength(discountText) + 
			    			calculateLength(strTotalDiscount)));
			    	mBuilder.addText(strTotalDiscount + "\n");
		    	}
		    	
		    	// transaction exclude vat
		    	if(trans.getTransactionVatExclude() > 0){
		    		String vatExcludeText = mContext.getString(R.string.vat) + " " +
		    				mFormat.currencyFormat(mShop.getCompanyVatRate(), "#,###.##") + "%";
		    		String strVatExclude = mFormat.currencyFormat(trans.getTransactionVatExclude());
		    		mBuilder.addText(vatExcludeText);
		    		mBuilder.addText(createHorizontalSpace(
		    				calculateLength(vatExcludeText) + 
		    				calculateLength(strVatExclude)));
		    		mBuilder.addText(strVatExclude + "\n");
		    	}
		    	
		    	// total price
		    	mBuilder.addText(totalText);
		    	mBuilder.addText(createHorizontalSpace(
		    			calculateLength(totalText) + 
		    			calculateLength(strTotalSale)));
		    	mBuilder.addText(strTotalSale + "\n");

		    	// total payment
		    	List<Payment.PaymentDetail> paymentLst = 
		    			mPayment.listPaymentGroupByType(transactionId);
		    	for(int i = 0; i < paymentLst.size(); i++){
		    		Payment.PaymentDetail payment = paymentLst.get(i);
			    	String strTotalPaid = mFormat.currencyFormat(payment.getPaid());
			    	if(payment.getPayTypeID() == PaymentDetail.PAY_TYPE_CREDIT){
			    		String paymentText = payment.getPayTypeName();
			    		String cardNoText = "xxxx xxxx xxxx ";
			    		try {
			    			paymentText = payment.getPayTypeName() + ":" + 
		    					mCreditCard.getCreditCardType(payment.getCreditCardType());
			    			cardNoText += payment.getCreaditCardNo().substring(12, 16);
			    		} catch (Exception e) {
			    			Logger.appendLog(mContext, Utils.LOG_DIR, 
			    					Utils.LOG_FILE_NAME, "Error gen creditcard no : " + e.getMessage());
			    		}
			    		mBuilder.addText(paymentText);
			    		mBuilder.addText(createHorizontalSpace(
			    				calculateLength(paymentText)));
			    		mBuilder.addText("\n");
		    			mBuilder.addText(cardNoText);
		    			mBuilder.addText(createHorizontalSpace(
		    					calculateLength(cardNoText) + 
		    					calculateLength(strTotalPaid)));
		    			mBuilder.addText(strTotalPaid);
			    	}else{
			    		String paymentText = payment.getPayTypeName() + " ";
				    	if(i < paymentLst.size() - 1){
					    	mBuilder.addText(paymentText);
				    		mBuilder.addText(createHorizontalSpace(
				    				calculateLength(paymentText) + 
				    				calculateLength(strTotalPaid)));
					    	mBuilder.addText(strTotalPaid);
				    	}else if(i == paymentLst.size() - 1){
					    	if(change > 0){
						    	mBuilder.addText(paymentText);
						    	mBuilder.addText(strTotalPaid);
					    		mBuilder.addText(createHorizontalSpace(
					    				calculateLength(changeText) + 
					    				calculateLength(strTotalChange) + 
					    				calculateLength(paymentText) + 
					    				calculateLength(strTotalPaid)));
						    	mBuilder.addText(changeText);
						    	mBuilder.addText(strTotalChange);
						    }else{
						    	mBuilder.addText(paymentText);
					    		mBuilder.addText(createHorizontalSpace(
					    				calculateLength(paymentText) + 
					    				calculateLength(strTotalPaid)));
						    	mBuilder.addText(strTotalPaid);
						    }
				    	}
			    	}
		    		mBuilder.addText("\n");
		    	}
			    mBuilder.addText(createLine("=") + "\n");
			    
			    if(mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
				    // before vat
				    mBuilder.addText(beforeVatText);
				    mBuilder.addText(createHorizontalSpace(
				    		calculateLength(beforeVatText) + 
				    		calculateLength(strBeforeVat)));
				    mBuilder.addText(strBeforeVat + "\n");
				    
				    // transaction vat
			    	mBuilder.addText(vatRateText);
			    	mBuilder.addText(createHorizontalSpace(
			    			calculateLength(vatRateText) + 
			    			calculateLength(strTransactionVat)));
			    	mBuilder.addText(strTransactionVat + "\n");
			    }
		    	// add footer
		    	for(ShopData.HeaderFooterReceipt hf : 
					mHeaderFooter.listHeaderFooter(HeaderFooterReceipt.FOOTER_LINE_TYPE)){
					mBuilder.addText(hf.getTextInLine());
					mBuilder.addText("\n");
				}
			} catch (EposException e) {
				switch(e.getErrorStatus()){
				case EposException.ERR_PARAM:
					
					break;
				case EposException.ERR_MEMORY:
					
					break;
				case EposException.ERR_UNSUPPORTED:
					
					break;
				case EposException.ERR_FAILURE:
					
					break;
				}
				e.printStackTrace();
			}
		}

		@Override
		public void prepareDataToPrint() {
		}
		
	}

	protected class WintecPrintReceipt extends WintecPrinter{

		public WintecPrintReceipt(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
			MPOSOrderTransaction trans = mOrders.getTransaction(transactionId);
			MPOSOrderTransaction.MPOSOrderDetail summOrder = mOrders.getSummaryOrder(transactionId);
			double beforVat = trans.getTransactionVatable() - trans.getTransactionVat();
			double change = mPayment.getTotalPaid(transactionId) - (summOrder.getTotalSalePrice() + summOrder.getVatExclude());
			
			// add void header
			if(trans.getTransactionStatusId() == Transaction.TRANS_STATUS_VOID){
				mBuilder.append("<c>" + mContext.getString(R.string.void_bill) + "\n");
				Calendar voidTime = Calendar.getInstance();
				voidTime.setTimeInMillis(Long.parseLong(trans.getVoidTime()));
				mBuilder.append(mContext.getString(R.string.void_time) + " " + mFormat.dateTimeFormat(voidTime.getTime()) + "\n");
				mBuilder.append(mContext.getString(R.string.void_by) + " " + mStaff.getStaff(trans.getVoidStaffId()).getStaffName() + "\n");
				mBuilder.append(mContext.getString(R.string.reason) + " " + trans.getVoidReason() + "\n\n");
			}
			
			// add header
			for(ShopData.HeaderFooterReceipt hf : 
				mHeaderFooter.listHeaderFooter(HeaderFooterReceipt.HEADER_LINE_TYPE)){
				mBuilder.append("<c>");
				mBuilder.append(hf.getTextInLine());
				mBuilder.append("\n");
			}
			
			String saleDate = mContext.getString(R.string.date) + " " +
					mFormat.dateTimeFormat(Utils.getCalendar().getTime());
			String receiptNo = mContext.getString(R.string.receipt_no) + " " +
					trans.getReceiptNo();
			String cashCheer = mContext.getString(R.string.cashier) + " " +
					mStaff.getStaff(trans.getOpenStaffId()).getStaffName();
			mBuilder.append(saleDate + createHorizontalSpace(calculateLength(saleDate)) + "\n");
			mBuilder.append(receiptNo + createHorizontalSpace(calculateLength(receiptNo)) + "\n");
			mBuilder.append(cashCheer + createHorizontalSpace(calculateLength(cashCheer)) + "\n");
			mBuilder.append(createLine("=") + "\n");
			
			List<MPOSOrderTransaction.MPOSOrderDetail> orderLst = 
					mOrders.listAllOrderGroupByProduct(transactionId);
	    	for(int i = 0; i < orderLst.size(); i++){
	    		MPOSOrderTransaction.MPOSOrderDetail order = 
	    				orderLst.get(i);
	    		String productName = order.getProductName();
	    		String productQty = mFormat.qtyFormat(order.getQty()) + "x ";
	    		String productPrice = mFormat.currencyFormat(order.getPricePerUnit());
	    		
	    		mBuilder.append(productQty);
	    		mBuilder.append(productName);
	    		mBuilder.append(createHorizontalSpace(
	    				calculateLength(productQty) + 
	    				calculateLength(productName) + 
	    				calculateLength(productPrice)));
	    		mBuilder.append(productPrice);
	    		mBuilder.append("\n");
	    		
	    		// orderSet
	    		if(order.getOrderSetDetailLst() != null){
	    			for(MPOSOrderTransaction.OrderSet.OrderSetDetail setDetail :
	    				order.getOrderSetDetailLst()){
	    				String setName = setDetail.getProductName();
	    				String setQty = "   " + mFormat.qtyFormat(setDetail.getOrderSetQty()) + "x ";
	    				String setPrice = mFormat.currencyFormat(setDetail.getProductPrice());
	    				mBuilder.append(setQty);
	    				mBuilder.append(setName);
	    				mBuilder.append(createHorizontalSpace(
	    						calculateLength(setQty) + 
	    						calculateLength(setName) + 
	    						calculateLength(setPrice)));
	    				mBuilder.append(setPrice);
	    				mBuilder.append("\n");
	    			}
	    		}
	    	}
	    	mBuilder.append(createLine("-") + "\n");
	    	
	    	String itemText = mContext.getString(R.string.items) + ": ";
	    	String totalText = mContext.getString(R.string.total) + "...............";
	    	String changeText = mContext.getString(R.string.change) + " ";
	    	String beforeVatText = mContext.getString(R.string.before_vat);
	    	String discountText = mContext.getString(R.string.discount);
	    	String vatRateText = mContext.getString(R.string.vat) + " " +
	    			mFormat.currencyFormat(mShop.getCompanyVatRate(), "#,###.##") + "%";
	    	
	    	String strTotalRetailPrice = mFormat.currencyFormat(summOrder.getTotalRetailPrice());
	    	String strTotalSale = mFormat.currencyFormat(summOrder.getTotalSalePrice() + summOrder.getVatExclude());
	    	String strTotalDiscount = "-" + mFormat.currencyFormat(summOrder.getPriceDiscount());
	    	String strTotalChange = mFormat.currencyFormat(change);
	    	String strBeforeVat = mFormat.currencyFormat(beforVat);
	    	String strTransactionVat = mFormat.currencyFormat(trans.getTransactionVat());
	    	
	    	// total item
	    	String strTotalQty = NumberFormat.getInstance().format(summOrder.getQty());
	    	mBuilder.append(itemText);
	    	mBuilder.append(strTotalQty);
	    	mBuilder.append(createHorizontalSpace(
	    			calculateLength(itemText) + 
	    			calculateLength(strTotalQty) + 
	    			calculateLength(strTotalRetailPrice)));
	    	mBuilder.append(strTotalRetailPrice + "\n");
	    	
	    	// total discount
	    	if(summOrder.getPriceDiscount() > 0){
		    	mBuilder.append(discountText);
		    	mBuilder.append(createHorizontalSpace(
		    			calculateLength(discountText) + 
		    			calculateLength(strTotalDiscount)));
		    	mBuilder.append(strTotalDiscount + "\n");
	    	}
	    	
	    	// transaction exclude vat
	    	if(trans.getTransactionVatExclude() > 0){
	    		String vatExcludeText = mContext.getString(R.string.vat) + " " +
	    				mFormat.currencyFormat(mShop.getCompanyVatRate(), "#,###.##") + "%";
	    		String strVatExclude = mFormat.currencyFormat(trans.getTransactionVatExclude());
	    		mBuilder.append(vatExcludeText);
	    		mBuilder.append(createHorizontalSpace(
	    				calculateLength(vatExcludeText) + 
	    				calculateLength(strVatExclude)));
	    		mBuilder.append(strVatExclude + "\n");
	    	}
	    	
	    	// total price
	    	mBuilder.append(totalText);
	    	mBuilder.append(createHorizontalSpace(
	    			calculateLength(totalText) + 
	    			calculateLength(strTotalSale)));
	    	mBuilder.append(strTotalSale + "\n");

	    	// total payment
	    	List<Payment.PaymentDetail> paymentLst = 
	    			mPayment.listPaymentGroupByType(transactionId);
	    	for(int i = 0; i < paymentLst.size(); i++){
	    		Payment.PaymentDetail payment = paymentLst.get(i);
		    	String strTotalPaid = mFormat.currencyFormat(payment.getPaid());
		    	if(payment.getPayTypeID() == PaymentDetail.PAY_TYPE_CREDIT){
		    		String paymentText = payment.getPayTypeName();
		    		String cardNoText = "xxxx xxxx xxxx ";
		    		try {
		    			paymentText = payment.getPayTypeName() + ":" + 
	    					mCreditCard.getCreditCardType(payment.getCreditCardType());
		    			cardNoText += payment.getCreaditCardNo().substring(12, 16);
		    		} catch (Exception e) {
		    			Logger.appendLog(mContext, Utils.LOG_DIR, 
		    					Utils.LOG_FILE_NAME, "Error gen creditcard no : " + e.getMessage());
		    		}
		    		mBuilder.append(paymentText);
		    		mBuilder.append(createHorizontalSpace(calculateLength(paymentText)));
		    		mBuilder.append("\n");
	    			mBuilder.append(cardNoText);
	    			mBuilder.append(createHorizontalSpace(
	    					calculateLength(cardNoText) + 
	    					calculateLength(strTotalPaid)));
	    			mBuilder.append(strTotalPaid);
		    	}else{
		    		String paymentText = payment.getPayTypeName() + " ";
			    	if(i < paymentLst.size() - 1){
				    	mBuilder.append(paymentText);
			    		mBuilder.append(createHorizontalSpace(
			    				calculateLength(paymentText) + 
			    				calculateLength(strTotalPaid)));
				    	mBuilder.append(strTotalPaid);
			    	}else if(i == paymentLst.size() - 1){
				    	if(change > 0){
					    	mBuilder.append(paymentText);
					    	mBuilder.append(strTotalPaid);
				    		mBuilder.append(createHorizontalSpace(
				    				calculateLength(changeText) + 
				    				calculateLength(strTotalChange) + 
				    				calculateLength(paymentText) + 
				    				calculateLength(strTotalPaid)));
					    	mBuilder.append(changeText);
					    	mBuilder.append(strTotalChange);
					    }else{
					    	mBuilder.append(paymentText);
				    		mBuilder.append(createHorizontalSpace(
				    				calculateLength(paymentText) + 
				    				calculateLength(strTotalPaid)));
					    	mBuilder.append(strTotalPaid);
					    }
			    	}
		    	}
	    		mBuilder.append("\n");
	    	}
		    mBuilder.append(createLine("=") + "\n");
		    
		    if(mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
			    // before vat
			    mBuilder.append(beforeVatText);
			    mBuilder.append(createHorizontalSpace(
			    		calculateLength(beforeVatText) + 
			    		calculateLength(strBeforeVat)));
			    mBuilder.append(strBeforeVat + "\n");
			    
			    // transaction vat
		    	mBuilder.append(vatRateText);
		    	mBuilder.append(createHorizontalSpace(
		    			calculateLength(vatRateText) + 
		    			calculateLength(strTransactionVat)));
		    	mBuilder.append(strTransactionVat + "\n");
		    }
		    
	    	// add footer
	    	for(ShopData.HeaderFooterReceipt hf : 
				mHeaderFooter.listHeaderFooter(HeaderFooterReceipt.FOOTER_LINE_TYPE)){
	    		mBuilder.append("<c>");
				mBuilder.append(hf.getTextInLine());
				mBuilder.append("\n");
			}
		}

		@Override
		public void prepareDataToPrint() {
		}
		
	}
	
	@Override
	protected void onPostExecute(Void result) {
		mPrintListener.onPrintSuccess();
	}

	@Override
	protected void onPreExecute() {
		mPrintListener.onPrepare();
	}

	@Override
	protected Void doInBackground(Void... params) {
		PrintReceiptLog printLog = new PrintReceiptLog(mContext.getApplicationContext());
		for(PrintReceiptLog.PrintReceipt printReceipt : printLog.listPrintReceiptLog()){
			try {
				if(Utils.isInternalPrinterSetting(mContext)){
					WintecPrintReceipt wt = new WintecPrintReceipt(mContext);
					wt.prepareDataToPrint(printReceipt.getTransactionId());
					wt.print();
				}else{
					EPSONPrintReceipt ep = new EPSONPrintReceipt(mContext);
					ep.prepareDataToPrint(printReceipt.getTransactionId());
					ep.print();
				}
				printLog.deletePrintStatus(printReceipt.getPriceReceiptLogId());
				
			} catch (Exception e) {
				printLog.updatePrintStatus(printReceipt.getPriceReceiptLogId(), PrintReceiptLog.PRINT_NOT_SUCCESS);
				Logger.appendLog(mContext, 
						Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
						" Print receipt fail : " + e.getMessage());
				mPrintListener.onPrintFail(e.getMessage());
			}
		}
		return null;
	}
	
	public static interface PrintStatusListener{
		void onPrepare();
		void onPrintSuccess();
		void onPrintFail(String msg);
	}
}
