package com.synature.mpos;

import java.text.ParseException;

import android.content.Context;
import cn.wintec.wtandroidjar2.ComIO;
import cn.wintec.wtandroidjar2.DspPos;

public class WintecCustomerDisplay{
	public static final int MAX_TEXT_LENGTH = 20;
	public static final int LIMIT_LENGTH = 10;
	
	private Context mContext;
	
	private DspPos mDsp;
	
	private String orderName;
	private String orderQty;
	private String orderPrice;
	private String orderTotalQty;
	private String orderTotalPrice;
	
	public WintecCustomerDisplay(Context context){
		mDsp = new DspPos(Utils.getWintecDspPath(context), 
				ComIO.Baudrate.valueOf(Utils.getWintecDspBaudRate(context)));
		mContext = context;
	}
	
	public void displayTotalPay(String totalPay, String change){
		clearScreen();
		mDsp.DSP_Dispay(mContext.getString(R.string.cash));
		mDsp.DSP_MoveCursor(1, MAX_TEXT_LENGTH - totalPay.length());
		mDsp.DSP_Dispay(totalPay);
		try {
			if(Utils.stringToDouble(change) > 0){
				mDsp.DSP_MoveCursorDown();
				mDsp.DSP_MoveCursorEndLeft();
				mDsp.DSP_Dispay(mContext.getString(R.string.change));
				mDsp.DSP_MoveCursor(2, MAX_TEXT_LENGTH - change.length());
				mDsp.DSP_Dispay(change);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayOrder() throws Exception{
		if(orderName.length() > LIMIT_LENGTH){
			orderName = limitString(orderName);
		}
		clearScreen();
		String combindText = orderQty + "@" + orderPrice;
		mDsp.DSP_Dispay(orderName);
		mDsp.DSP_MoveCursor(1, MAX_TEXT_LENGTH - combindText.length());
		mDsp.DSP_Dispay(combindText);
		mDsp.DSP_MoveCursorDown();
		mDsp.DSP_MoveCursorEndLeft();
		mDsp.DSP_Dispay(mContext.getString(R.string.total));
		mDsp.DSP_MoveCursor(2, MAX_TEXT_LENGTH - orderTotalPrice.length());
		mDsp.DSP_Dispay(orderTotalPrice);
	}
	
	public void displayWelcome(){
		clearScreen();
		mDsp.DSP_Dispay(mContext.getString(R.string.welcome_to));
		mDsp.DSP_MoveCursorDown();
		mDsp.DSP_MoveCursorEndLeft();
		mDsp.DSP_Dispay(mContext.getString(R.string.promise_system));
	}
	
	private String limitString(String text){
		return text.substring(0, LIMIT_LENGTH - 3) + "...";
	}
	
	public void clearScreen(){
		mDsp.DSP_ClearScreen();
	}
	
	public void close(){
		mDsp.DSP_Close();
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getOrderTotalQty() {
		return orderTotalQty;
	}

	public void setOrderTotalQty(String orderTotalQty) {
		this.orderTotalQty = orderTotalQty;
	}

	public String getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(String orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
}
