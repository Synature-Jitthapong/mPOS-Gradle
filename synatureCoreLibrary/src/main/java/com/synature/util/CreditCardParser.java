package com.synature.util;

public class CreditCardParser {
	
	public static final String END_SENTINEL1 = "\\?";
	
	public static final String FIELD_SEPERATOR = "\\^";
	
	private String mCardNo;
	private String mCardHolderName;
	private String mCardExpDate;
	
	public boolean parser(String content){
		if(content != null && !content.equals("")){
			String[] track = content.split(END_SENTINEL1);
			if(track[0].split(":")[0].equals("Track2")){
				parserDebit(track[0]);
				return true;
			}else{
				String track1 = track[0].trim();
				parserCredit(track1);
				return true;
			}
		}
		return false;
	}
	
	private void parserDebit(String track){
		String dataTrack[] = track.split("=");
		mCardNo = dataTrack[0].substring(7);
		mCardHolderName = "";
		mCardExpDate = "";
	}
	
	private void parserCredit(String track1){
		String dataTrack1[] = track1.split(FIELD_SEPERATOR);
		mCardNo = dataTrack1[0].substring(8);
		mCardHolderName = dataTrack1[1].trim();
		mCardExpDate = dataTrack1[2].substring(0, 4);	
	}
	
	public String getExpDate(){
		return mCardExpDate;
	}
	
	public String getCardHolderName(){
		return mCardHolderName;
	}
	
	public String getCardNo(){
		return mCardNo;
	}
}
