package com.synature.pos;

public class BankName {
	private int BankNameID;
	private String BankName;
	
	public BankName(){
		
	}
	
	public BankName(int id, String name){
		this.BankNameID = id;
		this.BankName = name;
	}
	
	public int getBankNameId() {
		return BankNameID;
	}
	public void setBankNameId(int bankNameId) {
		this.BankNameID = bankNameId;
	}
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		this.BankName = bankName;
	}

	@Override
	public String toString() {
		return BankName;
	}
}
