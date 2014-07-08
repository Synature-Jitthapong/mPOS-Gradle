package com.synature.mpos;

import com.google.gson.JsonSyntaxException;
import com.synature.pos.PrepaidCardInfo;
import com.synature.pos.WebServiceResult;

import android.content.Context;
import android.text.TextUtils;

public class FoodCourtBalanceOfCard extends FoodCourtMainService{

	public FoodCourtWebServiceListener mListener;
	
	/**
	 * @param context
	 * @param method
	 * @param shopId
	 * @param computerId
	 * @param staffId
	 * @param cardNo
	 * @param listener
	 */
	public FoodCourtBalanceOfCard(Context context, int shopId,
			int computerId, int staffId, String cardNo, 
			FoodCourtWebServiceListener listener) {
		super(context, GET_BALANCE_METHOD, shopId, computerId, staffId, cardNo);
		mListener = listener;
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
