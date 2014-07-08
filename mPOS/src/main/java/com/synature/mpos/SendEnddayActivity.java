package com.synature.mpos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.synature.exceptionhandler.ExceptionHandler;
import com.synature.mpos.SaleService.LocalBinder;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SendEnddayActivity extends Activity {

	private SaleService mPartService;
	private boolean mBound = false;
	
	private Formater mFormat;
	
	private int mStaffId;
	private int mShopId;
	private int mComputerId;
	private boolean mAutoClose = false;
	
	private MenuItem mItemClose;
	private MenuItem mItemSend;
	private MenuItem mItemProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Register ExceptinHandler for catch error when application crash.
		 */
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, 
				Utils.LOG_DIR, Utils.LOG_FILE_NAME));
		
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
	            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    LayoutParams params = getWindow().getAttributes();
	    params.width = 500;
	    params.height= 500;
	    params.alpha = 1.0f;
	    params.dimAmount = 0.5f;
	    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_send_endday);

		Intent intent = getIntent();
		mStaffId = intent.getIntExtra("staffId", 0);
		mShopId = intent.getIntExtra("shopId", 0);
		mComputerId = intent.getIntExtra("computerId", 0);
		mAutoClose = intent.getBooleanExtra("autoClose", false);
		
		mFormat = new Formater(this);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.send_endday, menu);
		mItemClose = menu.findItem(R.id.itemClose);
		mItemSend = menu.findItem(R.id.itemSendAll);
		mItemProgress = menu.findItem(R.id.itemProgress);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				setResult(RESULT_OK);
				finish();
			return true;
			case R.id.itemClose:
				setResult(RESULT_OK);
				finish();
				return true;
			case R.id.itemSendAll:
				mPartService.sendEnddaySale(mStaffId, mShopId, mComputerId, mSendListener);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private ProgressListener mSendListener = new ProgressListener(){

		@Override
		public void onPre() {
			mItemClose.setEnabled(false);
			mItemSend.setVisible(false);
			mItemProgress.setVisible(true);
		}

		@Override
		public void onPost() {
			mItemClose.setEnabled(true);
			mItemSend.setVisible(true);
			mItemProgress.setVisible(false);
			PlaceholderFragment f = (PlaceholderFragment) getFragmentManager().findFragmentById(R.id.container);
			if(f != null){
				f.setupAdapter();
			}
			if(mAutoClose){
				new AlertDialog.Builder(SendEnddayActivity.this)
				.setCancelable(false)
				.setTitle(R.string.send_endday_data)
				.setMessage(R.string.send_endday_data_success)
				.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_OK);
						finish();
					}
				})
				.show();
			}else{
				Utils.makeToask(SendEnddayActivity.this, getString(R.string.send_sale_data_success));
			}
		}

		@Override
		public void onError(String msg) {
			mItemClose.setEnabled(true);
			mItemSend.setVisible(true);
			mItemProgress.setVisible(false);
			Utils.makeToask(SendEnddayActivity.this, msg);
		}
		
	};
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private SendEnddayActivity mHost;
		
		private Session mSession;
		private List<String> mSessLst;
		private EnddayListAdapter mEnddayAdapter;
		
		private ListView mLvEndday;
		
		public PlaceholderFragment() {
		}

		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mHost = (SendEnddayActivity) getActivity();
			mSession = new Session(getActivity());
			mSessLst = new ArrayList<String>();
		}


		private void setupAdapter(){
			mSessLst = mSession.listSessionEnddayNotSend();
			if(mEnddayAdapter == null){
				mEnddayAdapter = new EnddayListAdapter();
				mLvEndday.setAdapter(mEnddayAdapter);
			}else{
				mEnddayAdapter.notifyDataSetChanged();
			}
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			mLvEndday = (ListView) view.findViewById(R.id.lvEndday);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setupAdapter();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_send_endday,
					container, false);
		}
		
		private class EnddayListAdapter extends BaseAdapter{

			@Override
			public int getCount() {
				return mSessLst != null ? mSessLst.size() : 0;
			}

			@Override
			public Object getItem(int position) {
				return mSessLst.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					LayoutInflater inflater = (LayoutInflater)
							getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.endday_list_template, null);
				}
				String sessionDate = mSessLst.get(position);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(sessionDate));
				TextView tvSaleDate = (TextView) convertView.findViewById(R.id.tvSaleDate);
				tvSaleDate.setText(mHost.mFormat.dateFormat(cal.getTime()));
				return convertView;
			}
			
		}
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
