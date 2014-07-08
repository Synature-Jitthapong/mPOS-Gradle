package com.synature.mpos;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.synature.exceptionhandler.ExceptionHandler;
import com.synature.mpos.database.Bank;
import com.synature.mpos.database.CreditCard;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.PaymentDetail;
import com.synature.pos.BankName;
import com.synature.pos.CreditCardType;
import com.synature.util.CreditCardParser;
import com.synature.util.Logger;
import com.synature.util.VerifyCardType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class CreditPayActivity extends Activity implements TextWatcher, 
	Runnable{
	
	public static final String TAG = "CreditPayActivity";
	
	/*
	 * is magnatic read state
	 */
	private boolean mIsRead = false;
	
	/*
	 * Thread for run magnetic reader listener 
	 */
	private Thread mMsrThread;

	private WintecMagneticReader mMsrReader;
	
	private PaymentDetail mPayment;
	private Formater mFormat;
	
	private List<BankName> mBankLst;
	private List<CreditCardType> mCreditCardLst;
	
	private int mTransactionId;
	private int mComputerId;
	private int mBankId;
	private int mCardTypeId;
	private int mExpYear;
	private int mExpMonth;
	private double mPaymentLeft;
	private double mTotalCreditPay;
	
	private EditText mTxtTotalPrice;
	private EditText mTxtTotalPay;
	private EditText mTxtCardHolderName;
	private EditText mTxtCardNoSeq1;
	private EditText mTxtCardNoSeq2;
	private EditText mTxtCardNoSeq3;
	private EditText mTxtCardNoSeq4;
	private Spinner mSpBank;
	private Spinner mSpCardType;
	private Spinner mSpExpYear;
	private Spinner mSpExpMonth;
	
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
	    params.width = WindowManager.LayoutParams.MATCH_PARENT;
	    params.height= getResources().getInteger(R.integer.activity_dialog_height);
	    params.alpha = 1.0f;
	    params.dimAmount = 0.5f;
	    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); 
		setContentView(R.layout.activity_credit_pay);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mTxtTotalPrice = (EditText) findViewById(R.id.txtCardTotalPrice);
		mTxtTotalPay = (EditText) findViewById(R.id.txtCardPayAmount);
		mTxtCardNoSeq1 = (EditText) findViewById(R.id.txtCardNoSeq1);
		mTxtCardNoSeq2 = (EditText) findViewById(R.id.txtCardNoSeq2);
		mTxtCardNoSeq3 = (EditText) findViewById(R.id.txtCardNoSeq3);
		mTxtCardNoSeq4 = (EditText) findViewById(R.id.txtCardNoSeq4);
		mTxtCardHolderName = (EditText) findViewById(R.id.txtCardHolderName);
		mSpBank = (Spinner) findViewById(R.id.spBank);
		mSpCardType = (Spinner) findViewById(R.id.spCardType);
		mSpExpYear = (Spinner) findViewById(R.id.spExpYear);
		mSpExpMonth = (Spinner) findViewById(R.id.spExpMonth);
		mTxtTotalPay.setSelectAllOnFocus(true);
		mTxtCardNoSeq1.addTextChangedListener(this);
		mTxtCardNoSeq2.addTextChangedListener(this);
		mTxtCardNoSeq3.addTextChangedListener(this);
		mTxtCardNoSeq4.addTextChangedListener(this);
		
		mSpExpYear.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				String year = (String)parent.getItemAtPosition(position);
				mExpYear = Integer.parseInt(year);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mSpExpMonth.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				String month = (String)parent.getItemAtPosition(position);
				mExpMonth = Integer.parseInt(month) + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mPayment = new PaymentDetail(getApplicationContext());
		mFormat = new Formater(getApplicationContext());
		
		Intent intent = getIntent();
		mTransactionId = intent.getIntExtra("transactionId", 0);
		mComputerId = intent.getIntExtra("computerId", 0);
		mPaymentLeft = intent.getDoubleExtra("paymentLeft", 0.0d);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// start magnetic reader thread
		try {
			mMsrReader = new WintecMagneticReader(this);
			mMsrThread = new Thread(this);
			mMsrThread.start();
			mIsRead = true;
			Logger.appendLog(this, Utils.LOG_DIR, 
					Utils.LOG_FILE_NAME, "Start magnetic reader thread");
		} catch (Exception e) {
			Logger.appendLog(this, Utils.LOG_DIR, 
					Utils.LOG_FILE_NAME, 
					"Error start magnetic reader thread " + 
					e.getMessage());
		}
		//test();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mMsrReader.close();
		mIsRead = false;
		closeMsrThread();
	}

	@Override
	protected void onResume() {
		init();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_credit_pay, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		case R.id.itemConfirm:
			confirm();
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void createSpinnerYear(){
		String[] years = new String[10];
		Calendar c = Calendar.getInstance(Locale.US);
		for(int i = 0; i < years.length; i++){
			years[i] = String.valueOf(c.get(Calendar.YEAR) + i);
		}
		SpinnerAdapter adapter = new ArrayAdapter<String>(CreditPayActivity.this, 
				android.R.layout.simple_spinner_dropdown_item, years);
		mSpExpYear.setAdapter(adapter);
	}
	
	private void createSpinnerMonth(){
		String[] months = new String[12];
		for(int i = 0; i < months.length; i++){
			months[i] = String.format("%02d", i + 1);
		}
		SpinnerAdapter adapter = new ArrayAdapter<String>(CreditPayActivity.this, 
				android.R.layout.simple_spinner_dropdown_item, months);
		mSpExpMonth.setAdapter(adapter);
		Calendar c = Calendar.getInstance(Locale.getDefault());
		mSpExpMonth.setSelection(c.get(Calendar.MONTH));
	}
	
	private void init(){
		displayTotalPrice();
		loadCreditCardType();
		loadBankName();
		createSpinnerMonth();
		createSpinnerYear();
	}
	
	private void displayTotalPrice(){
		mTxtTotalPrice.setText(mFormat.currencyFormat(mPaymentLeft));
		mTxtTotalPay.setText(mTxtTotalPrice.getText());
	}
	
//	private boolean checkCardNoSeq(){
//		boolean already = false;
//		if(!mTxtCardNoSeq1.equals("") && !mTxtCardNoSeq2.equals("") && 
//				!mTxtCardNoSeq3.equals("") && !mTxtCardNoSeq4.equals("")){
//			if(mTxtCardNoSeq1.getText().toString().length() < 4){
//				already = false;
//				mTxtCardNoSeq1.requestFocus();
//			}
//			else if(mTxtCardNoSeq2.getText().toString().length() < 4){
//				already = false;
//				mTxtCardNoSeq2.requestFocus();
//			}
//			else if(mTxtCardNoSeq3.getText().toString().length() < 4){
//				already = false;
//				mTxtCardNoSeq3.requestFocus();
//			}
//			else if(mTxtCardNoSeq4.getText().toString().length() < 4){
//				already = false;
//				mTxtCardNoSeq4.requestFocus();
//			}
//			else{
//				already = true;
//			}
//		}else{
//			already = false;
//		}
//		return already;
//	}

	public void confirm(){
		//if (checkCardNoSeq()) {
			//if (!mTxtCVV2.getText().toString().isEmpty()) {
				try {
					mTotalCreditPay = Utils.stringToDouble(
							mTxtTotalPay.getText().toString());
				} catch (ParseException e) {
					Logger.appendLog(this, Utils.LOG_DIR, 
							Utils.LOG_FILE_NAME, e.getMessage());
				}
				
				if (mTotalCreditPay > 0) {
//					LayoutInflater inflater = (LayoutInflater) 
//							CreditPayActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					View cardConfirmView = inflater.inflate(R.layout.confirm_credit_pay_layout, null);
//					((EditText) cardConfirmView.findViewById(R.id.txtTotalPay))
//						.setText(mTxtTotalPay.getText().toString());
//					((EditText) cardConfirmView.findViewById(R.id.txtCardNo))
//						.setText(mTxtCardNoSeq1.getText().toString() + "-" +
//								mTxtCardNoSeq2.getText().toString() + "-" +
//								mTxtCardNoSeq3.getText().toString() + "-" +
//								mTxtCardNoSeq4.getText().toString());
//					((EditText) cardConfirmView.findViewById(R.id.txtCardType))
//						.setText(mSpCardType.getItemAtPosition(mSpCardType.getSelectedItemPosition()).toString());
//					((EditText) cardConfirmView.findViewById(R.id.txtBank))
//						.setText(mSpBank.getItemAtPosition(mSpBank.getSelectedItemPosition()).toString());
//					((EditText) cardConfirmView.findViewById(R.id.txtExpDate))
//						.setText(mSpExpMonth.getItemAtPosition(mSpExpMonth.getSelectedItemPosition()).toString() + "/" +
//								mSpExpYear.getItemAtPosition(mSpExpYear.getSelectedItemPosition()).toString());
//					AlertDialog.Builder builder = new AlertDialog.Builder(CreditPayActivity.this);
//					builder.setTitle(R.string.credit_pay);
//					builder.setView(cardConfirmView);
//					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//						}
//					});
//					builder.setNeutralButton(android.R.string.ok, null);
//					
//					final AlertDialog d = builder.create();
//					d.show();
//					
//					Button btnConfirm = d.getButton(AlertDialog.BUTTON_NEUTRAL);
//					btnConfirm.setOnClickListener(new OnClickListener(){
//
//						@Override
//						public void onClick(View v) {
							String cardNo = mTxtCardNoSeq1.getText().toString()
									+ mTxtCardNoSeq2.getText().toString()
									+ mTxtCardNoSeq3.getText().toString()
									+ mTxtCardNoSeq4.getText().toString();
							try {
								mPayment.addPaymentDetail(mTransactionId, mComputerId,
										PaymentDetail.PAY_TYPE_CREDIT,
										mTotalCreditPay, mTotalCreditPay >= mPaymentLeft ?
												mPaymentLeft : mTotalCreditPay, cardNo, mExpMonth,
										mExpYear, mBankId, mCardTypeId, "");
								//d.dismiss();
								Intent intent = new Intent();
								if(mTotalCreditPay >= mPaymentLeft)
									setResult(PaymentActivity.RESULT_ENOUGH, intent);
								else
									setResult(PaymentActivity.RESULT_NOT_ENOUGH, intent);
								finish();
							} catch (SQLException e) {
								new AlertDialog.Builder(CreditPayActivity.this)
								.setMessage(e.getMessage())
								.setNeutralButton(R.string.close, new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
									
								}).show();
							}
//							
//						}
//						
//					});
				} else {
					mTxtTotalPay.requestFocus();
					new AlertDialog.Builder(CreditPayActivity.this)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(R.string.payment)
							.setMessage(R.string.enter_enough_money)
							.setNeutralButton(R.string.close,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
				}
//			} else {
//				mTxtCVV2.requestFocus();
//				new AlertDialog.Builder(CreditPayActivity.this)
//				.setIcon(android.R.drawable.ic_dialog_alert)
//				.setTitle(R.string.payment)
//				.setMessage(R.string.promp_cvv2)
//				.setNeutralButton(R.string.close,
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//
//							}
//						}).show();
//			}
//		} else {
//			new AlertDialog.Builder(CreditPayActivity.this)
//			.setIcon(android.R.drawable.ic_dialog_alert)
//			.setTitle(R.string.payment)
//			.setMessage(R.string.promp_card_no)
//			.setNeutralButton(R.string.close,
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog,
//								int which) {
//
//						}
//					}).show();
//		}	
	}
	
	private void loadCreditCardType(){
		CreditCard cd = new CreditCard(getApplicationContext());
		mCreditCardLst = cd.listAllCreditCardType();
		
		CreditCardType cc = new CreditCardType();
		cc.setCreditCardTypeId(0);
		cc.setCreditCardTypeName(getString(R.string.spinner_please_select));
		mCreditCardLst.add(0, cc);
		
		ArrayAdapter<CreditCardType> adapter = 
				new ArrayAdapter<CreditCardType>(CreditPayActivity.this, 
						android.R.layout.simple_dropdown_item_1line, mCreditCardLst);
		mSpCardType.setAdapter(adapter);
		mSpCardType.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				CreditCardType card = (CreditCardType) parent.getItemAtPosition(position);
				mCardTypeId = card.getCreditCardTypeId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void loadBankName(){
		Bank bk = new Bank(getApplicationContext());
		mBankLst = bk.listAllBank();
		
		BankName b = new BankName();
		b.setBankNameId(0);
		b.setBankName(getString(R.string.spinner_please_select));
		mBankLst.add(0, b);
		
		ArrayAdapter<BankName> adapter = 
				new ArrayAdapter<BankName>(CreditPayActivity.this, 
						android.R.layout.simple_dropdown_item_1line, mBankLst);
		mSpBank.setAdapter(adapter);
		mSpBank.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				BankName bank = (BankName) parent.getItemAtPosition(position);
				mBankId = bank.getBankNameId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public void afterTextChanged(Editable txt) {
		View v = getCurrentFocus();
		if(v instanceof EditText){
			switch(v.getId()){
			case R.id.txtCardNoSeq1:
				if(mTxtCardNoSeq1.getText().toString().length() == 4)
					mTxtCardNoSeq2.requestFocus();
			case R.id.txtCardNoSeq2:
				if(mTxtCardNoSeq2.getText().toString().length() == 4)
					mTxtCardNoSeq3.requestFocus();
			case R.id.txtCardNoSeq3:
				if(mTxtCardNoSeq3.getText().toString().length() == 4)
					mTxtCardNoSeq4.requestFocus();
			case R.id.txtCardNoSeq4:
				if(mTxtCardNoSeq4.getText().toString().length() == 4)
					mSpCardType.requestFocus();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	/*
	 * Close magnetic reader thread
	 */
	private void closeMsrThread(){
		if(mMsrThread != null){
			mMsrThread.interrupt();
			mMsrThread = null;
		}
	}

	/*
	 * Listener for magnetic reader
	 */
	@Override
	public void run() {
		while(mIsRead){
			try {
				final String content = mMsrReader.getTrackData();
				
				if(content.length() > 0){
					Logger.appendLog(getApplicationContext(), 
						Utils.LOG_DIR, Utils.LOG_FILE_NAME,
						"Content : " + content);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							try {
								CreditCardParser parser = new CreditCardParser();
								if(parser.parser(content)){
									String cardNo = parser.getCardNo();
									String cardHolderName = parser.getCardHolderName();
									String expDate = parser.getExpDate();
									
									mTxtCardNoSeq1.setText(null);
									mTxtCardNoSeq2.setText(null);
									mTxtCardNoSeq3.setText(null);
									mTxtCardNoSeq4.setText(null);
									mTxtCardHolderName.setText(null);
									
									mTxtCardNoSeq1.setText(cardNo.substring(0, 4));
									mTxtCardNoSeq2.setText(cardNo.substring(4, 8));
									mTxtCardNoSeq3.setText(cardNo.substring(8, 12));
									mTxtCardNoSeq4.setText(cardNo.substring(12, 16));
									mTxtCardHolderName.setText(cardHolderName);
									
									try {
										ArrayAdapter yearAdapter = (ArrayAdapter) mSpExpYear.getAdapter();
										ArrayAdapter monthAdapter = (ArrayAdapter) mSpExpMonth.getAdapter();
										int yearPosition = yearAdapter.getPosition("20" + expDate.substring(0, 2));
										int monthPosition = monthAdapter.getPosition(expDate.substring(2, 4));
										mSpExpYear.setSelection(yearPosition);
										mSpExpMonth.setSelection(monthPosition);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									try {
										VerifyCardType.CardType cardType = VerifyCardType.checkCardType(cardNo); 
										switch(cardType){
										case VISA:
											mSpCardType.setSelection(1);
											break;
										case MASTER:
											mSpCardType.setSelection(2);
											break;
										default:
											mSpCardType.setSelection(0);
										}
									} catch (Exception e) {
										Logger.appendLog(getApplicationContext(), 
												Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
												"Error set selected spinner card type");
									}
									
									Logger.appendLog(getApplicationContext(), 
											Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
											"CARD NO : " + cardNo + " \n " +
											"CARD HOLDER NAME : " + cardHolderName + "\n" +
											"EXP DATE : " + expDate);	
								}
							} catch (Exception e) {
								new AlertDialog.Builder(CreditPayActivity.this)
								.setTitle(R.string.credit_card)
								.setMessage("Error parser card data " + e.getMessage())
								.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								})
								.show();
								Logger.appendLog(getApplicationContext(), 
										Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
										"Error " + e.getMessage());
							}
						}
						
					});
				}
			} catch (Exception e) {
				Logger.appendLog(getApplicationContext(), 
						Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
						" Error when read data from magnetic card : " + e.getMessage());
			}
		}
	}

	// debug test
	private void test(){
		String content = "Track2:0025870064605584=1299=330001234=16824?";
		String content1 = "Track1:B4552939410162971^JITTHAPONG ARJSALEE       ^170220100000   876540039600C000?"

+"Track2:4552939410162971=17022010000039687654?";
		try {
			CreditCardParser parser = new CreditCardParser();
			if(parser.parser(content1)){
				mTxtCardNoSeq1.setText(null);
				mTxtCardNoSeq2.setText(null);
				mTxtCardNoSeq3.setText(null);
				mTxtCardNoSeq4.setText(null);
				mTxtCardHolderName.setText(null);
				
				String cardNo = parser.getCardNo();
				String cardHolderName = parser.getCardHolderName();
				String expDate = parser.getExpDate();
				
				mTxtCardNoSeq1.setText(cardNo.substring(0, 4));
				mTxtCardNoSeq2.setText(cardNo.substring(4, 8));
				mTxtCardNoSeq3.setText(cardNo.substring(8, 12));
				mTxtCardNoSeq4.setText(cardNo.substring(12, 16));
				mTxtCardHolderName.setText(cardHolderName);
				//mSpExpMonth.setSelection();
				
				try {
					VerifyCardType.CardType cardType = VerifyCardType.checkCardType(cardNo); 
					switch(cardType){
					case VISA:
						mSpCardType.setSelection(1);
						break;
					case MASTER:
						mSpCardType.setSelection(2);
						break;
					default:
						mSpCardType.setSelection(0);
					}
				} catch (Exception e) {
					Logger.appendLog(getApplicationContext(), 
							Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
							"Error set selected spinner card type");
				}
			}
		} catch (Exception e) {
			new AlertDialog.Builder(CreditPayActivity.this)
			.setTitle(R.string.credit_card)
			.setMessage("Error parser card data " + e.getMessage())
			.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.show();
			Logger.appendLog(getApplicationContext(), 
					Utils.LOG_DIR, Utils.LOG_FILE_NAME, 
					"Error " + e.getMessage());
		}
	}
}
