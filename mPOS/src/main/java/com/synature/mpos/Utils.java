package com.synature.mpos;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synature.mpos.database.Formater;
import com.synature.mpos.database.MPOSDatabase;
import com.synature.mpos.database.Products;
import com.synature.mpos.database.SaleTransaction;
import com.synature.mpos.database.Session;
import com.synature.mpos.database.Transaction;
import com.synature.mpos.database.SaleTransaction.POSData_SaleTransaction;
import com.synature.mpos.database.table.OrderCommentTable;
import com.synature.mpos.database.table.OrderDetailTable;
import com.synature.mpos.database.table.OrderSetTable;
import com.synature.mpos.database.table.OrderTransactionTable;
import com.synature.mpos.database.table.PaymentDetailTable;
import com.synature.mpos.database.table.SessionDetailTable;
import com.synature.mpos.database.table.SessionTable;
import com.synature.util.Logger;

public class Utils {
	/**
	 * WebService file name
	 */
	public static final String WS_NAME = "ws_mpos.asmx";

	/**
	 * Menu image dir
	 */
	public static final String IMG_DIR = "mPOSImg";
	
	/**
	 * Log file name
	 */
	public static final String LOG_FILE_NAME = "mpos_";
	
	/**
	 * Log dir
	 */
	public static final String LOG_DIR = "mPOSLog";


	/**
	 * Sale dir store partial sale json file
	 */
	public static final String LOG_SALE_DIR = LOG_DIR + File.separator + "Sale";
	
	/**
	 * Endday sale dir store endday sale json file
	 */
	public static final String LOG_ENDDAY_DIR = LOG_DIR + File.separator + "EnddaySale";
	
	/**
	 * Image path on server
	 */
	public static final String SERVER_IMG_PATH = "Resources/Shop/MenuImage/";
	
	/**
	 * Android Device Code
	 */
	public static String sDeviceCode;
	
	/**
	 * The minimum date
	 */
	public static final int MINIMUM_YEAR = 1900;
	public static final int MINIMUM_MONTH = 0;
	public static final int MINIMUM_DAY = 1;
	
