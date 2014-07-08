package com.synature.mpos;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.synature.pos.PrepaidCardInfo;

import android.content.Context;

public abstract class FoodCourtMainService extends MPOSMainService{

	public static final String GET_BALANCE_METHOD = "WSiFoodCourt_JSON_GetBalanceAmountFromCardNo";
	public static final String PAY_METHOD = "WSiFoodCourt_JSON_PayAmountOfCardNo";
	
	public static final String CARD_NO_PARAM = "szCardNo";
	public static final String PAY_AMOUNT_PARAM = "fPayAmount";
	
	public FoodCourtMainService(Context context, String method, int shopId, 
			int computerId, int staffId, String cardNo) {
		super(context, method);

		mProperty = new PropertyInfo();
		mProperty.setName(SHOP_ID_PARAM);
		mProperty.setValue(shopId);
		mProperty.setType(int.class);
		mSoapRequest.addProperty(mProperty);

		mProperty = new PropertyInfo();
		mProperty.setName(COMPUTER_ID_PARAM);
		mProperty.setValue(computerId);
		mProperty.setType(int.class);
		mSoapRequest.addProperty(mProperty);

		mProperty = new PropertyInfo();
		mProperty.setName(STAFF_ID_PARAM);
		mProperty.setValue(staffId);
		mProperty.setType(int.class);
		mSoapRequest.addProperty(mProperty);

		mProperty = new PropertyInfo();
		mProperty.setName(CARD_NO_PARAM);
		mProperty.setValue(cardNo);
		mProperty.setType(String.class);
		mSoapRequest.addProperty(mProperty);
	}

	protected PrepaidCardInfo toPrepaidCardInfoObject(String json) throws JsonSyntaxException{
		Gson gson = new Gson();
		Type type = new TypeToken<PrepaidCardInfo>(){}.getType();
		PrepaidCardInfo card = gson.fromJson(json, type);
		return card;
	}
	
	public static interface FoodCourtWebServiceListener{
		void onPre();
		void onPost(PrepaidCardInfo cardInfo);
		void onError(String msg);
	}
}
