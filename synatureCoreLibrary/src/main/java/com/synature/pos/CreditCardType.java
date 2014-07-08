package com.synature.pos;

public class CreditCardType {
	private int CreditCardTypeID;
	private String CreditCardTypeName;
	
	public CreditCardType(){
		
	}
	
	public CreditCardType(int type, String name){
		this.CreditCardTypeID = type;
		this.CreditCardTypeName = name;
	}

	public int getCreditCardTypeId() {
		return CreditCardTypeID;
	}

	public void setCreditCardTypeId(int creditCardTypeId) {
		this.CreditCardTypeID = creditCardTypeId;
	}

	public String getCreditCardTypeName() {
		return CreditCardTypeName;
	}

	public void setCreditCardTypeName(String creditCardTypeName) {
		this.CreditCardTypeName = creditCardTypeName;
	}

	@Override
	public String toString() {
		return CreditCardTypeName;
	}
	
}
