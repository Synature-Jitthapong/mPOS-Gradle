package com.synature.mpos;

import java.util.Calendar;

import com.synature.mpos.database.Computer;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.Session;
import com.synature.mpos.database.Shop;
import com.synature.mpos.database.SyncMasterLog;
import com.synature.mpos.database.UserVerification;
import com.synature.mpos.ui.SystemUiHider;
import com.synature.pos.ShopData;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity{
	
	/**
	 * Request code for set system date
	 */
	public static final int REQUEST_FOR_SETTING_DATE = 1;
	/**
	 * Request code for setting
	 */
	public static final int REQUEST_FOR_SETTING = 2;
	
	public static final int CLICK_TIMES_TO_SETTING = 5;
	
	private int mStaffId;
	
	private Shop mShop;
	private Session mSession;
	private Computer mComputer;
	private Formater mFormat;
	private SyncMasterLog mSync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mSession = new Session(this);
		mShop = new Shop(this);
		mComputer = new Computer(this);
		mFormat = new Formater(this);
		mSync = new SyncMasterLog(this);

		if(savedInstanceState == null){
			getFragmentManager().beginTransaction()
				.add(R.id.loginContent, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_SETTING_DATE){
			if(resultCode == RESULT_OK){
				gotoMainActivity();
			}
		}
		if(requestCode == REQUEST_FOR_SETTING){
			if(resultCode == RESULT_OK){
				if(!mSync.IsAlreadySync())
					updateData();
			}
		}
	}

	/**
	 * Compare system date with session date
	 * if system date less than session date 
	 * this not allow to do anything and 
	 * force to date & time setting.
	 */
	private void checkSessionDate(){
		if(mSession.getCurrentSessionId() > 0){
			final Calendar sessionDate = Calendar.getInstance();
			sessionDate.setTimeInMillis(Long.parseLong(mSession.getSessionDate()));
			/*
			 *  sessionDate > currentDate
			 *  mPOS will force to go to date & time Settings
			 *  for setting correct date.
			 */
			if(sessionDate.getTime().compareTo(Utils.getDate().getTime()) > 0){
				new AlertDialog.Builder(this)
				.setCancelable(false)
				.setTitle(R.string.system_date)
				.setMessage(R.string.system_date_less)
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton(R.string.date_time_setting, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivityForResult(
								new Intent(android.provider.Settings.ACTION_DATE_SETTINGS),
								REQUEST_FOR_SETTING_DATE);
					}
				}).show();
			}
			
			/*
			 * Current date > Session date
			 * mPOS will force to end day.
			 */
			else if(Utils.getDate().getTime().compareTo(sessionDate.getTime()) > 0){
				// check last session has already enddday ?
				if(!mSession.checkEndday(mSession.getSessionDate())){
					Utils.endday(LoginActivity.this, mShop.getShopId(), 
							mComputer.getComputerId(), mSession.getCurrentSessionId(), 
							mStaffId, 0, true);
					// force end previous sale date
//					new AlertDialog.Builder(this)
//					.setCancelable(false)
//					.setTitle(R.string.system_date)
//					.setMessage(R.string.system_date_more_than_session)
//					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//						}
//					})
//					.setPositiveButton(R.string.endday, new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							boolean endday = Utils.endday(LoginActivity.this, mShop.getShopId(), 
//									mComputer.getComputerId(), mSession.getCurrentSessionId(), 
//									mStaffId, 0, true);
//							if(endday){
//								new AlertDialog.Builder(LoginActivity.this)
//								.setCancelable(false)
//								.setTitle(R.string.endday)
//								.setMessage(R.string.endday_success)
//								.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
//									
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										gotoMainActivity();
//									}
//								}).show();
//							}
//						}
//					}).show();
				}else{
					gotoMainActivity();
				}
			}else{
				gotoMainActivity();
			}
		}else{
			gotoMainActivity();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch(item.getItemId()){
		case R.id.itemSetting:
			intent = new Intent(LoginActivity.this, SettingsActivity.class);
			startActivityForResult(intent, REQUEST_FOR_SETTING);
			return true;
		case R.id.itemUpdate:
			updateData();
			return true;
		case R.id.itemAbout:
			intent = new Intent(LoginActivity.this, AboutActivity.class);
			startActivity(intent);
			return true;
		case R.id.itemClearSale:
			Utils.clearSale(LoginActivity.this);
			return true;
		case R.id.itemExit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);	
		}
	}

	private void updateData(){
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setTitle(R.string.update_data);
		// checking device
		MPOSWebServiceClient.authenDevice(this, new MPOSWebServiceClient.AuthenDeviceListener() {
			
			@Override
			public void onPre() {
				progress.setMessage(getString(R.string.check_device_progress));
				progress.show();
			}
			
			@Override
			public void onPost() {
			}
			
			@Override
			public void onError(String msg) {
				if(progress.isShowing())
					progress.dismiss();
				new AlertDialog.Builder(LoginActivity.this)
				.setCancelable(false)
				.setTitle(R.string.update_data)
				.setMessage(msg)
				.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
			}
			
			@Override
			public void onPost(final int shopId) {
				// load shop data
				MPOSWebServiceClient.loadShopData(LoginActivity.this, shopId, new ProgressListener(){

					@Override
					public void onPre() {
						progress.setMessage(getString(R.string.update_shop_progress));
					}

					@Override
					public void onPost() {
						// load product datat
						MPOSWebServiceClient.loadProductData(LoginActivity.this, shopId, new ProgressListener(){

							@Override
							public void onPre() {
								progress.setMessage(getString(R.string.update_product_progress));
							}

							@Override
							public void onPost() {
								if(progress.isShowing())
									progress.dismiss();
								new AlertDialog.Builder(LoginActivity.this)
								.setCancelable(false)
								.setTitle(R.string.update_data)
								.setMessage(R.string.update_data_success)
								.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										startActivity(new Intent(LoginActivity.this, LoginActivity.class));
										finish();
									}
								}).show();
							}

							@Override
							public void onError(String msg) {
								if(progress.isShowing())
									progress.dismiss();
								new AlertDialog.Builder(LoginActivity.this)
								.setCancelable(false)
								.setTitle(R.string.update_data)
								.setMessage(msg)
								.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								}).show();
							}
							
						});
					}

					@Override
					public void onError(String msg) {
						if(progress.isShowing())
							progress.dismiss();
						new AlertDialog.Builder(LoginActivity.this)
						.setCancelable(false)
						.setTitle(R.string.update_data)
						.setMessage(msg)
						.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					}
					
				});
			}
		});
	}
	
	@Override
	protected void onResume() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String url = sharedPref.getString(SettingsActivity.KEY_PREF_SERVER_URL, "");
		if(url.isEmpty()){
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, REQUEST_FOR_SETTING);
		}else{
			PlaceholderFragment placeHolder = (PlaceholderFragment) 
					getFragmentManager().findFragmentById(R.id.loginContent);
			placeHolder.mTxtUser.requestFocus();
		}
		super.onResume();
	}
			
	private void gotoMainActivity(){
		PlaceholderFragment placeHolder = (PlaceholderFragment) 
				getFragmentManager().findFragmentById(R.id.loginContent);
		placeHolder.mTxtUser.setText(null);
		placeHolder.mTxtPass.setText(null);
		if(mSession.checkEndday(String.valueOf(Utils.getDate().getTimeInMillis()))){
			String enddayMsg = getString(R.string.sale_date) 
					+ " " + mFormat.dateFormat(Utils.getDate().getTime()) 
					+ " " + getString(R.string.alredy_endday);
			new AlertDialog.Builder(this)
			.setCancelable(false)
			.setTitle(R.string.endday)
			.setMessage(enddayMsg)
			.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}else{
			enddingMultipleDay();
			startEnddayService();
			final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.putExtra("staffId", mStaffId);
			if(!mSync.IsAlreadySync()){
				final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
				progress.setCancelable(false);
				progress.setTitle(R.string.update_data);
				// load shop data
				MPOSWebServiceClient.loadShopData(LoginActivity.this, mShop.getShopId(), new ProgressListener(){
	
					@Override
					public void onPre() {
						progress.setMessage(getString(R.string.update_shop_progress));
						progress.show();
					}
	
					@Override
					public void onPost() {
						// load product data
						MPOSWebServiceClient.loadProductData(LoginActivity.this, mShop.getShopId(), new ProgressListener(){
	
							@Override
							public void onPre() {
								progress.setMessage(getString(R.string.update_product_progress));
							}
	
							@Override
							public void onPost() {
								if(progress.isShowing())
									progress.dismiss();
								startActivity(intent);
								finish();
							}
	
							@Override
							public void onError(String msg) {
								if(progress.isShowing())
									progress.dismiss();
								new AlertDialog.Builder(LoginActivity.this)
								.setCancelable(false)
								.setTitle(R.string.update_data)
								.setMessage(getString(R.string.cannot_update_data) + "\n" + msg)
								.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										startActivity(intent);
										finish();
									}
								}).show();
							}
							
						});
					}
	
					@Override
					public void onError(String msg) {
						if(progress.isShowing())
							progress.dismiss();
						new AlertDialog.Builder(LoginActivity.this)
						.setCancelable(false)
						.setTitle(R.string.update_data)
						.setMessage(getString(R.string.cannot_update_data) + "\n" + msg)
						.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(intent);
								finish();
							}
						}).show();
					}
					
				});
			}else{
				startActivity(intent);
				finish();
			}
		}
	}
	
	private void startEnddayService(){
		// start endday service for send endday sale
		Intent enddayIntent = new Intent(LoginActivity.this, EnddaySaleService.class);
		enddayIntent.putExtra("staffId", mStaffId);
		enddayIntent.putExtra("shopId", mShop.getShopId());
		enddayIntent.putExtra("computerId", mComputer.getComputerId());
		startService(enddayIntent);
	}
	
	private void enddingMultipleDay(){
		String sessionDate = mSession.getSessionDate();
		if(!sessionDate.equals("")){
			Calendar sessCal = Calendar.getInstance();
			sessCal.setTimeInMillis(Long.parseLong(sessionDate));
			if(Utils.getDiffDay(sessCal) > 0){
				Utils.endingMultipleDay(LoginActivity.this, mShop.getShopId(), 
						mComputer.getComputerId(), mStaffId, sessCal);
			}
		}
	}
	
	public void checkLogin(){
		String user = "";
		String pass = "";
	
		PlaceholderFragment placeHolder = (PlaceholderFragment) 
				getFragmentManager().findFragmentById(R.id.loginContent);
		
		if(!placeHolder.mTxtUser.getText().toString().isEmpty()){
			user = placeHolder.mTxtUser.getText().toString();
			
			if(!placeHolder.mTxtPass.getText().toString().isEmpty()){
				pass = placeHolder.mTxtPass.getText().toString();
				UserVerification login = new UserVerification(getApplicationContext(), user, pass);
				
				if(login.checkUser()){
					ShopData.Staff s = login.checkLogin();
					if(s != null){
						mStaffId = s.getStaffID();
						checkSessionDate();
						placeHolder.mTxtUser.setError(null);
						placeHolder.mTxtPass.setError(null);
					}else{
						placeHolder.mTxtUser.setError(null);
						placeHolder.mTxtPass.setError(getString(R.string.incorrect_password));
					}
				}else{
					placeHolder.mTxtUser.setError(getString(R.string.incorrect_user));
					placeHolder.mTxtPass.setError(null);
				}
			}else{
				placeHolder.mTxtUser.setError(null);
				placeHolder.mTxtPass.setError(getString(R.string.enter_password));
			}
		}else{
			placeHolder.mTxtUser.setError(getString(R.string.enter_username));
			placeHolder.mTxtPass.setError(null);
		}
	}
	
	public static class PlaceholderFragment extends Fragment{

		private Button mBtnLogin;
		private ImageButton mBtnSetting;
		private EditText mTxtUser;
		private EditText mTxtPass;
		private TextView mTvShopName;
		private TextView mTvSaleDate;
		private TextView mTvLastSession;
		
		private int mNumClick = 0;

		public PlaceholderFragment(){
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);
			mBtnLogin = (Button) rootView.findViewById(R.id.buttonLogin);
			mTxtUser = (EditText) rootView.findViewById(R.id.txtUser);
			mTxtPass = (EditText) rootView.findViewById(R.id.txtPass);
			mTvShopName = (TextView) rootView.findViewById(R.id.tvShopName);
			mTvSaleDate = (TextView) rootView.findViewById(R.id.tvSaleDate);
			mTvLastSession = (TextView) rootView.findViewById(R.id.tvLastSession);
			mBtnSetting = (ImageButton) rootView.findViewById(R.id.btnSetting);
			
			mTxtUser.setSelectAllOnFocus(true);
			mTxtPass.setSelectAllOnFocus(true);
			mBtnLogin.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((LoginActivity) getActivity()).checkLogin();
				}
				
			});
			mBtnSetting.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(++mNumClick == CLICK_TIMES_TO_SETTING){
						mNumClick = 0;
						startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
					}
				}
				
			});
			mTxtPass.setOnEditorActionListener(new OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_DONE){
						((LoginActivity) getActivity()).checkLogin();
						return true;
					}
					return false;
				}
				
			});

			((TextView) rootView.findViewById(R.id.tvDeviceCode))
				.setText(getString(R.string.device_code) + ":" +
						Utils.getDeviceCode(getActivity()));
			
			try {
				Shop shop = ((LoginActivity) getActivity()).mShop;
				Session session = ((LoginActivity) getActivity()).mSession;
				Formater format = ((LoginActivity) getActivity()).mFormat;
				
				if(shop.getShopName() != null)
					mTvShopName.setText(shop.getShopName());
				
				if(session.getSessionDate() != null && 
						!session.getSessionDate().isEmpty())
					mTvLastSession.setText(format.dateFormat(session.getSessionDate()));
				else
					mTvLastSession.setText(" :- ");

				mTvSaleDate.setText(format.dateFormat(Utils.getDate().getTime()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return rootView;
		}
	}
}
