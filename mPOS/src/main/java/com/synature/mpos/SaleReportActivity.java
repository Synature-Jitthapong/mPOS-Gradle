package com.synature.mpos;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.synature.exceptionhandler.ExceptionHandler;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MPOSDatabase;
import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.mpos.database.PaymentDetail;
import com.synature.mpos.database.Products;
import com.synature.mpos.database.Reporting;
import com.synature.mpos.database.Shop;
import com.synature.mpos.database.Transaction;
import com.synature.mpos.database.Reporting.SimpleProductData;
import com.synature.pos.Payment;
import com.synature.pos.Report;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SaleReportActivity extends Activity implements OnClickListener{

	public static final int REPORT_BY_BILL = 0;
	public static final int REPORT_BY_PRODUCT = 1;
	public static final int REPORT_ENDDAY = 2;
	
	private Shop mShop;
	
	private Formater mFormat;

	private Reporting mReporting;
	
	private int mStaffId;
	
	private Calendar mCalendar;
	private String mDateFrom;
	private String mDateTo;
	
	private TextView mTvTo;
	private Button mBtnDateFrom;
	private Button mBtnDateTo;
	private Spinner mSpReportType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Register ExceptinHandler for catch error when application crash.
		 */
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, 
				Utils.LOG_DIR, Utils.LOG_FILE_NAME));
		
		setContentView(R.layout.activity_sale_report);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mShop = new Shop(this);
		mFormat = new Formater(SaleReportActivity.this);
		Calendar c = Calendar.getInstance();
		mCalendar = new GregorianCalendar(c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		mDateFrom = String.valueOf(mCalendar.getTimeInMillis());
		mDateTo = String.valueOf(mCalendar.getTimeInMillis());

		mReporting = new Reporting(SaleReportActivity.this, mDateFrom, mDateTo);
		
		mStaffId = getIntent().getIntExtra("staffId", 0);
		
		if(savedInstanceState == null){
			getFragmentManager().beginTransaction()
				.add(R.id.reportContent, BillReportFragment.getInstance()).commit();
		}
	}
	
	private void switchReportType(int type){
		switch(type){
		case REPORT_BY_BILL:
			getFragmentManager().beginTransaction()
			.replace(R.id.reportContent, BillReportFragment.getInstance()).commit();
			if(mBtnDateFrom != null){
				mBtnDateFrom.setVisibility(View.VISIBLE);
				mTvTo.setVisibility(View.VISIBLE);
			}
			setTitle(R.string.sale_report_by_bill);
			break;
		case REPORT_BY_PRODUCT:
			getFragmentManager().beginTransaction()
			.replace(R.id.reportContent, ProductReportFragment.getInstance()).commit();
			if(mBtnDateFrom != null){
				mBtnDateFrom.setVisibility(View.VISIBLE);
				mTvTo.setVisibility(View.VISIBLE);
			}
			setTitle(R.string.sale_report_by_product);
			break;
		case REPORT_ENDDAY:
			getFragmentManager().beginTransaction()
			.replace(R.id.reportContent, EnddayReportFragment.getInstance()).commit();
			if(mBtnDateFrom != null){
				mBtnDateFrom.setVisibility(View.GONE);
				mTvTo.setVisibility(View.GONE);
			}
			setTitle(R.string.endday_report);
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sale_report, menu);
		MenuItem itemReportType = (MenuItem) menu.findItem(R.id.itemReportType);
		MenuItem itemCondition = (MenuItem) menu.findItem(R.id.itemDateCondition);
		mSpReportType = (Spinner) itemReportType.getActionView().findViewById(R.id.spinner1);
		mBtnDateFrom = (Button) itemCondition.getActionView().findViewById(R.id.btnDateFrom);
		mBtnDateTo = (Button) itemCondition.getActionView().findViewById(R.id.btnDateTo);
		mTvTo = (TextView) itemCondition.getActionView().findViewById(R.id.tvTo);
		mBtnDateFrom.setText(mFormat.dateFormat(mCalendar.getTime()));
		mBtnDateTo.setText(mFormat.dateFormat(mCalendar.getTime()));
		mBtnDateFrom.setOnClickListener(this);
		mBtnDateTo.setOnClickListener(this);
		mSpReportType.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				switchReportType(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		String[] reportTypes = {
				getString(R.string.sale_report_by_bill),
				getString(R.string.sale_report_by_product),
				getString(R.string.endday_report)
		};
		mSpReportType.setAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_dropdown_item, reportTypes));
		mSpReportType.setSelection(0);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private static LinearLayout createRowSummary(Context context, TextView[] tvs){
		LinearLayout row = new LinearLayout(context);
		for(TextView tvSummary : tvs){
			row.addView(tvSummary);
		}
		return row;
	}
	
	private static TextView createTextViewItem(Context context, 
			String content, LinearLayout.LayoutParams params){
		TextView tvItem = new TextView(context);
		tvItem.setText(content);
		tvItem.setLayoutParams(params);
		tvItem.setGravity(Gravity.RIGHT);
		tvItem.setTextAppearance(context, R.style.BodyText);
		return tvItem;
	}
	
	private static TextView createTextViewHeader(Context context, 
			String content, LinearLayout.LayoutParams params, int gravity){
		TextView tvHeader = new TextView(context);
		tvHeader.setText(content);
		tvHeader.setLayoutParams(params);
		tvHeader.setGravity(gravity == 0 ? Gravity.CENTER : gravity);
		tvHeader.setTextAppearance(context, R.style.HeaderText);
		tvHeader.setPadding(4, 4, 4, 4);
		return tvHeader;
	}
	
	private static TextView createTextViewSummary(Context context, 
			String content, LinearLayout.LayoutParams params){
		TextView tvSummary = new TextView(context);
		tvSummary.setText(content);
		tvSummary.setLayoutParams(params);
		tvSummary.setGravity(Gravity.RIGHT);
		tvSummary.setTextAppearance(context, R.style.HeaderText);
		tvSummary.setPadding(4, 4, 4, 4);
		return tvSummary;
	}

	/*
	 * Payment detail dialog
	 */
	public static class PaymentDetailFragment extends DialogFragment{
		
		private SaleReportActivity mHost;
		private PaymentDetail mPayment;
		private List<Payment.PaymentDetail> mPaymentLst;
		private PaymentDetailAdapter mPaymentAdapter;
		
		private int mTransactionId;
		
		private LayoutInflater mInflater;
		
		public static PaymentDetailFragment newInstance(int transactionId){
			PaymentDetailFragment f = new PaymentDetailFragment();
			Bundle b = new Bundle();
			b.putInt("transactionId", transactionId);
			f.setArguments(b);
			return f;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			mHost = (SaleReportActivity) getActivity();
			mTransactionId = getArguments().getInt("transactionId");
			
			mPayment = new PaymentDetail(getActivity());
			mPaymentLst = mPayment.listPaymentGroupByType(mTransactionId);
			mPaymentAdapter = new PaymentDetailAdapter();
			
			mInflater = (LayoutInflater) 
					getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			super.onCreate(savedInstanceState);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			View v = mInflater.inflate(R.layout.listview, null);
			final ListView lv = (ListView) v;
			lv.setAdapter(mPaymentAdapter);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.payment);
			builder.setView(v);
			builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDialog().dismiss();
				}
			});

			return builder.create();
		}

		private class PaymentDetailAdapter extends BaseAdapter{

			@Override
			public int getCount() {
				return mPaymentLst != null ? mPaymentLst.size() : 0;
			}

			@Override
			public Payment.PaymentDetail getItem(int position) {
				return mPaymentLst.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = mInflater.inflate(R.layout.template_flex_left_right, null);
				TextView tvLeft = (TextView) convertView.findViewById(R.id.textView1);
				TextView tvRight = (TextView) convertView.findViewById(R.id.textView2);
				
				Payment.PaymentDetail payment = mPaymentLst.get(position);
				
				tvLeft.setText(payment.getPayTypeName());
				tvRight.setText(mHost.mFormat.currencyFormat(payment.getPayAmount()));
				
				return convertView;
			}
			
		}
	}
	
	@Override
	public void onClick(View v) {
		DialogFragment dialogFragment;
		
		switch(v.getId()){
		case R.id.btnDateFrom:
			dialogFragment = new DatePickerFragment(new DatePickerFragment.OnSetDateListener() {
				
				@Override
				public void onSetDate(long date) {
					mCalendar.setTimeInMillis(date);
					mDateFrom = String.valueOf(mCalendar.getTimeInMillis());
					
					mBtnDateFrom.setText(mFormat.dateFormat(mCalendar.getTime()));
					mReporting.setDateFrom(mDateFrom);
				}
			});
			dialogFragment.show(getFragmentManager(), "Condition");
			break;
		case R.id.btnDateTo:
			dialogFragment = new DatePickerFragment(new DatePickerFragment.OnSetDateListener() {
				
				@Override
				public void onSetDate(long date) {
					mCalendar.setTimeInMillis(date);
					mDateTo = String.valueOf(mCalendar.getTimeInMillis());
					
					mBtnDateTo.setText(mFormat.dateFormat(mCalendar.getTime()));
					mReporting.setDateTo(mDateTo);
				}
			});
			dialogFragment.show(getFragmentManager(), "Condition");
			break;
		}
	}
	
	/**
	 * @author j1tth4
	 * Endday Report Fragment
	 */
	public static class EnddayReportFragment extends Fragment{

		private SaleReportActivity mHost;
		
		private static EnddayReportFragment sInstance;
		
		private Transaction mTrans;
		private PaymentDetail mPayment;
	
		private LinearLayout mEnddayReportFooterContainer;
		private ListView mLvEnddayReport;
		
		public static EnddayReportFragment getInstance(){
			if(sInstance == null){
				sInstance = new EnddayReportFragment();
			}
			return sInstance;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mHost = (SaleReportActivity) getActivity();
			setHasOptionsMenu(true);
			mTrans = new Transaction(getActivity());
			mPayment = new PaymentDetail(getActivity());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_endday_report, container, false);
			mEnddayReportFooterContainer = (LinearLayout) rootView.findViewById(R.id.enddayReportFooterContainer);
			mLvEnddayReport = (ListView) rootView.findViewById(R.id.lvEnddayReport);
			return rootView;
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.fragment_sale_report, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			SaleReportActivity activity = (SaleReportActivity) getActivity();
			switch(item.getItemId()){
			case R.id.itemCreateReport:
				createReport();
				return true;
			case R.id.itemPrint:
				new PrintReport(getActivity(), activity.mDateTo, activity.mDateTo, 
						activity.mStaffId, PrintReport.WhatPrint.SUMMARY_SALE).execute();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		
		private void createReport(){
			LayoutInflater inflater = (LayoutInflater)
					getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Shop shop = new Shop(getActivity());
			MPOSOrderTransaction trans = mTrans.getTransaction(mHost.mDateTo);
			MPOSOrderTransaction.MPOSOrderDetail sumOrder 
				= mTrans.getSummaryOrderInDay(mHost.mDateTo, mHost.mDateTo);
			
			mEnddayReportFooterContainer.removeAllViews();
			// add footer
			View totalView = inflater.inflate(R.layout.left_mid_right_template, null);
			((TextView) totalView.findViewById(R.id.tvMid)).setText(getString(R.string.total));
			((TextView) totalView.findViewById(R.id.tvMid)).append(" " +
					mHost.mFormat.qtyFormat(sumOrder.getQty()));
			((TextView) totalView.findViewById(R.id.tvRight)).setText(
					mHost.mFormat.currencyFormat(sumOrder.getTotalRetailPrice()));
			mEnddayReportFooterContainer.addView(totalView);
			
			if(sumOrder.getPriceDiscount() > 0){
				View discountView = inflater.inflate(R.layout.left_mid_right_template, null);
				((TextView) discountView.findViewById(R.id.tvMid)).setText(getString(R.string.discount));
				((TextView) discountView.findViewById(R.id.tvRight)).setText(
						mHost.mFormat.currencyFormat(sumOrder.getPriceDiscount()));
				mEnddayReportFooterContainer.addView(discountView);
			}
			
			View subTotalView2 = inflater.inflate(R.layout.left_mid_right_template, null);
			((TextView) subTotalView2.findViewById(R.id.tvMid)).setText(getString(R.string.sub_total));
			((TextView) subTotalView2.findViewById(R.id.tvRight)).setText(
					mHost.mFormat.currencyFormat(sumOrder.getTotalSalePrice()));
			mEnddayReportFooterContainer.addView(subTotalView2);
			
			if(sumOrder.getVatExclude() > 0){
				View vatExcludeView = inflater.inflate(R.layout.left_mid_right_template, null);
				((TextView) vatExcludeView.findViewById(R.id.tvMid)).setText(getString(R.string.vat_exclude));
				((TextView) vatExcludeView.findViewById(R.id.tvRight)).setText(
						mHost.mFormat.currencyFormat(sumOrder.getVatExclude()));
				mEnddayReportFooterContainer.addView(vatExcludeView);
			}
			
			View totalSaleView = inflater.inflate(R.layout.left_mid_right_template, null);
			((TextView) totalSaleView.findViewById(R.id.tvMid)).setText(getString(R.string.total_sale));
			((TextView) totalSaleView.findViewById(R.id.tvRight)).setText(
					mHost.mFormat.currencyFormat(
							sumOrder.getTotalSalePrice() + sumOrder.getVatExclude()));
			mEnddayReportFooterContainer.addView(totalSaleView);
			
			if(shop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
				View vatView = inflater.inflate(R.layout.left_mid_right_template, null);
				((TextView) vatView.findViewById(R.id.tvMid)).setText(getString(R.string.before_vat));
				((TextView) vatView.findViewById(R.id.tvRight)).setText(
						mHost.mFormat.currencyFormat(
								trans.getTransactionVatable() - trans.getTransactionVat()));
				mEnddayReportFooterContainer.addView(vatView);
				vatView = inflater.inflate(R.layout.left_mid_right_template, null);
				((TextView) vatView.findViewById(R.id.tvMid)).setText(getString(R.string.total_vat));
				((TextView) vatView.findViewById(R.id.tvRight)).setText(
						mHost.mFormat.currencyFormat(trans.getTransactionVat()));
				mEnddayReportFooterContainer.addView(vatView);
			}
			
			List<Payment.PaymentDetail> summaryPaymentLst = 
					mPayment.listSummaryPayment(
							mTrans.getSeperateTransactionId(mHost.mDateTo));
			if(summaryPaymentLst != null){
				View paymentView = inflater.inflate(R.layout.left_mid_right_template, null);
				((TextView) paymentView.findViewById(R.id.tvMid)).setText(getString(R.string.payment_detail));
				((TextView) paymentView.findViewById(R.id.tvMid)).setPaintFlags(
						((TextView) paymentView.findViewById(R.id.tvMid)).getPaintFlags() |Paint.UNDERLINE_TEXT_FLAG);
				((TextView) paymentView.findViewById(R.id.tvRight)).setText(null);
				mEnddayReportFooterContainer.addView(paymentView);
				for(Payment.PaymentDetail payment : summaryPaymentLst){
					paymentView = inflater.inflate(R.layout.left_mid_right_template, null);
					((TextView) paymentView.findViewById(R.id.tvMid)).setText(payment.getPayTypeName());
					((TextView) paymentView.findViewById(R.id.tvRight)).setText(
							mHost.mFormat.currencyFormat(payment.getPayAmount()));
					mEnddayReportFooterContainer.addView(paymentView);
				}
			}
			
			View totalReceiptView = inflater.inflate(R.layout.left_mid_right_template, null);
			((TextView) totalReceiptView.findViewById(R.id.tvMid)).setText(getString(R.string.total_receipt_in_day));
			((TextView) totalReceiptView.findViewById(R.id.tvRight)).setText(
					String.valueOf(mTrans.getTotalReceipt(mHost.mDateTo)));
			mEnddayReportFooterContainer.addView(totalReceiptView);
			
			MPOSOrderTransaction.MPOSOrderDetail sumVoidOrder = 
					mTrans.getSummaryVoidOrderInDay(mHost.mDateTo);
			View totalVoidView = inflater.inflate(R.layout.left_mid_right_template, null);
			((TextView) totalVoidView.findViewById(R.id.tvMid)).setText(getString(R.string.void_bill));
			((TextView) totalVoidView.findViewById(R.id.tvMid)).setPaintFlags(
					((TextView) totalVoidView.findViewById(R.id.tvMid)).getPaintFlags() |Paint.UNDERLINE_TEXT_FLAG);
			((TextView) totalVoidView.findViewById(R.id.tvRight)).setText(null);
			mEnddayReportFooterContainer.addView(totalVoidView);
			totalVoidView = inflater.inflate(R.layout.left_mid_right_template, null);
			((TextView) totalVoidView.findViewById(R.id.tvMid)).setText(getString(R.string.void_bill_after_paid));
			((TextView) totalVoidView.findViewById(R.id.tvMid)).append(" " +
					mHost.mFormat.qtyFormat(sumVoidOrder.getQty()));
			((TextView) totalVoidView.findViewById(R.id.tvRight)).setText(
					mHost.mFormat.currencyFormat(sumVoidOrder.getTotalSalePrice()));
			mEnddayReportFooterContainer.addView(totalVoidView);
			
			loadReportDetail();
		}
		
		private void loadReportDetail(){
			Reporting reporting = new Reporting(getActivity(), 
					mHost.mDateTo, 
					mHost.mDateTo);
			List<SimpleProductData> simpleLst = reporting.listSummaryProductGroupInDay();
			mLvEnddayReport.setAdapter(new EnddayReportAdapter(simpleLst));
		}
		
		private class EnddayReportAdapter extends BaseAdapter{

			private LayoutInflater mInflater;
			private List<SimpleProductData> mSimpleLst;
			
			public EnddayReportAdapter(List<SimpleProductData> simpleLst){
				mInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mSimpleLst = simpleLst;
			}
			
			@Override
			public int getCount() {
				return mSimpleLst != null ? mSimpleLst.size() : 0;
			}

			@Override
			public SimpleProductData getItem(int position) {
				return mSimpleLst.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder;
				if(convertView == null){
					convertView = mInflater.inflate(R.layout.endday_report_template, null);
					holder = new ViewHolder();
					holder.tvGroupDept = (TextView) convertView.findViewById(R.id.tvGroupDept);
					holder.tvGroupTotalQty = (TextView) convertView.findViewById(R.id.tvGroupTotalQty);
					holder.tvGroupTotalPrice= (TextView) convertView.findViewById(R.id.tvGroupTotalPrice);
					holder.itemContainer = (LinearLayout) convertView.findViewById(R.id.itemContainer);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				
				SimpleProductData simple = mSimpleLst.get(position);
				holder.tvGroupDept.setText(simple.getDeptName());
				holder.tvGroupTotalQty.setText(
						mHost.mFormat.qtyFormat(simple.getDeptTotalQty()));
				holder.tvGroupTotalPrice.setText(
						mHost.mFormat.currencyFormat(simple.getDeptTotalPrice()));
				if(simple.getItemLst() != null){
					holder.itemContainer.removeAllViews();
					for(SimpleProductData.Item item : simple.getItemLst()){
						View bill = mInflater.inflate(R.layout.left_mid_right_template, null);
						((TextView) bill.findViewById(R.id.tvLeft)).setText(item.getItemName());
						((TextView) bill.findViewById(R.id.tvMid)).setText(
								mHost.mFormat.qtyFormat(item.getTotalQty()));
						((TextView) bill.findViewById(R.id.tvRight)).setText(
								mHost.mFormat.currencyFormat(item.getTotalPrice()));
						holder.itemContainer.addView(bill);
					}
				}
				return convertView;
			}
			
			class ViewHolder{
				TextView tvGroupDept;
				TextView tvGroupTotalQty;
				TextView tvGroupTotalPrice;
				LinearLayout itemContainer;
			}
		}
	}
	
	public static class BillReportFragment extends Fragment{

		private SaleReportActivity mHost;
		
		private static BillReportFragment sInstance;

		private Report mBillReport;
		private BillReportAdapter mBillReportAdapter;
		
		private ListView mLvReport;
		private LinearLayout mBillHeader;
		private LinearLayout mBillSumContent;
		private ProgressDialog mProgress;
		
		public static BillReportFragment getInstance(){
			if(sInstance == null){
				sInstance = new BillReportFragment();
			}
			return sInstance;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
			case R.id.itemCreateReport:
				new LoadBillReportTask().execute();
				return true;
			case R.id.itemPrint:
				new PrintReport(getActivity(), mHost.mDateFrom, mHost.mDateTo,
						PrintReport.WhatPrint.BILL_REPORT).execute();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.fragment_sale_report, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mHost = (SaleReportActivity) getActivity();
			setHasOptionsMenu(true);
			mBillReport = new Report();
			mBillReportAdapter = new BillReportAdapter();
			mProgress = new ProgressDialog(getActivity());
			mProgress.setMessage(getString(R.string.loading));
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bill_report, container, false);

			mBillHeader = (LinearLayout) rootView.findViewById(R.id.billHeader);
			mBillSumContent = (LinearLayout) rootView.findViewById(R.id.billSummaryContent);
			mLvReport = (ListView) rootView.findViewById(R.id.lvReport);
			mLvReport.setAdapter(mBillReportAdapter);
			
			createHeader();
			return rootView;
		}
		
		private void createHeader(){
			mBillHeader.removeAllViews();
			TextView[] tvHeaders = {
					createTextViewHeader(getActivity(), "", Utils.getLinHorParams(0.2f), 0),
					createTextViewHeader(getActivity(), getActivity().getString(R.string.receipt_no), 
							Utils.getLinHorParams(1.0f), Gravity.LEFT),
					createTextViewHeader(getActivity(), getActivity().getString(R.string.total), 
							Utils.getLinHorParams(0.7f), Gravity.RIGHT),
					createTextViewHeader(getActivity(), getActivity().getString(R.string.discount), 
							Utils.getLinHorParams(0.7f), Gravity.RIGHT),
					createTextViewHeader(getActivity(), getActivity().getString(R.string.sub_total), 
							Utils.getLinHorParams(0.7f), Gravity.RIGHT),
					createTextViewHeader(getActivity(), getActivity().getString(R.string.vatable), 
							Utils.getLinHorParams(0.7f), Gravity.RIGHT)
			};
			for(TextView tv : tvHeaders){
				mBillHeader.addView(tv);
			}
			if(mHost.mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
				tvHeaders = new TextView[]{
						createTextViewHeader(getActivity(), getString(R.string.before_vat), 
								Utils.getLinHorParams(0.7f), Gravity.RIGHT),
						createTextViewHeader(getActivity(), getString(R.string.vat) + " "
								+ NumberFormat.getInstance().format(mHost.mShop.getCompanyVatRate())
								+ " " + getString(R.string.percent), Utils.getLinHorParams(0.7f), Gravity.RIGHT)
				};
				for(TextView tv : tvHeaders){
					mBillHeader.addView(tv);
				}
			}
			tvHeaders = new TextView[]{
					createTextViewHeader(getActivity(), getString(R.string.total_payment), 
							Utils.getLinHorParams(0.7f), Gravity.RIGHT)
			};
			for(TextView tv : tvHeaders){
				mBillHeader.addView(tv);
			}
		}
		
		public class BillReportAdapter extends BaseAdapter{
			
			private LayoutInflater mInflater;
			
			public BillReportAdapter(){
				mInflater = (LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			
			@Override
			public int getCount() {
				return mBillReport != null ? mBillReport.getReportDetail().size() : 0;
			}

			@Override
			public Report.ReportDetail getItem(int position) {
				return mBillReport.getReportDetail().get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();

				summaryBill();
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = mInflater.inflate(R.layout.bill_report_template, null);
				}
				final Report.ReportDetail report = mBillReport.getReportDetail().get(position);
				double vatable = report.getVatable();
				double totalVat = report.getTotalVat();
				double beforVat = vatable - totalVat;
				double totalPrice = report.getTotalPrice();
				double totalDiscount = report.getDiscount();
				double subTotal = report.getSubTotal();
				double totalPay = report.getTotalPayment();

				LinearLayout container = (LinearLayout) convertView;
				if(container.getChildCount() > 0)
					container.removeAllViews();
				TextView tvBill = createTextViewItem(getActivity(), report.getReceiptNo(), 
						Utils.getLinHorParams(1f));
				tvBill.setGravity(Gravity.LEFT);		
				if(report.getTransStatus() == Transaction.TRANS_STATUS_VOID){
					tvBill.setTextColor(Color.RED);
					tvBill.setPaintFlags(tvBill.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				}else{
					tvBill.setTextColor(Color.BLACK);
					tvBill.setPaintFlags(tvBill.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
				}
				TextView tvs[] = new TextView[]{
						tvBill,
						createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(totalPrice),
								Utils.getLinHorParams(0.7f)),
						createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(totalDiscount),
								Utils.getLinHorParams(0.7f)),
						createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(subTotal),  
								Utils.getLinHorParams(0.7f)),
						createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(vatable),  
								Utils.getLinHorParams(0.7f))
				};
				ImageView imgSendStatus = new ImageView(getActivity());
				imgSendStatus.setLayoutParams(Utils.getLinHorParams(0.2f));
				container.addView(imgSendStatus);
				for(TextView tv : tvs){
					container.addView(tv);
				}
				if(mHost.mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
					tvs = new TextView[]{
						createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(beforVat),  
								Utils.getLinHorParams(0.7f)),
						createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(totalVat),  
								Utils.getLinHorParams(0.7f)),
					};
					for(TextView tv : tvs){
						container.addView(tv);
					}
				}
				TextView tvTotalPay = new TextView(getActivity());
				tvTotalPay = createTextViewItem(getActivity(), mHost.mFormat.currencyFormat(totalPay),  
						Utils.getLinHorParams(0.7f));
				tvTotalPay.setFocusable(false);
				tvTotalPay.setFocusableInTouchMode(false);
				tvTotalPay.setTextAppearance(getActivity(), android.R.style.TextAppearance_Holo_Large);
				tvTotalPay.setTextColor(Color.BLUE);
				tvTotalPay.setPaintFlags(tvTotalPay.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
				tvTotalPay.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						PaymentDetailFragment f = 
								PaymentDetailFragment.newInstance(report.getTransactionId());
						f.show(getFragmentManager(), "PaymentDialogFragment");
					}
					
				});
				container.addView(tvTotalPay);
				
