package com.synature.mpos;

import android.content.Context;
import cn.wintec.wtandroidjar2.ComIO;
import cn.wintec.wtandroidjar2.Msr;

public class WintecMagneticReader{

	private byte mEnterTrack1;
	private byte mEnterTrack2;
	private byte mEnterTrack3;
	
	private Msr mMsr;
	
	public WintecMagneticReader(Context context){
		mMsr = new Msr(Utils.getWintecMsrDevPath(context), 
				ComIO.Baudrate.valueOf(Utils.getWintecMsrBaudRate(context)));
	}
	
	public String getTrackData(){
		return mMsr.MSR_GetTrackData(mEnterTrack1, mEnterTrack2, mEnterTrack3);
	}
	
	public void close(){
		mMsr.MSR_Close();
	}
}
