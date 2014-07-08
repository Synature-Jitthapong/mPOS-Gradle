package com.synature.pos;

public class PrepaidCardInfo {
	private int iCardID;
	private String szCardNo;
	private String szCardUDID;
	private int iCardStatus;
	private double fCurrentAmount;
	private double fDepositAmount;
	private int iMemberID;
	private String dExpireDate;
	
	public int getiCardID() {
		return iCardID;
	}
	public void setiCardID(int iCardID) {
		this.iCardID = iCardID;
	}
	public String getSzCardNo() {
		return szCardNo;
	}
	public void setSzCardNo(String szCardNo) {
		this.szCardNo = szCardNo;
	}
	public String getSzCardUDID() {
		return szCardUDID;
	}
	public void setSzCardUDID(String szCardUDID) {
		this.szCardUDID = szCardUDID;
	}
	public int getiCardStatus() {
		return iCardStatus;
	}
	public void setiCardStatus(int iCardStatus) {
		this.iCardStatus = iCardStatus;
	}
	public double getfCurrentAmount() {
		return fCurrentAmount;
	}
	public void setfCurrentAmount(double fCurrentAmount) {
		this.fCurrentAmount = fCurrentAmount;
	}
	public double getfDepositAmount() {
		return fDepositAmount;
	}
	public void setfDepositAmount(double fDepositAmount) {
		this.fDepositAmount = fDepositAmount;
	}
	public int getiMemberID() {
		return iMemberID;
	}
	public void setiMemberID(int iMemberID) {
		this.iMemberID = iMemberID;
	}
	public String getdExpireDate() {
		return dExpireDate;
	}
	public void setdExpireDate(String dExpireDate) {
		this.dExpireDate = dExpireDate;
	}
}
