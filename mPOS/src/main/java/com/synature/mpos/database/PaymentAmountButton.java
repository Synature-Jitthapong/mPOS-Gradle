package com.synature.mpos.database;

import java.util.ArrayList;
import java.util.List;

import com.synature.mpos.database.table.PaymentButtonTable;
import com.synature.pos.Payment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PaymentAmountButton extends MPOSDatabase {
	
	public PaymentAmountButton(Context context) {
		super(context);
	}
	
	/**
	 * @return List<Payment.PaymentAmountButton>
	 */
	public List<Payment.PaymentAmountButton> listPaymentButton(){
		List<Payment.PaymentAmountButton> paymentButtonLst = 
				new ArrayList<Payment.PaymentAmountButton>();
		Cursor cursor = getReadableDatabase().query(PaymentButtonTable.TABLE_PAYMENT_BUTTON, 
				new String[]{
				PaymentButtonTable.COLUMN_PAYMENT_AMOUNT_ID,
				PaymentButtonTable.COLUMN_PAYMENT_AMOUNT
				},
				null, null, null, null, 
				PaymentButtonTable.COLUMN_ORDERING);
		if(cursor.moveToFirst()){
			do{
				Payment.PaymentAmountButton payButton = 
						new Payment.PaymentAmountButton();
				payButton.setPaymentAmountID(cursor.getInt(cursor.getColumnIndex(PaymentButtonTable.COLUMN_PAYMENT_AMOUNT_ID)));
				payButton.setPaymentAmount(cursor.getDouble(cursor.getColumnIndex(PaymentButtonTable.COLUMN_PAYMENT_AMOUNT)));
				paymentButtonLst.add(payButton);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return paymentButtonLst;
	}
	
	/**
	 * @param paymentAmountLst
	 */
	public void insertPaymentAmountButton(List<Payment.PaymentAmountButton> paymentAmountLst){
		getWritableDatabase().beginTransaction();
		try {
			getWritableDatabase().delete(PaymentButtonTable.TABLE_PAYMENT_BUTTON, null, null);
			for(Payment.PaymentAmountButton payButton : paymentAmountLst){
				ContentValues cv = new ContentValues();
				cv.put(PaymentButtonTable.COLUMN_PAYMENT_AMOUNT_ID, payButton.getPaymentAmountID());
				cv.put(PaymentButtonTable.COLUMN_PAYMENT_AMOUNT, payButton.getPaymentAmount());
				getWritableDatabase().insertOrThrow(PaymentButtonTable.TABLE_PAYMENT_BUTTON, null, cv);
			}
			getWritableDatabase().setTransactionSuccessful();
		} finally {
			getWritableDatabase().endTransaction();
		}
	}
}
