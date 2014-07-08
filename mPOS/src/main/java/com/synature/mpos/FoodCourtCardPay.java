package com.synature.mpos;

import org.ksoap2.serialization.PropertyInfo;

import com.google.gson.JsonSyntaxException;
import com.synature.pos.PrepaidCardInfo;
import com.synature.pos.WebServiceResult;

import android.content.Context;
import android.text.TextUtils;

public class FoodCourtCardPay extends FoodCourtMainService{
	
	private FoodCourtWebServiceListener mListener;
	
	/**
	 * @param context
	 * @param shopId
	 * @param computerId
	 * @param staffId
	 * @param cardNo
	 * @param payAmount
	 * @param listener
	 */
	public FoodCourtCardPay(Context context, int shopId,
			int computerId, int staffId, String cardNo, String payAmount, 
			FoodCourtWebServiceListener listener) {
		super(context, PAY_METHOD, shopId, computerId, staffId, cardNo);

		mListener = listener;
		
		mProperty = new PropertyInfo();
		mProperty.setName(PAY_AMOUNT_PARAM);
		mProperty.setValue(payAmount);
		mProperty.setType(String.class);
		mSoapRequest.addProperty(mProperty);
	}

	@Override
	protected void onPreExecute() {
		mListener.onPre();
	}

	@Override
	protected void onPostExecute(String result) {
		WebServiceResult ws;
		try {
			ws = toServiceObject(result);
			if(ws.getiResultID() == RESPONSE_SUCCESS){
				try {
					PrepaidCardInfo cardInfo = toPrepaidCardInfoObject(ws.getSzResultData());
					mListener.onPost(cardInfo);
				} catch (Exception e) {
					mListener.onError(e.getMessage());
				}
			}else{
				mListener.onError(TextUtils.isEmpty(ws.getSzResultData()) ? result : ws.getSzResultData());
			}
		} catch (JsonSyntaxException e) {
			mListener.onError(result);
		}
	}

}
