package com.synature.pos;

import java.util.List;

public class POSData_OrderTransInfo {
	public static class POSData_CommentInfo{
		private int iCommentID;
		private double fCommentQty;
		private double fCommentPrice;
		private double fDiscountPrice; // Per Item
		
		public int getiCommentID() {
			return iCommentID;
		}
		public void setiCommentID(int iCommentID) {
			this.iCommentID = iCommentID;
		}
		public double getfCommentQty() {
			return fCommentQty;
		}
		public void setfCommentQty(double fCommentQty) {
			this.fCommentQty = fCommentQty;
		}
		public double getfCommentPrice() {
			return fCommentPrice;
		}
		public void setfCommentPrice(double fCommentPrice) {
			this.fCommentPrice = fCommentPrice;
		}
		public double getfDiscountPrice() {
			return fDiscountPrice;
		}
		public void setfDiscountPrice(double fDiscountPrice) {
			this.fDiscountPrice = fDiscountPrice;
		}
	}
	
	public static class POSData_PaymentAmount
	{
		private int iPaymentAmountID;
		private int iAmount;
		public int getiPaymentAmountID() {
			return iPaymentAmountID;
		}
		public void setiPaymentAmountID(int iPaymentAmountID) {
			this.iPaymentAmountID = iPaymentAmountID;
		}
		public int getiAmount() {
			return iAmount;
		}
		public void setiAmount(int iAmount) {
			this.iAmount = iAmount;
		}
	}
	
	public static class POSData_ChildOrderSetLinkType7Info
    {
        private int iProductID;
        private double fProductPrice;
        private double fProductQty;
        private String szOrderComment;
        public List<POSData_CommentInfo> xListCommentInfo;
        private int iPGroupID;       // For child type 7
        private int iSetGroupNo;     // For child type 7
		public int getiProductID() {
			return iProductID;
		}
		public void setiProductID(int iProductID) {
			this.iProductID = iProductID;
		}
		public double getfProductPrice() {
			return fProductPrice;
		}
		public void setfProductPrice(double fProductPrice) {
			this.fProductPrice = fProductPrice;
		}
		public double getfProductQty() {
			return fProductQty;
		}
		public void setfProductQty(double fProductQty) {
			this.fProductQty = fProductQty;
		}
		public String getSzOrderComment() {
			return szOrderComment;
		}
		public void setSzOrderComment(String szOrderComment) {
			this.szOrderComment = szOrderComment;
		}
		public int getiPGroupID() {
			return iPGroupID;
		}
		public void setiPGroupID(int iPGroupID) {
			this.iPGroupID = iPGroupID;
		}
		public int getiSetGroupNo() {
			return iSetGroupNo;
		}
		public void setiSetGroupNo(int iSetGroupNo) {
			this.iSetGroupNo = iSetGroupNo;
		}
    }
	  
	public static class POSData_OrderItemInfo 
	{
		private int iProductID;
		private double fProductPrice;
		private double fProductQty;
		private double fDiscountPrice; // Per Item
		private int iSaleMode;
		private String szOrderComment;
		private int iSeatID;
		private int iCourseID;

		public List<POSData_CommentInfo> xListCommentInfo;
        public List<POSData_ChildOrderSetLinkType7Info> xListChildOrderSetLinkType7;

		public int getiCourseID() {
			return iCourseID;
		}
		public void setiCourseID(int iCourseID) {
			this.iCourseID = iCourseID;
		}
		public int getiSeatID() {
			return iSeatID;
		}
		public void setiSeatID(int iSeatID) {
			this.iSeatID = iSeatID;
		}
		public int getiSaleMode() {
			return iSaleMode;
		}
		public void setiSaleMode(int iSaleMode) {
			this.iSaleMode = iSaleMode;
		}
		public int getiProductID() {
			return iProductID;
		}
		public void setiProductID(int iProductID) {
			this.iProductID = iProductID;
		}
		public double getfProductPrice() {
			return fProductPrice;
		}
		public void setfProductPrice(double fProductPrice) {
			this.fProductPrice = fProductPrice;
		}
		public double getfProductQty() {
			return fProductQty;
		}
		public void setfProductQty(double fProductQty) {
			this.fProductQty = fProductQty;
		}
		public double getfDiscountPrice() {
			return fDiscountPrice;
		}
		public void setfDiscountPrice(double fDiscountPrice) {
			this.fDiscountPrice = fDiscountPrice;
		}
		public String getSzOrderComment() {
			return szOrderComment;
		}
		public void setSzOrderComment(String szOrderComment) {
			this.szOrderComment = szOrderComment;
		}
		public List<POSData_CommentInfo> getxListCommentInfo() {
			return xListCommentInfo;
		}
		public void setxListCommentInfo(List<POSData_CommentInfo> xListCommentInfo) {
			this.xListCommentInfo = xListCommentInfo;
		}
	}
}

