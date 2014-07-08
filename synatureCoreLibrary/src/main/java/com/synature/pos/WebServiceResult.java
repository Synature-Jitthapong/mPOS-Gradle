package com.synature.pos;

public class WebServiceResult {
	public static final int SUCCESS_STATUS = 0;
	public static final int FAIL_STATUS = -1;
	
	private int iResultID;
	private String szResultData;
	private int iExtraInteger;
	private String szExtraString;

	public int getiResultID() {
		return iResultID;
	}
	public void setiResultID(int iResultID) {
		this.iResultID = iResultID;
	}
	public String getSzResultData() {
		return szResultData;
	}
	public void setSzResultData(String szResultData) {
		this.szResultData = szResultData;
	}
	public int getiExtraInteger() {
		return iExtraInteger;
	}
	public void setiExtraInteger(int iExtraInteger) {
		this.iExtraInteger = iExtraInteger;
	}
	public String getSzExtraString() {
		return szExtraString;
	}
	public void setSzExtraString(String szExtraString) {
		this.szExtraString = szExtraString;
	}
	
	public class WebServiceVersion{
		private String WS_Version;
		private String szBuildVersion;
		private String dtBuildVersion;
		
		public String getWS_Version() {
			return WS_Version;
		}
		public void setWS_Version(String wS_Version) {
			WS_Version = wS_Version;
		}
		public String getSzBuildVersion() {
			return szBuildVersion;
		}
		public void setSzBuildVersion(String szBuildVersion) {
			this.szBuildVersion = szBuildVersion;
		}
		public String getDtBuildVersion() {
			return dtBuildVersion;
		}
		public void setDtBuildVersion(String dtBuildVersion) {
			this.dtBuildVersion = dtBuildVersion;
		}
	}
}
