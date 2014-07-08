package com.synature.mpos;

import android.content.Context;

import com.epson.eposprint.BatteryStatusChangeEventListener;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.eposprint.Print;
import com.epson.eposprint.StatusChangeEventListener;

public abstract class EPSONPrinter extends PrinterUtility implements 
	BatteryStatusChangeEventListener, StatusChangeEventListener{
	
	protected Context mContext;
	protected Print mPrinter;
	protected Builder mBuilder;
	
	public EPSONPrinter(Context context){
		mContext = context;
		mPrinter = new Print(context.getApplicationContext());
		mPrinter.setStatusChangeEventCallback(this);
		mPrinter.setBatteryStatusChangeEventCallback(this);
		
		open();
		createBuilder();
	}
	
	protected boolean open(){
		try {
			mPrinter.openPrinter(Print.DEVTYPE_TCP, Utils.getPrinterIp(mContext), 0, 1000);
			return true;
		} catch (EposException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	protected boolean createBuilder(){
		try {
			mBuilder = new Builder(Utils.getEPSONModelName(mContext), Builder.MODEL_ANK, 
					mContext);
			mBuilder.addTextSize(1, 1);
			
			if(Utils.getEPSONPrinterFont(mContext).equals("a")){
				mBuilder.addTextFont(Builder.FONT_A);
			}else if(Utils.getEPSONPrinterFont(mContext).equals("b")){
				mBuilder.addTextFont(Builder.FONT_B);
			}

			return true;
		} catch (EposException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public abstract void prepareDataToPrint(int transactionId);
	public abstract void prepareDataToPrint();
	
	protected void print(){
		// send mBuilder data
		int[] status = new int[1];
		int[] battery = new int[1];
		try {
			mBuilder.addFeedUnit(30);
			mBuilder.addCut(Builder.CUT_FEED);
			mPrinter.sendData(mBuilder, 10000, status, battery);
		} catch (EposException e) {
			e.printStackTrace();
		}
		if (mBuilder != null) {
			mBuilder.clearCommandBuffer();
		}

		// close printer
		try {
			mPrinter.closePrinter();
			mPrinter = null;
		} catch (EposException e) {
			e.printStackTrace();
		}
	}
}