	/**
	 * @param context
	 * @param shopId
	 * @param computerId
	 * @param sessionId
	 * @param staffId
	 * @param closeAmount
	 * @param isEndday
	 * @param listener
	 */
	public static boolean endday(Context context, int shopId, int computerId, 
			int sessionId, int staffId, double closeAmount, boolean isEndday) {
		Session sess = new Session(context);
		Transaction trans = new Transaction(context);
		String currentSaleDate = sess.getSessionDate(sessionId);
		try {
			try {
				// add session endday
				sess.addSessionEnddayDetail(currentSaleDate,
						trans.getTotalReceipt(currentSaleDate),
						trans.getTotalReceiptAmount(currentSaleDate));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// close session
			sess.closeSession(sessionId,
				staffId, closeAmount, isEndday);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param context
	 * @param shopId
	 * @param computerId
	 * @param staffId
	 * @param lastSessCal
	 * @return true if success endday
	 */
	public static boolean endingMultipleDay(Context context, int shopId, 
			int computerId, int staffId, Calendar lastSessCal){
		int diffDay = getDiffDay(lastSessCal);
		try {
			Session sess = new Session(context);
			Calendar sessCal = (Calendar) lastSessCal.clone();
			for(int i = 1; i < diffDay; i++){
				sessCal.add(Calendar.DAY_OF_MONTH, 1);
				int sessId = sess.openSession(getDate(sessCal.get(Calendar.YEAR), 
						sessCal.get(Calendar.MONTH), sessCal.get(Calendar.DAY_OF_MONTH)), 
						shopId, computerId, staffId, 0);
				sess.addSessionEnddayDetail(String.valueOf(
						sessCal.getTimeInMillis()), 0, 0);
				sess.closeSession(sessId, staffId, 0, true);
			}
			try {
				Formater format = new Formater(context);
				Logger.appendLog(context, LOG_DIR,
						LOG_FILE_NAME,
						"Success ending multiple day : " 
						+ " from : " + format.dateFormat(lastSessCal.getTime())
						+ " to : " + format.dateFormat(Calendar.getInstance().getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.appendLog(context, LOG_DIR,
					LOG_FILE_NAME,
					"Error ending multiple day : " + e.getMessage());
		}
		return false;
	}
	
	public static int getDiffDay(Calendar lastSessCal){
		Calendar currCalendar = Calendar.getInstance();
		int diffDay = 0;
		if(lastSessCal.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)){
			diffDay = currCalendar.get(Calendar.DAY_OF_YEAR) - lastSessCal.get(Calendar.DAY_OF_YEAR);
		}else if(lastSessCal.get(Calendar.YEAR) < currCalendar.get(Calendar.YEAR)){
			diffDay = (lastSessCal.getActualMaximum(Calendar.DAY_OF_YEAR) 
					- lastSessCal.get(Calendar.DAY_OF_YEAR)) + currCalendar.get(Calendar.DAY_OF_YEAR);
		}	
		return diffDay;
	}
	
	/**
	 * @param context
	 * @param saleTrans
	 * @return String JSON Sale
	 */
	public static String generateJSONSale(Context context,
			POSData_SaleTransaction saleTrans) {
		String jsonSale = null;
		try {
			Gson gson = new Gson();
			Type type = new TypeToken<POSData_SaleTransaction>() {}.getType();
			jsonSale = gson.toJson(saleTrans, type);
		} catch (Exception e) {
			Logger.appendLog(context, LOG_DIR,
					LOG_FILE_NAME,
					" Error when generate json sale : " + e.getMessage());
		}
		return jsonSale;
	}
	
	/**
	 * @author j1tth4
	 * task for load OrderTransaction 
	 */
	public static class LoadSaleTransaction extends AsyncTask<Void, Void, POSData_SaleTransaction>{

		protected LoadSaleTransactionListener mListener;
		protected SaleTransaction mSaleTrans;
			
		public LoadSaleTransaction(Context context, String sessionDate,
				boolean isListAll, LoadSaleTransactionListener listener){
			mListener = listener;
			mSaleTrans = new SaleTransaction(context, sessionDate, isListAll);
		}
		
		@Override
		protected void onPreExecute() {
			mListener.onPre();
		}

		@Override
		protected void onPostExecute(POSData_SaleTransaction saleTrans) {
			mListener.onPost(saleTrans);
		}
		
		@Override
		protected POSData_SaleTransaction doInBackground(Void... params) {
			return mSaleTrans.listSaleTransaction();
		}
	}
	
	public static Calendar getCalendar(){
		return Calendar.getInstance(Locale.getDefault());
	}
	
	public static Calendar getMinimum(){
		return new GregorianCalendar(MINIMUM_YEAR, MINIMUM_MONTH, MINIMUM_DAY);
	}
	
	public static Calendar getDate(int year, int month, int day){
		return new GregorianCalendar(year, month, day);
	}
	
	public static Calendar getDate(){
		Calendar c = getCalendar();
		return new GregorianCalendar(c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH), 
				c.get(Calendar.DAY_OF_MONTH));
	}
	
	public static Calendar convertStringToCalendar(String dateTime){
		Calendar calendar = Calendar.getInstance();
		if(dateTime == null || dateTime.isEmpty()){
			dateTime = String.valueOf(getMinimum().getTimeInMillis());
		}
		calendar.setTimeInMillis(Long.parseLong(dateTime));
		return calendar;
	}
	
	public static double calculateVatPrice(double totalPrice, double vatRate, int vatType){
		if(vatType == Products.VAT_TYPE_EXCLUDE)
			return totalPrice * (100 + vatRate) / 100;
		else
			return totalPrice;
	}
	
	public static double calculateVatAmount(double totalPrice, double vatRate, int vatType){
		if(vatType == Products.VAT_TYPE_INCLUDED)
			return totalPrice * vatRate / (100 + vatRate);
		else if(vatType == Products.VAT_TYPE_EXCLUDE)
			return totalPrice * vatRate / 100;
		else
			return 0;
	}
	
	public static void makeToask(Context c, String msg){
		Toast toast = Toast.makeText(c, 
				msg, Toast.LENGTH_LONG);
		toast.show();
	}

	/**
	 * @param scale
	 * @param value
	 * @return string fixes digit
	 */
	public static String fixesDigitLength(Formater format, int scale, double value){
		return format.currencyFormat(value, "#,##0.0000");
	}

	/**
	 * @param scale
	 * @param value
	 * @return rounding value
	 */
//	public static double rounding(int scale, double value){
//		BigDecimal big = new BigDecimal(value);
//		return big.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
//	}
	
	/**
	 * @param price
	 * @return rounding value
	 */
//	public static double roundingPrice(double price){
//		double result = price;
//		long iPart;		// integer part
//		double fPart;	// fractional part
//		iPart = (long) price;
//		fPart = price - iPart;
//		if(fPart < 0.25){
//			fPart = 0.0d;
//		}else if(fPart >= 0.25 && fPart < 0.50){
//			fPart = 0.25d;
//		}else if(fPart >= 0.50 && fPart < 0.75){
//			fPart = 0.50d;
//		}else if(fPart == 0.75){
//			fPart = 0.75d;
//		}else if(fPart > 0.75){
//			iPart += 1;
//			fPart = 0.0d;
//		}
//		result = iPart + fPart;
//		return result;
//		return price;
//	}

	public static double stringToDouble(String text) throws ParseException{
		double value = 0.0d;
		NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
		Number num = format.parse(text);
		value = num.doubleValue();
		return value;
	}

	public static void logServerResponse(Context context, String msg){
		Logger.appendLog(context, LOG_DIR,
				LOG_FILE_NAME,
				" Server Response : " + msg);
	}
	
	public static void clearSale(Context context){
		MPOSDatabase.MPOSOpenHelper mSqliteHelper = 
				MPOSDatabase.MPOSOpenHelper.getInstance(context);
		SQLiteDatabase sqlite = mSqliteHelper.getWritableDatabase();
		sqlite.delete(OrderDetailTable.TABLE_ORDER, null, null);
		sqlite.delete(OrderSetTable.TABLE_ORDER_SET, null, null);
		sqlite.delete(OrderCommentTable.TABLE_ORDER_COMMENT, null, null);
		sqlite.delete(OrderTransactionTable.TABLE_ORDER_TRANS, null, null);
		sqlite.delete(PaymentDetailTable.TABLE_PAYMENT_DETAIL, null, null);
		sqlite.delete(SessionTable.TABLE_SESSION, null, null);
		sqlite.delete(SessionDetailTable.TABLE_SESSION_ENDDAY_DETAIL, null, null);
	}

	/**
	 * @param context
	 * @return android device id
	 */
	public static String getDeviceCode(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}
	
	/**
	 * @param context
	 * @return drawer baud rate
	 */
	public static String getWintecDrwBaudRate(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_DRW_BAUD_RATE, "BAUD_38400");
	}
	
	/**
	 * @param context
	 * @return drawer dev path
	 */
	public static String getWintecDrwDevPath(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_DRW_DEV_PATH, "/dev/ttySAC1");
	}
	
	/**
	 * @param context
	 * @return magnetic reader baud rate
	 */
	public static String getWintecMsrBaudRate(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_MSR_BAUD_RATE, "BAUD_38400");
	}
	
	/**
	 * @param context
	 * @return magnetic reader dev path
	 */
	public static String getWintecMsrDevPath(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_MSR_DEV_PATH, "/dev/ttySAC3");
	}
	
