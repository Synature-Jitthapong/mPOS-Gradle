package com.synature.pos;

public class ReasonItem {
	public static class POSData_ReasonItem
    {
        private int iReasonID;
        private String szReasonName;
		
        public int getiReasonID() {
			return iReasonID;
		}
		public void setiReasonID(int iReasonID) {
			this.iReasonID = iReasonID;
		}
		public String getSzReasonName() {
			return szReasonName;
		}
		public void setSzReasonName(String szReasonName) {
			this.szReasonName = szReasonName;
		}
    }
}