//				List<Payment.PaymentDetail> payTypeLst = 
//						mPayment.listPaymentGroupByType(report.getTransactionId(), report.getComputerId());
	//	
//				int idx = 7; // position to add
//				for(Payment.PaymentDetail payType : payTypeLst){
//					TextView tvPayTypeHead = (TextView) mInflater.inflate(R.layout.tv_column_header, null);
//					mBillHeader.addView(tvPayTypeHead, idx);
//					TextView tvPaytype = (TextView) mInflater.inflate(R.layout.tv_column_detail, null);
//					((LinearLayout) convertView).addView(tvPaytype, idx);
//					tvPayTypeHead.setText(payType.getPayTypeName());
//					tvPaytype.setText(Utils.getGlobalProperty().currencyFormat(payType.getPayAmount()));
//					idx++;
//				}
				
//				holder.tvTotalPayment.setTextColor(Color.BLUE);
//				holder.tvTotalPayment.setPaintFlags(holder.tvTotalPayment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//				holder.tvTotalPayment.setOnClickListener(new OnClickListener(){
//
//					@Override
//					public void onClick(View v) {
//						PaymentDetailFragment f = 
//								PaymentDetailFragment.newInstance(report.getTransactionId());
//						f.show(getFragmentManager(), "PaymentDialogFragment");
//					}
//					
//				});
//				
				if(report.getSendStatus() == MPOSDatabase.ALREADY_SEND){
					imgSendStatus.setImageResource(R.drawable.ic_action_accept);
				}else{
					imgSendStatus.setImageResource(R.drawable.ic_action_warning);
				}
				return convertView;
			}
		}
		
		private void summaryBill(){
			Report.ReportDetail summary = 
					mHost.mReporting.getBillSummary();
			mBillSumContent.removeAllViews();
			TextView[] tvSummary = {
					createTextViewSummary(getActivity(), getString(R.string.summary),  
							Utils.getLinHorParams(1.2f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summary.getTotalPrice()),  
							Utils.getLinHorParams(0.7f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summary.getDiscount()),  
							Utils.getLinHorParams(0.7f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summary.getSubTotal()),  
							Utils.getLinHorParams(0.7f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summary.getVatable()),  
							Utils.getLinHorParams(0.7f))
			};
			for(TextView tv : tvSummary){
				mBillSumContent.addView(tv);	
			}
			if(mHost.mShop.getCompanyVatType() == Products.VAT_TYPE_INCLUDED){
				double beforVat = summary.getVatable() - summary.getTotalVat();
				tvSummary = new TextView[]{
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(beforVat), 
							Utils.getLinHorParams(0.7f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summary.getTotalVat()),  
							Utils.getLinHorParams(0.7f))
				};
				for(TextView tv : tvSummary){
					mBillSumContent.addView(tv);
				}
			}
			tvSummary = new TextView[]{
				createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summary.getTotalPayment()),  
						Utils.getLinHorParams(0.7f))
			};
			for(TextView tv : tvSummary){
				mBillSumContent.addView(tv);
			}
		}
		
		public class LoadBillReportTask extends AsyncTask<Void, Void, Void>{

			@Override
			protected void onPreExecute() {
				mProgress.show();
			}

			@Override
			protected void onPostExecute(Void result) {
				if(mProgress.isShowing())
					mProgress.dismiss();
				mBillReportAdapter.notifyDataSetChanged();
			}

			@Override
			protected Void doInBackground(Void... params) {
				mBillReport = mHost.mReporting.getSaleReportByBill();
				return null;
			}
			
		}
	}
	
	public static class ProductReportFragment extends Fragment{

		private SaleReportActivity mHost;
		
		private static ProductReportFragment sInstance;

		private Report mReportProduct;
		private ProductReportAdapter mProductReportAdapter;
		
		private LinearLayout mProductSumContent;
		private ExpandableListView mLvReportProduct;
		private ProgressDialog mProgress;
		
		public static ProductReportFragment getInstance(){
			if(sInstance == null){
				sInstance = new ProductReportFragment();
			}
			return sInstance;
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.fragment_sale_report, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
			case R.id.itemCreateReport:
				new LoadProductReportTask().execute();
				return true;
			case R.id.itemPrint:
				new PrintReport(getActivity(), 
						mHost.mDateFrom, mHost.mDateTo,
						PrintReport.WhatPrint.PRODUCT_REPORT).execute();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mHost = (SaleReportActivity) getActivity();
			setHasOptionsMenu(true);
			mReportProduct = new Report();
			mProductReportAdapter = new ProductReportAdapter();
			mProgress = new ProgressDialog(getActivity());
			mProgress.setMessage(getString(R.string.loading));
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_product_report, container, false);

			mProductSumContent = (LinearLayout) rootView.findViewById(R.id.productSummaryContent);
			mLvReportProduct = (ExpandableListView) rootView.findViewById(R.id.lvReportProduct);

			mLvReportProduct.setAdapter(mProductReportAdapter);
			mLvReportProduct.setGroupIndicator(null);
			return rootView;
		}
		
		public class ProductReportAdapter extends BaseExpandableListAdapter{
			
			private LayoutInflater mInflater;
			
			public ProductReportAdapter(){
				mInflater = (LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			
			@Override
			public Report.ReportDetail getChild(int groupPosition, int childPosition) {
				try {
					return mReportProduct.getGroupOfProductLst().get(groupPosition).
							getReportDetail().get(childPosition);
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				
				summaryProduct();
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {	
				ProductReportViewHolder holder;
				if(convertView == null){
					convertView = mInflater.inflate(R.layout.product_report_template, parent, false);
					holder = new ProductReportViewHolder();
					holder.tvNo = (TextView) convertView.findViewById(R.id.tvNo);
					holder.tvProductCode = (TextView) convertView.findViewById(R.id.tvProCode);
					holder.tvProductName = (TextView) convertView.findViewById(R.id.tvProName);
					holder.tvProductPrice = (TextView) convertView.findViewById(R.id.tvProPrice);
					holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
					holder.tvQtyPercent = (TextView) convertView.findViewById(R.id.tvQtyPercent);
					holder.tvSubTotal = (TextView) convertView.findViewById(R.id.tvSubTotal);
					holder.tvSubTotalPercent = (TextView) convertView.findViewById(R.id.tvSubTotalPercent);
					holder.tvDiscount = (TextView) convertView.findViewById(R.id.tvDiscount);
					holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);
					holder.tvTotalPricePercent = (TextView) convertView.findViewById(R.id.tvTotalPricePercent);
					holder.tvVatType = (TextView) convertView.findViewById(R.id.tvVatType);
					convertView.setTag(holder);
				}else{
					holder = (ProductReportViewHolder) convertView.getTag();
				}

				Report.ReportDetail reportDetail = 
						mReportProduct.getGroupOfProductLst().get(groupPosition).getReportDetail().get(childPosition);
				setText(holder, childPosition, reportDetail);
				
				if(reportDetail.getProductName().equals(Reporting.SUMM_DEPT)){
					setSummary(holder, mReportProduct.getGroupOfProductLst().get(groupPosition).getProductDeptName());
				}else if(reportDetail.getProductName().equals(Reporting.SUMM_GROUP)){
					setSummary(holder, mReportProduct.getGroupOfProductLst().get(groupPosition).getProductGroupName());
				}
				return convertView;
			}
			
			private void setSummary(ProductReportViewHolder holder, String text){
				holder.tvNo.setVisibility(View.GONE);
				holder.tvProductName.setVisibility(View.GONE);
				holder.tvProductCode.setVisibility(View.GONE);
				holder.tvProductPrice.setText(getActivity().getString(R.string.summary) + 
						" " + text);
				holder.tvProductPrice.setLayoutParams(
						new LinearLayout.LayoutParams(0, 
								LayoutParams.WRAP_CONTENT, 2.8f));
			}
			
			private void setText(ProductReportViewHolder holder, int position, 
					Report.ReportDetail reportDetail){
				holder.tvNo.setVisibility(View.VISIBLE);
				holder.tvProductCode.setVisibility(View.VISIBLE);
				holder.tvProductPrice.setVisibility(View.VISIBLE);
				holder.tvProductName.setVisibility(View.VISIBLE);
				holder.tvNo.setLayoutParams(
						new LinearLayout.LayoutParams(0, 
								LayoutParams.WRAP_CONTENT, 0.2f));
				holder.tvProductCode.setLayoutParams(
						new LinearLayout.LayoutParams(0, 
								LayoutParams.WRAP_CONTENT, 0.8f));
				holder.tvProductName.setLayoutParams(
						new LinearLayout.LayoutParams(0, 
								LayoutParams.WRAP_CONTENT, 1f));
				holder.tvProductPrice.setLayoutParams(
						new LinearLayout.LayoutParams(0, 
								LayoutParams.WRAP_CONTENT, 0.8f));
				holder.tvNo.setText(String.valueOf(position + 1) + ".");
				holder.tvProductCode.setText(reportDetail.getProductCode());
				holder.tvProductName.setText(reportDetail.getProductName());
				holder.tvProductPrice.setText(mHost.mFormat.currencyFormat(
						reportDetail.getPricePerUnit()));
				holder.tvQty.setText(mHost.mFormat.qtyFormat(
						reportDetail.getQty()));
				holder.tvQtyPercent.setText(mHost.mFormat.currencyFormat(
						reportDetail.getQtyPercent()));
				holder.tvSubTotal.setText(mHost.mFormat.currencyFormat(
						reportDetail.getSubTotal()));
				holder.tvSubTotalPercent.setText(mHost.mFormat.currencyFormat(
						reportDetail.getSubTotalPercent()));
				holder.tvDiscount.setText(mHost.mFormat.currencyFormat(
						reportDetail.getDiscount()));
				holder.tvTotalPrice.setText(mHost.mFormat.currencyFormat(
						reportDetail.getTotalPrice()));
				holder.tvTotalPricePercent.setText(mHost.mFormat.currencyFormat(
						reportDetail.getTotalPricePercent()));
				holder.tvVatType.setText(reportDetail.getVat());
			}
			
			@Override
			public int getChildrenCount(int groupPosition) {
				int count = 0;
				if(mReportProduct != null)
				{
					if(mReportProduct.getGroupOfProductLst() != null){
						if(mReportProduct.getGroupOfProductLst().get(groupPosition).getReportDetail() != null){
							count = mReportProduct.getGroupOfProductLst().get(groupPosition).getReportDetail().size();
						}
					}
				}
				return count;
			}

			@Override
			public Report.GroupOfProduct getGroup(int groupPosition) {
				try {
					return mReportProduct.getGroupOfProductLst().get(groupPosition);
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public int getGroupCount() {
				int count = 0;
				if(mReportProduct != null){
					if(mReportProduct.getGroupOfProductLst() != null){
						count = mReportProduct.getGroupOfProductLst().size();
					}
				}
				return count;
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				
				try {
					if(!isExpanded)
						((ExpandableListView) parent).expandGroup(groupPosition);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ProductReportHeaderHolder groupHolder;
				if(convertView == null){
					groupHolder = new ProductReportHeaderHolder();
					groupHolder.tvHeader = new TextView(getActivity());
					groupHolder.tvHeader.setTextAppearance(getActivity(), R.style.HeaderText);
					groupHolder.tvHeader.setPadding(8, 4, 4, 4);
					groupHolder.tvHeader.setTextSize(20);
					convertView = groupHolder.tvHeader;
					convertView.setTag(groupHolder);
				}else{
					groupHolder = (ProductReportHeaderHolder) convertView.getTag();
				}
				groupHolder.tvHeader.setText(mReportProduct.getGroupOfProductLst().get(groupPosition).getProductGroupName() + ":" +
						mReportProduct.getGroupOfProductLst().get(groupPosition).getProductDeptName());
				return convertView;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return false;
			}
			
			class ProductReportHeaderHolder{
				TextView tvHeader;
			}
			
			class ProductReportViewHolder{
				TextView tvNo;
				TextView tvProductCode;
				TextView tvProductName;
				TextView tvProductPrice;
				TextView tvQty;
				TextView tvQtyPercent;
				TextView tvSubTotal;
				TextView tvSubTotalPercent;
				TextView tvDiscount;
				TextView tvTotalPrice;
				TextView tvTotalPricePercent;
				TextView tvVatType;
			}
		}
		
		private void summaryProduct(){
			Report.ReportDetail summProduct = 
					mHost.mReporting.getProductSummary();
			TextView[] tvGrandTotal = {
					createTextViewSummary(getActivity(), getString(R.string.grand_total), Utils.getLinHorParams(2.8f)),
					createTextViewSummary(getActivity(), mHost.mFormat.qtyFormat(summProduct.getQty()), 
							Utils.getLinHorParams(0.5f)),
					createTextViewSummary(getActivity(), mHost.mFormat.qtyFormat(summProduct.getQty() / summProduct.getQty() * 100), 
							Utils.getLinHorParams(0.5f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summProduct.getSubTotal()), 
							Utils.getLinHorParams(0.8f)),
					createTextViewSummary(getActivity(), mHost.mFormat.qtyFormat(summProduct.getSubTotal() / summProduct.getSubTotal() * 100), 
							Utils.getLinHorParams(0.5f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summProduct.getDiscount()), 
							Utils.getLinHorParams(0.8f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summProduct.getTotalPrice()), 
							Utils.getLinHorParams(0.8f)),
					createTextViewSummary(getActivity(), mHost.mFormat.qtyFormat(summProduct.getTotalPrice() / summProduct.getTotalPrice() * 100), 
							Utils.getLinHorParams(0.5f)),
					createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.2f))
			};
			mProductSumContent.removeAllViews();
			mProductSumContent.addView(createRowSummary(getActivity(), tvGrandTotal));
			
			Transaction trans = new Transaction(getActivity());
			MPOSOrderTransaction.MPOSOrderDetail summOrder 
				= trans.getSummaryOrderInDay(mHost.mDateFrom, 
						mHost.mDateTo);	
			
			// total sale
			TextView[] tvSubTotal = {
					createTextViewSummary(getActivity(), getString(R.string.sub_total), Utils.getLinHorParams(5.9f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summOrder.getTotalRetailPrice()),
							Utils.getLinHorParams(0.8f)),
					createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.5f)),
					createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.2f))
			};
			mProductSumContent.addView(createRowSummary(getActivity(), tvSubTotal));
			
			if(summOrder.getPriceDiscount() > 0){
				// total discount
				TextView[] tvTotalDiscount = {
						createTextViewSummary(getActivity(), getString(R.string.discount), Utils.getLinHorParams(5.9f)),
						createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summOrder.getPriceDiscount()),
								Utils.getLinHorParams(0.8f)),
						createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.5f)),
						createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.2f))
				};
				mProductSumContent.addView(createRowSummary(getActivity(), tvTotalDiscount));
			}
			
			if(summOrder.getVatExclude() > 0){
				Shop shop = new Shop(getActivity());
				// total vatExclude
				TextView[] tvTotalVatExclude = {
						createTextViewSummary(getActivity(), getString(R.string.vat_exclude) + " " +
								NumberFormat.getInstance().format(shop.getCompanyVatRate()) + "%", Utils.getLinHorParams(5.9f)),
						createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summOrder.getVatExclude()), 
								Utils.getLinHorParams(0.8f)),
						createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.5f)),
						createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.2f))
				};
				mProductSumContent.addView(createRowSummary(getActivity(), tvTotalVatExclude));
			}
			
			// total sale
			TextView[] tvTotalSale = {
					createTextViewSummary(getActivity(), getString(R.string.total_sale), Utils.getLinHorParams(5.9f)),
					createTextViewSummary(getActivity(), mHost.mFormat.currencyFormat(summOrder.getTotalSalePrice() + 
							summOrder.getVatExclude()), Utils.getLinHorParams(0.8f)),
					createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.5f)),
					createTextViewSummary(getActivity(), "", Utils.getLinHorParams(0.2f))
			};
			mProductSumContent.addView(createRowSummary(getActivity(), tvTotalSale));
		
		}
		
		public class LoadProductReportTask extends AsyncTask<Void, Void, Void>{

			@Override
			protected void onPreExecute() {
				mProgress.show();
			}

			@Override
			protected void onPostExecute(Void result) {
				if(mProgress.isShowing())
					mProgress.dismiss();
				mProductReportAdapter.notifyDataSetChanged();
			}

			@Override
			protected Void doInBackground(Void... params) {
				mReportProduct = mHost.mReporting.getProductDataReport();
				return null;
			}
			
		}
	}
}
