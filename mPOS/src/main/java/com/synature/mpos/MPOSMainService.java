package com.synature.mpos;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.synature.pos.WebServiceResult;
import com.synature.util.Ksoap2WebServiceTask;


public class MPOSMainService extends Ksoap2WebServiceTask{
	
	public static final String NAME_SPACE = "http://tempuri.org/";
	
	// webservice method
	public static final String CHECK_DEVICE_METHOD = "WSmPOS_CheckAuthenShopDevice";
	public static final String LOAD_MENU_METHOD = "WSmPOS_JSON_LoadMenuDataV2";
	public static final String LOAD_PRODUCT_METHOD = "WSmPOS_JSON_LoadProductDataV2";
	public static final String LOAD_SHOP_METHOD = "WSmPOS_JSON_LoadShopData";
	public static final String SEND_SALE_TRANS_METHOD = "WSmPOS_JSON_SendSaleAllTransactionDataWithEndDay";
	public static final String SEND_PARTIAL_SALE_TRANS_METHOD = "WSmPOS_JSON_SendSalePartialTransactionData";
	public static final String SEND_STOCK_METHOD = "WSmPOS_JSON_SendInventoryDocumentData";
	
	/**
	 * The response of WebServiceResult
	 * 0 success -1 error
	 */
	public static final int RESPONSE_SUCCESS = 0;
	public static final int RESPONSE_ERROR = -1;
	
	public static final String SHOP_ID_PARAM = "iShopID";
	public static final String COMPUTER_ID_PARAM = "iComputerID";
	public static final String STAFF_ID_PARAM = "iStaffID";
	public static final String DEVICE_CODE_PARAM = "szDeviceCode";
	public static final String JSON_SALE_PARAM = "szJsonSaleTransData";
	
	
	public MPOSMainService(Context context, String method) {
		super(context.getApplicationContext(), NAME_SPACE, 
				method, Utils.getConnectionTimeOut(context));
		
		mProperty = new PropertyInfo();
		mProperty.setName(DEVICE_CODE_PARAM);
		mProperty.setValue(Utils.getDeviceCode(context));
		mProperty.setType(String.class);
		mSoapRequest.addProperty(mProperty);
	}

	public WebServiceResult toServiceObject(String json) throws JsonSyntaxException{
		Gson gson = new Gson();
		Type type = new TypeToken<WebServiceResult>(){}.getType();
		WebServiceResult ws = gson.fromJson(json, type);
		return ws;
	}
}
