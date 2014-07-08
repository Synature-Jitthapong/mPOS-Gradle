package com.synature.mpos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.synature.exceptionhandler.ExceptionHandler;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.mpos.database.Products;
import com.synature.mpos.database.Shop;
import com.synature.mpos.database.Transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class DiscountActivity extends Activity{
	
	public static final int PRICE_DISCOUNT_TYPE = 1;
	public static final int PERCENT_DISCOUNT_TYPE = 2;
	public static final String DISCOUNT_FRAGMENT_TAG = "DiscountDialog";

	private Formater mFormat;
	private Transaction mTrans;
	private Products mProduct;
	private MPOSOrderTransaction.MPOSOrderDetail mOrder;
	private DiscountAdapter mDisAdapter;
	private List<MPOSOrderTransaction.MPOSOrderDetail> mOrderLst;
	
	private int mTransactionId;
	private double mTotalPrice;
	private int mPosition = -1;
	private boolean mIsEdited = false;
	private int mDisAllType = PERCENT_DISCOUNT_TYPE;	// default is percent discount
	private RadioGroup mRdoDisType;
	private EditText mTxtDisAll;
	private Button mBtnApplyDisAll;
	private MenuItem mItemConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/**
		 * Register ExceptinHandler for catch error when application crash.
		 */
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, 
				Utils.LOG_DIR, Utils.LOG_FILE_NAME));
		
		setContentView(R.layout.activity_discount);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View disAllView = inflater.inflate(R.layout.input_discount_layout, null);
		mBtnApplyDisAll = (Button) inflater.inflate(R.layout.button_action, null);
		mBtnApplyDisAll.setText(R.string.apply);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(4, params.topMargin, params.rightMargin, params.bottomMargin);
		((LinearLayout) disAllView).addView(mBtnApplyDisAll, params);
		actionBar.setCustomView(disAllView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		actionBar.setDisplayShowCustomEnabled(true);
		mRdoDisType = (RadioGroup) disAllView.findViewById(R.id.rdoDiscountType);
		mTxtDisAll = (EditText) disAllView.findViewById(R.id.txtDiscount);
		mTxtDisAll.clearFocus();
		mBtnApplyDisAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				discountAll();
			}
			
		});
		mTxtDisAll.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE){
					discountAll();
					return true;
				}
				return false;
			}
			
		});
		mRdoDisType.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rdo = (RadioButton) group.findViewById(checkedId);
				switch(checkedId){
				case R.id.rdoPrice:
					if(rdo.isChecked())
						mDisAllType = PRICE_DISCOUNT_TYPE;
					break;
				case R.id.rdoPercent:
					if(rdo.isChecked())
						mDisAllType = PERCENT_DISCOUNT_TYPE;
					break;
				}
			}
			
		});
	
		mTrans = new Transaction(this);
		mProduct = new Products(this);
		mFormat = new Formater(this);
		mOrder = new MPOSOrderTransaction.MPOSOrderDetail();
		
		mOrderLst = new ArrayList<MPOSOrderTransaction.MPOSOrderDetail>();
		mDisAdapter = new DiscountAdapter();
		
		Intent intent = getIntent();
		mTransactionId = intent.getIntExtra("transactionId", 0);

		mTrans.prepareDiscount(mTransactionId);
		
		if(savedInstanceState == null){
			getFragmentManager().beginTransaction().add(R.id.discountContainer, 
					PlaceholderFragment.newInstance()).commit();
		}
	}
	
	public void doPositiveClick(double discount, int discountType){
		if(updateDiscount(discount, discountType)){
			mItemConfirm.setVisible(true);
		}
	}
	
	public void doNegativeClick(){
		
	}
	
	public static class DiscountDialogFragment extends DialogFragment{
		private String mProductName;
		private double mDiscount;
		private double mTotalRetailPrice;
		private int mDiscountType;
		
		public static DiscountDialogFragment newInstance(String productName, 
				double discount, double totalRetailPrice, int discountType){
			DiscountDialogFragment f = new DiscountDialogFragment();
			Bundle b = new Bundle();
			b.putString("title", productName);
			b.putDouble("discount", discount);
			b.putDouble("totalRetailPrice", totalRetailPrice);
			b.putInt("discountType", discountType);
			f.setArguments(b);
			return f;
		}
		
		private void enterDiscount(EditText editText){
			double discount = mDiscount;
			try {
				discount = Utils.stringToDouble(editText.getText().toString());
				((DiscountActivity)getActivity()).doPositiveClick(discount, mDiscountType);
				getDialog().dismiss();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			mProductName = getArguments().getString("title");
			mDiscount = getArguments().getDouble("discount");
			mTotalRetailPrice = getArguments().getDouble("totalRetailPrice");
			mDiscountType = getArguments().getInt("discountType");
			if(mDiscountType == 0)
				mDiscountType = PERCENT_DISCOUNT_TYPE;
			
			LayoutInflater inflater = (LayoutInflater)
					getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.input_discount_layout, null);
			final EditText txtDiscount = (EditText) v.findViewById(R.id.txtDiscount);
			final RadioGroup rdoDiscountType = (RadioGroup) v.findViewById(R.id.rdoDiscountType);
			if(mDiscountType == PERCENT_DISCOUNT_TYPE)
				((RadioButton)rdoDiscountType.findViewById(R.id.rdoPercent)).setChecked(true);
			else if(mDiscountType == PRICE_DISCOUNT_TYPE)
				((RadioButton)rdoDiscountType.findViewById(R.id.rdoPrice)).setChecked(true);
			if(mDiscountType == PERCENT_DISCOUNT_TYPE)
				txtDiscount.setText(((DiscountActivity) getActivity()).mFormat.currencyFormat(mDiscount * 100 / mTotalRetailPrice));
			else
				txtDiscount.setText(((DiscountActivity) getActivity()).mFormat.currencyFormat(mDiscount));
			txtDiscount.setSelectAllOnFocus(true);
			txtDiscount.requestFocus();
			txtDiscount.setOnEditorActionListener(new OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_DONE){
						enterDiscount((EditText)v);
						return true;
					}
					return false;
				}
				
			});
			rdoDiscountType.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					RadioButton rdo = (RadioButton) group.findViewById(checkedId);
					switch(checkedId){
					case R.id.rdoPrice:
						if(rdo.isChecked())
							mDiscountType = PRICE_DISCOUNT_TYPE;
						break;
					case R.id.rdoPercent:
						if(rdo.isChecked())
							mDiscountType = PERCENT_DISCOUNT_TYPE;
						break;
					}
				}
				
			});
		
			return new AlertDialog.Builder(getActivity())
				.setTitle(mProductName)
				.setView(v)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						enterDiscount(txtDiscount);
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((DiscountActivity)getActivity()).doNegativeClick();
					}
				}).create();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancel();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	public static class PlaceholderFragment extends Fragment{

		private LinearLayout mLayoutVat;
		private ListView mLvDiscount;
		private EditText mTxtTotalVatExc;
		private EditText mTxtSubTotal;
		private EditText mTxtTotalDiscount;
		private EditText mTxtTotalPrice;
		
		public static PlaceholderFragment newInstance(){
			PlaceholderFragment f = new PlaceholderFragment();
			return f;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_discount, container, false);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			mLvDiscount.setAdapter(((DiscountActivity) getActivity()).mDisAdapter);
			mLvDiscount.setOnItemClickListener(new OnItemClickListener(){
		
				@Override
				public void onItemClick(AdapterView<?> parent, View v, final int position,
						final long id) {
					MPOSOrderTransaction.MPOSOrderDetail order = 
							(MPOSOrderTransaction.MPOSOrderDetail) parent.getItemAtPosition(position);
					
					Products p = new Products(getActivity());
					if(p.getProduct(order.getProductId()).getDiscountAllow() == 1){
						((DiscountActivity) getActivity()).mPosition = position;
						((DiscountActivity) getActivity()).mOrder = order;
						DiscountDialogFragment discount = 
								DiscountDialogFragment.newInstance(
										((DiscountActivity) getActivity()).mOrder.getProductName(), 
										((DiscountActivity) getActivity()).mOrder.getPriceDiscount(), 
										((DiscountActivity) getActivity()).mOrder.getTotalRetailPrice(), 
										((DiscountActivity) getActivity()).mOrder.getDiscountType());
						discount.show(getFragmentManager(), DISCOUNT_FRAGMENT_TAG);
					}else{
						new AlertDialog.Builder(getActivity())
						.setTitle(R.string.discount)
						.setMessage(R.string.not_allow_discount)
						.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mLvDiscount.setItemChecked(((DiscountActivity) getActivity()).mPosition, true);
							}
						})
						.show();
					}
				}
			});
			((DiscountActivity) getActivity()).loadOrder();
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			mLayoutVat = (LinearLayout) view.findViewById(R.id.layoutVat);
			mTxtTotalVatExc = (EditText) view.findViewById(R.id.txtTotalVatExclude);
			mLvDiscount = (ListView) view.findViewById(R.id.lvOrder);
			mTxtSubTotal = (EditText) view.findViewById(R.id.txtSubTotal);
			mTxtTotalDiscount = (EditText) view.findViewById(R.id.txtTotalDiscount);
			mTxtTotalPrice = (EditText) view.findViewById(R.id.txtTotalPrice);
			Shop shop = new Shop(getActivity());
			((TextView) view.findViewById(R.id.textView12)).append(" " + 
					NumberFormat.getInstance().format(shop.getCompanyVatRate()) + getString(R.string.percent));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			cancel();
			return true;
		case R.id.itemReset:
			clearDiscount();
			loadOrder();
			return true;
		case R.id.itemConfirm:
			mTrans.confirmDiscount(mTransactionId);
			finish();
			return true;
		default:
		return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_discount, menu);
		mItemConfirm = menu.findItem(R.id.itemConfirm);
		return true;
	}

	/*
	 * this following by 
	 * if discount = 200
	 * A 100 (100 / 1000) * 200 = x
	 * B 200 (200 / 1000) * 200 = y
	 * C 300 (300 / 1000) * 200 = z
	 * D 1000 = 200 - (x + y + z)
	 */
	private void discountAll(){
		if(!TextUtils.isEmpty(mTxtDisAll.getText())){
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mTxtDisAll.getWindowToken(), 0);
			// clear discount first
			clearDiscount();
			try {
				Products product = new Products(this);
				double discountAll = Utils.stringToDouble(mTxtDisAll.getText().toString());
				double maxTotalRetailPrice = mTrans.getMaxTotalRetailPrice(mTransactionId);
				double totalDiscount = 0.0d;
				MPOSOrderTransaction.MPOSOrderDetail summOrder = 
						mTrans.getSummaryOrderForDiscount(mTransactionId);
				if(discountAll <= summOrder.getTotalRetailPrice()){
					mTxtDisAll.setText(null);
					List<MPOSOrderTransaction.MPOSOrderDetail> orderLst = mOrderLst;
					for(MPOSOrderTransaction.MPOSOrderDetail order : orderLst){
						if(product.getProduct(order.getProductId()).getDiscountAllow() == 1){
							double totalRetailPrice = order.getTotalRetailPrice();
							if(mDisAllType == PRICE_DISCOUNT_TYPE){
								if(totalDiscount < discountAll){
									if(order.getTotalRetailPrice() < maxTotalRetailPrice){
										double discount = (totalRetailPrice / maxTotalRetailPrice) * discountAll;
										BigDecimal big = new BigDecimal(discount);
										big = big.setScale(0, BigDecimal.ROUND_FLOOR);
										discount = big.doubleValue();
										if(discount > order.getTotalRetailPrice())
											discount = order.getTotalRetailPrice();
										totalDiscount += discount;
										if(totalDiscount > discountAll){
											discount = discountAll - mTrans.getSummaryOrderForDiscount(mTransactionId).getPriceDiscount();
										}
										double totalPriceAfterDiscount = totalRetailPrice - discount;
										mTrans.discountEatchProduct(mTransactionId, order.getOrderDetailId(),
												order.getVatType(), mProduct.getVatRate(order.getProductId()), totalPriceAfterDiscount, 
												discount, PRICE_DISCOUNT_TYPE);
									}
								}
							}else if(mDisAllType == PERCENT_DISCOUNT_TYPE){
								double discount = calculateDiscount(order.getTotalRetailPrice(), discountAll, PERCENT_DISCOUNT_TYPE);
								double totalPriceAfterDiscount = totalRetailPrice - discount;
								if(discount >= 0){
									mTrans.discountEatchProduct(mTransactionId, order.getOrderDetailId(),
											order.getVatType(), mProduct.getVatRate(order.getProductId()), totalPriceAfterDiscount, 
											discount, PERCENT_DISCOUNT_TYPE);
								}
							}
						}
					}
					if(mDisAllType == PRICE_DISCOUNT_TYPE){
						Iterator<MPOSOrderTransaction.MPOSOrderDetail> it = orderLst.iterator();
						while(it.hasNext()){
							MPOSOrderTransaction.MPOSOrderDetail order = it.next();
							if(order.getTotalRetailPrice() == maxTotalRetailPrice){
								double totalRetailPrice = order.getTotalRetailPrice();
								if(totalDiscount < discountAll){
									// if totalDiscount == 0 that means totalRetailPrice of all item is same
									double discount = totalDiscount == 0 ? 
											(totalRetailPrice / summOrder.getTotalRetailPrice()) * discountAll
											: discountAll - totalDiscount;
									BigDecimal big = new BigDecimal(discount);
									big = big.setScale(0, BigDecimal.ROUND_FLOOR);
									discount = big.doubleValue();
									if(discount > order.getTotalRetailPrice())
										discount = order.getTotalRetailPrice();
									totalDiscount += discount;
									double totalPriceAfterDiscount = totalRetailPrice - discount;
									mTrans.discountEatchProduct(mTransactionId, order.getOrderDetailId(),
											order.getVatType(), mProduct.getVatRate(order.getProductId()), totalPriceAfterDiscount, 
											discount, PRICE_DISCOUNT_TYPE);
								}
							}
						}
					}
					loadOrder();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void clearDiscount(){
		mTrans.cancelDiscount(mTransactionId);
		mTrans.prepareDiscount(mTransactionId);
	}
	
	private boolean updateDiscount(double discount, int discountType) {
		discount = calculateDiscount(mOrder.getTotalRetailPrice(), discount, discountType);
		if(discount >= 0){
			double totalPriceAfterDiscount = mOrder.getTotalRetailPrice() - discount;
			mTrans.discountEatchProduct(mTransactionId, 
					mOrder.getOrderDetailId(), mOrder.getVatType(),
					mProduct.getVatRate(mOrder.getProductId()), 
					totalPriceAfterDiscount, discount, discountType);
			
			MPOSOrderTransaction.MPOSOrderDetail order = mOrderLst.get(mPosition);
			order.setPriceDiscount(discount);
			order.setTotalSalePrice(totalPriceAfterDiscount);
			order.setDiscountType(discountType);
			
			mOrderLst.set(mPosition, order);
			mDisAdapter.notifyDataSetChanged();
			mIsEdited = true;	
			return true;
		}
		return false;
	}
	
	/**
	 * @param totalRetailPrice
	 * @param discount
	 * @param discountType
	 * @return -1 if not success
	 */
	private double calculateDiscount(double totalRetailPrice, double discount, int discountType){
		if(discount < 0)
			return 0;
		
		double totalDiscount = discount;
		if(discountType == PRICE_DISCOUNT_TYPE){
			if(totalRetailPrice < discount)
				totalDiscount = -1;
		}else if(discountType == PERCENT_DISCOUNT_TYPE){
			if(discount > 100)
				totalDiscount = -1;
			else
				totalDiscount = totalRetailPrice * discount / 100;
		}
		BigDecimal big = new BigDecimal(totalDiscount);
		big = big.setScale(0, BigDecimal.ROUND_FLOOR);
		return big.doubleValue();
	}
	
	private void cancel(){
		if (mIsEdited) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.discount)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(R.string.confirm_cancel)
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mTrans.cancelDiscount(mTransactionId);
									finish();
								}
							}).show();
		} else {
			finish();
		}	
	}
	
	private void summary() {
		Fragment f = getFragmentManager().findFragmentById(R.id.discountContainer);
		if(f != null){
			if(f instanceof PlaceholderFragment){
				MPOSOrderTransaction.MPOSOrderDetail summOrder = 
						mTrans.getSummaryOrderForDiscount(mTransactionId);
				double totalVatExcluded = summOrder.getVatExclude();
				if(totalVatExcluded > 0){
					((PlaceholderFragment) f).mLayoutVat.setVisibility(View.VISIBLE);
				}else{
					((PlaceholderFragment) f).mLayoutVat.setVisibility(View.GONE);
				}
				mTotalPrice = summOrder.getTotalSalePrice() + summOrder.getVatExclude();
				((PlaceholderFragment) f).mTxtTotalVatExc.setText(mFormat.currencyFormat(totalVatExcluded));
				((PlaceholderFragment) f).mTxtSubTotal.setText(mFormat.currencyFormat(summOrder.getTotalRetailPrice()));
				((PlaceholderFragment) f).mTxtTotalDiscount.setText(mFormat.currencyFormat(summOrder.getPriceDiscount()));
				((PlaceholderFragment) f).mTxtTotalPrice.setText(mFormat.currencyFormat(mTotalPrice));
			}
		}
	}
	
	private void loadOrder() {
		mOrderLst = mTrans.listAllOrderForDiscount(mTransactionId);
		mDisAdapter.notifyDataSetChanged();
	}
	
	/**
	 * @author j1tth4
	 * Discount list adapter
	 */
	private class DiscountAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return mOrderLst != null ? mOrderLst.size() : 0;
		}

		@Override
		public MPOSOrderTransaction.MPOSOrderDetail getItem(int position) {
			return mOrderLst.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			summary();
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = convertView;
			rowView = inflater.inflate(R.layout.discount_template, null);
			TextView tvNo = (TextView) rowView.findViewById(R.id.tvNo);
			TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
			TextView tvQty = (TextView) rowView.findViewById(R.id.tvQty);
			TextView tvUnitPrice = (TextView) rowView.findViewById(R.id.tvPrice);
			TextView tvTotalPrice = (TextView) rowView.findViewById(R.id.tvTotalPrice);
			TextView tvDiscount = (TextView) rowView.findViewById(R.id.tvDiscount);
			final TextView tvSalePrice = (TextView) rowView.findViewById(R.id.tvSalePrice);

			final MPOSOrderTransaction.MPOSOrderDetail order =
					mOrderLst.get(position);
			tvNo.setText(Integer.toString(position + 1) + ".");
			tvName.setText(order.getProductName());
			tvQty.setText(mFormat.qtyFormat(order.getQty()));
			tvUnitPrice.setText(mFormat.currencyFormat(order.getPricePerUnit()));
			tvTotalPrice.setText(mFormat.currencyFormat(order.getTotalRetailPrice()));
			tvDiscount.setText(mFormat.currencyFormat(order.getPriceDiscount()));
			tvSalePrice.setText(mFormat.currencyFormat(order.getTotalSalePrice()));
			
			return rowView;
		}
	}
}