	/**
	 * @param context
	 * @return true if enable internal printer
	 */
	public static boolean isInternalPrinterSetting(Context context){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(SettingsActivity.KEY_PREF_PRINTER_INTERNAL, true);
	}
	
	/**
	 * @param context
	 * @return epson printer font
	 */
	public static String getEPSONPrinterFont(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_PRINTER_FONT_LIST, "");
	}
	
	/**
	 * @param context
	 * @return printer baud rate
	 */
	public static String getWintecPrinterBaudRate(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_PRINTER_BAUD_RATE, "BAUD_38400");
	}
	
	/**
	 * @param context
	 * @return printer dev path
	 */
	public static String getWintecPrinterDevPath(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_PRINTER_DEV_PATH, "/dev/ttySAC1");
	}
	
	/**
	 * @param context
	 * @return epson model name
	 */
	public static String getEPSONModelName(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_PRINTER_LIST, "");
	}
	
	/**
	 * @param context
	 * @return epson printer ip
	 */
	public static String getPrinterIp(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_PRINTER_IP, "");
	}
	
	/**
	 * @param context
	 * @return true if enable wintec customer display
	 */
	public static boolean isEnableWintecCustomerDisplay(Context context){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(SettingsActivity.KEY_PREF_ENABLE_DSP, false);
	}
	
	/**
	 * @param context
	 * @return dsp baud rate
	 */
	public static String getWintecDspBaudRate(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_DSP_BAUD_RATE, "BAUD_9600");
	}
	
	/**
	 * @param context
	 * @return dsp dev path
	 */
	public static String getWintecDspPath(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_DSP_DEV_PATH, "/dev/ttySAC3");
	}
	
	/**
	 * @param context
	 * @return second display ip
	 */
	public static String getSecondDisplayIp(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_SECOND_DISPLAY_IP, "");
	}
	
	/**
	 * @param context
	 * @return second display port
	 */
	public static int getSecondDisplayPort(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		int port = Integer.parseInt(context.getString(R.string.default_second_display_port));
		try {
			String prefPort = sharedPref.getString(SettingsActivity.KEY_PREF_SECOND_DISPLAY_PORT, "");
			if(!prefPort.equals("")){
				port = Integer.parseInt(prefPort);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return port;
	}
	
	/**
	 * @param context
	 * @return true if enable second display
	 */
	public static boolean isEnableSecondDisplay(Context context){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(SettingsActivity.KEY_PREF_ENABLE_SECOND_DISPLAY, false);
	}

	/**
	 * @param context
	 * @return menu image url
	 */
	public static String getImageUrl(Context context) {
		return getUrl(context) + "/" + SERVER_IMG_PATH;
	}

	/**
	 * @param context
	 * @return full webservice url
	 */
	public static String getFullUrl(Context context) {
		return getUrl(context) + "/" + WS_NAME;
	}

	/**
	 * @param context
	 * @return connection time out millisecond
	 */
	public static int getConnectionTimeOut(Context context){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String strTimeOut = sharedPref.getString(SettingsActivity.KEY_PREF_CONN_TIME_OUT_LIST, "30");
		int timeOut = Integer.parseInt(strTimeOut);
		return timeOut * 1000;
	}
	
	public static String getUrl(Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String url = sharedPref.getString(SettingsActivity.KEY_PREF_SERVER_URL,
				"");
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			// not found protocal
			url = "http://" + url;
			//e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * @param context
	 * @return true if enable show menu image
	 */
	public static boolean isShowMenuImage(Context context){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(SettingsActivity.KEY_PREF_SHOW_MENU_IMG, true);
	}
	
	public static LinearLayout.LayoutParams getLinHorParams(float weight){
		return new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
	}
	
	public static interface LoadSaleTransactionListener extends ProgressListener{
		void onPost(POSData_SaleTransaction saleTrans);
	}
}
