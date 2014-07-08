package com.synature.util;

public class VerifyCardType {
	
	public static enum CardType{
		VISA,
		MASTER,
		AMERICAN
	};
	
	public static CardType checkCardType(String cardNumber){
		if(cardNumber.matches("^4[0-9]{6,}$")){
			return CardType.VISA;
		}else if(cardNumber.matches("^5[1-5][0-9]{5,}$")){
			return CardType.MASTER;
		}else if(cardNumber.matches("^3[47][0-9]{5,}$")){
			return CardType.AMERICAN;
		}
		return null;
	}
}
