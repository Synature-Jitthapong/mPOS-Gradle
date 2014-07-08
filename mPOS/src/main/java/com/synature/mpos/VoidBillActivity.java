package com.synature.mpos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.synature.exceptionhandler.ExceptionHandler;
import com.synature.mpos.SaleService.LocalBinder;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MPOSOrderTransaction;
import com.synature.mpos.database.PrintReceiptLog;
import com.synature.mpos.database.Transaction;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class VoidBillActivity extends Activity {
	
	private SaleService mPartService;
	private boolean mBound = false;
	
	private Transaction mTrans;
	private Formater mFormat;
	
	private List<MPOSOrderTransaction> mTransLst;
	private List<MPOSOrderTransaction.MPOSOrderDetail> mOrderLst;
	private BillAdapter mBillAdapter;
	private BillDetailAdapter mBillDetailAdapter;
	
	private int mTransactionId;
	private int mComputerId;
	private int mShopId;
	private int mStaffId;
	
	private Calendar mCalendar;
	private String mDate;
	private String mReceiptNo;
	private String mReceiptDate;
	
	private ListView mLvBill;
	private ListView mLvBillDetail;
	private EditText txtReceiptNo;
	private EditText txtReceiptDate;
	private TextView tvSaleDate;
	private Button btnSearch;
	private MenuItem mItemConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Register ExceptinHandler for catch error when application crash.
		 */
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, 
				Utils.LOG_DIR, Utils.LOG_FILE_NAME));
		
		setContentView(R.layout.activity_void_bill);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        
		Calendar c = Calendar.getInstance();
		mCalendar = new GregorianCalendar(c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		mDate = String.valueOf(mCalendar.getTimeInMillis());
		
		txtReceiptNo = (EditText) findViewById(R.id.txtReceiptNo);
		txtReceiptDate = (EditText) findViewById(R.id.txtSaleDate);
		mLvBill = (ListView) findViewById(R.id.lvBill);
		mLvBillDetail = (ListView) findViewById(R.id.lvBillDetail);
	    tvSaleDate = (TextView) findViewById(R.id.tvSaleDate);
	    btnSearch = (Button) findViewById(R.id.btnSearch);

		mTrans = new Transaction(getApplicationContext());
		mFormat = new Formater(getApplicationContext());
		mTransLst = new ArrayList<MPOSOrderTransaction>();
		mOrderLst = new ArrayList<MPOSOrderTransaction.MPOSOrderDetail>();
		mBillAdapter = new BillAdapter();
		mBillDetailAdapter = new BillDetailAdapter();
		mLvBill.setAdapter(mBillAdapter);
		mLvBillDetail.setAdapter(mBillDetailAdapter);
		
		tvSaleDate.setText(mFormat.dateFormat(mCalendar.getTime()));
	    
	    btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				searchBill();
			}
	    	
	    });
	    
		mLvBill.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Calendar c = Calendar.getInstance();
				MPOSOrderTransaction trans = (MPOSOrderTransaction) parent.getItemAtPosition(position);
				c.setTimeInMillis(Long.parseLong(trans.getPaidTime()));
				
				mTransactionId = trans.getTransactionId();
				mComputerId = trans.getComputerId();
				mReceiptNo = trans.getReceiptNo();
				mReceiptDate = mFormat.dateTimeFormat(c.getTime());
				
				if(trans.getTransactionStatusId() == Transaction.TRANS_STATUS_SUCCESS)
					mItemConfirm.setEnabled(true);
				else if(trans.getTransactionStatusId() == Transaction.TRANS_STATUS_VOID)
					mItemConfirm.setEnabled(false);
				searchVoidItem();
			}
		});

	    Intent intent = getIntent();
	    mStaffId = intent.getIntExtra("staffId", 0);
	    mShopId = intent.getIntExtra("shopId", 0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = new Intent(this, SaleService.class);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mBound){
			unbindService(mServiceConnection);
			mBound = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_void_bill, menu);
		mItemConfirm = menu.findItem(R.id.itemConfirm);
		mItemConfirm.setEnabled(false);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			cancel();
			return true;
		case R.id.itemConfirm:
			confirm();
			return true;
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	private class BillAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public BillAdapter(){
			mInflater = (LayoutInflater) 
					VoidBillActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return mTransLst != null ? mTransLst.size() : 0;
		}

		@Override
		public MPOSOrderTransaction getItem(int position) {
			return mTransLst.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.receipt_template, null);
				holder = new ViewHolder();
				holder.tvReceiptNo = (TextView) convertView.findViewById(R.id.tvReceiptNo);
				holder.tvPaidTime = (TextView) convertView.findViewById(R.id.tvPaidTime);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			final MPOSOrderTransaction trans = mTransLst.get(position);
			Calendar c = Calendar.getInstance();
			try {
				c.setTimeInMillis(Long.parseLong(trans.getPaidTime()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.tvReceiptNo.setText(trans.getReceiptNo());
			holder.tvPaidTime.setText(mFormat.dateTimeFormat(c.getTime()));
			if(trans.getTransactionStatusId() == Transaction.TRANS_STATUS_VOID){
				holder.tvReceiptNo.setTextColor(Color.RED);
				holder.tvReceiptNo.setPaintFlags(holder.tvReceiptNo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}else{
				holder.tvReceiptNo.setTextColor(Color.BLACK);
				holder.tvReceiptNo.setPaintFlags(holder.tvReceiptNo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			}
			return convertView;
		}
		
		class ViewHolder{
			TextView tvReceiptNo;
			TextView tvPaidTime;
		}
	}
	
	private class BillDetailAdapter extends BaseAdapter{
		
		LayoutInflater inflater;
		
		public BillDetailAdapter(){
			inflater = LayoutInflater.from(VoidBillActivity.this);
		}
		
		@Override
		public int getCount() {
			return mOrderLst != null ? mOrderLst.size() : 0;
		}

		@Override
		public MPOSOrderTransaction.OrderDetail getItem(int position) {
			return mOrderLst.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MPOSOrderTransaction.OrderDetail order = mOrderLst.get(position);
			ViewHolder holder;
			
			if(convertView == null){
				convertView = inflater.inflate(R.layout.void_item_template, null);
				holder = new ViewHolder();
				
				holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
				holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
				holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
		
			holder.tvItem.setText(order.getProductName());
			holder.tvQty.setText(mFormat.qtyFormat(order.getQty()));
			holder.tvPrice.setText(mFormat.currencyFormat(order.getPricePerUnit()));
			holder.tvTotalPrice.setText(mFormat.currencyFormat(order.getTotalRetailPrice()));
			
			return convertView;
		}
		
		private class ViewHolder{
			TextView tvItem;
			TextView tvQty;
			TextView tvPrice;
			TextView tvTotalPrice;
		}
	}
	
	private void searchBill(){
		txtReceiptNo.setText("");
		txtReceiptDate.setText("");
		
		mTransLst = mTrans.listTransaction(mDate);
		if(mTransLst.size() == 0){
			new AlertDialog.Builder(VoidBillActivity.this)
			.setTitle(R.string.void_bill)
			.setMessage(R.string.not_found_bill)
			.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
		mBillAdapter.notifyDataSetChanged();
	}
	
	private void searchVoidItem(){
		txtReceiptNo.setText(mReceiptNo);
		txtReceiptDate.setText(mReceiptDate);
		
		mOrderLst = mTrans.listAllOrder(mTransactionId);
		mBillDetailAdapter.notifyDataSetChanged();
	}

	public void confirm() {
		LayoutInflater inflater = (LayoutInflater)
				VoidBillActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inputLayout = inflater.inflate(R.layout.input_text_layout, null);
		final EditText txtVoidReason = (EditText) inputLayout.findViewById(R.id.editText1);
		txtVoidReason.setHint(R.string.reason);
		AlertDialog.Builder builder = new AlertDialog.Builder(VoidBillActivity.this);
		builder.setTitle(R.string.void_bill);
		builder.setView(inputLayout);
		builder.setMessage(R.string.confirm_void_bill);
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(txtVoidReason.getWindowToken(), 0);
			}
		});
		builder.setPositiveButton(R.string.yes, null);
		
		final AlertDialog d = builder.create();
		d.show();
		Button btnOk = d.getButton(AlertDialog.BUTTON_POSITIVE);
		
		btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String voidReason = txtVoidReason.getText().toString();
				if(!voidReason.isEmpty()){
					mTrans.voidTransaction(mTransactionId, mStaffId, voidReason);
					new AlertDialog.Builder(VoidBillActivity.this)
					.setTitle(R.string.void_bill)
					.setMessage(R.string.void_bill_success)
					.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
					mItemConfirm.setEnabled(false);
					d.dismiss();
					searchBill();
					printReceipt();
					sendSale();
				}
			}
			
		});
	}
	
	private void printReceipt(){
		PrintReceiptLog printLog = 
				new PrintReceiptLog(getApplicationContext());
		printLog.insertLog(mTransactionId, mStaffId);
		
		new PrintReceipt(VoidBillActivity.this, new PrintReceipt.PrintStatusListener() {
			
			@Override
			public void onPrintSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPrintFail(String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPrepare() {
				// TODO Auto-generated method stub
				
			}
		}).execute();
	}
	
	private void sendSale(){
		mPartService.sendSale(mShopId, mComputerId, mStaffId, 
				false, new ProgressListener(){

			@Override
			public void onPre() {
			}

			@Override
			public void onPost() {
			}

			@Override
			public void onError(String msg) {
				
			}
			
		});
	}

	private void cancel() {
		finish();
	}
	
	/**
	 * PartialSaleService Connection
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mPartService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
		
	};
}
