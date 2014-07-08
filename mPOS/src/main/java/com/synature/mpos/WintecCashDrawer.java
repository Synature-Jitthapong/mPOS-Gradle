package com.synature.mpos;

import android.content.Context;
import cn.wintec.wtandroidjar2.ComIO;
import cn.wintec.wtandroidjar2.Drw;

public class WintecCashDrawer{
	
	private Drw mDrw;
	
	public WintecCashDrawer(Context context){
		mDrw = new Drw(Utils.getWintecDrwDevPath(context), 
				ComIO.Baudrate.valueOf(Utils.getWintecDrwBaudRate(context)));
	}
	
	public void openCashDrawer(){
		mDrw.DRW_Open();
	}
	
	public void close(){
		mDrw.DRW_Close();
	}
}
