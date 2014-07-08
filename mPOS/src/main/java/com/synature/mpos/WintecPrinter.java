package com.synature.mpos;

import android.content.Context;
import android.text.TextUtils;

import com.synature.util.ThaiLevelText;
import com.synature.util.ThaiLevelText.OPOSThaiText;

import cn.wintec.wtandroidjar2.ComIO;
import cn.wintec.wtandroidjar2.Printer;

public abstract class WintecPrinter extends PrinterUtility{
	
	/**
	 * ISO8859-11 character
	 */
	public static final String ISO_8859_11 = "x-iso-8859-11";
	
	/**
	 * IBM 874
	 */
	public static final String CP_874 = "Cp874";
	
	protected Context mContext;
	protected StringBuilder mBuilder;
	protected Printer mPrinter;
	
	public WintecPrinter(Context context){
		mContext = context;
		mPrinter = new Printer(Utils.getWintecPrinterDevPath(mContext), 
				ComIO.Baudrate.valueOf(Utils.getWintecPrinterBaudRate(mContext)));
		mPrinter.PRN_DisableChinese();
		mPrinter.PRN_SetCodePage(70);
		mBuilder = new StringBuilder();
	}

	protected void print(){
		String[] subElement = mBuilder.toString().split("\n");
    	for(String data : subElement){
//    		if(!data.contains("<b>")){
//	    		mPrinter.PRN_EnableBoldFont(0);
//    		}
//    		if(!data.contains("<u>")){
//	    		mPrinter.PRN_DisableFontUnderline();
//    		}
			if(data.contains("<c>")){
				data = adjustAlignCenter(data.replace("<c>", ""));
			}
			if(data.contains("<b>")){
				//mPrinter.PRN_EnableBoldFont(1);
				data = data.replace("<b>", "");
			}
			if(data.contains("<u>")){
				//mPrinter.PRN_EnableFontUnderline();
				data = data.replace("<u>", "");
			}

    		OPOSThaiText supportThai = ThaiLevelText.parsingThaiLevel(data);
    		if(!TextUtils.isEmpty(supportThai.TextLine1))
    			mPrinter.PRN_Print(supportThai.TextLine1, CP_874);
    		mPrinter.PRN_Print(supportThai.TextLine2, CP_874);
    		if(!TextUtils.isEmpty(supportThai.TextLine3))
    			mPrinter.PRN_Print(supportThai.TextLine3, CP_874);
		}
    	mPrinter.PRN_PrintAndFeedLine(6);		
    	mPrinter.PRN_HalfCutPaper();
    	close();
	}
	
	private void close(){
    	mPrinter.PRN_Close();
	}
	
	private static String adjustAlignCenter(String text){
		int rimSpace = (HORIZONTAL_MAX_SPACE - text.length()) / 2;
		StringBuilder empText = new StringBuilder();
		for(int i = 0; i < rimSpace; i++){
			empText.append(" ");
		}
		return empText.toString() + text + empText.toString();
	}
	
	public abstract void prepareDataToPrint(int transactionId);
	public abstract void prepareDataToPrint();
}
