package com.synature.mpos;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.pos.OrderTransaction;

/**
 * @author j1tth4
 * bill adapter
 */
public abstract class OrderTransactionAdapter extends BaseAdapter {
	
	protected List<MPOSOrderTransaction> mTransLst;
	protected LayoutInflater mInflater;

	public OrderTransactionAdapter(Context c, List<MPOSOrderTransaction> transLst) {
		mTransLst = transLst;
		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mTransLst != null ? mTransLst.size() : 0;
	}

	@Override
	public OrderTransaction getItem(int position) {
		return mTransLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
