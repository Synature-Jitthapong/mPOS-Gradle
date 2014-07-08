package com.synature.mpos;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import com.epson.eposprint.EposException;

public class SettingsActivity extends PreferenceActivity {

	public static final String KEY_PREF_SERVER_URL = "server_url";
	public static final String KEY_PREF_CONN_TIME_OUT_LIST = "connection_time_out";
	public static final String KEY_PREF_PRINTER_IP = "printer_ip";
	public static final String KEY_PREF_PRINTER_LIST = "printer_list";
	public static final String KEY_PREF_PRINTER_FONT_LIST = "printer_font_list";
	public static final String KEY_PREF_PRINTER_INTERNAL = "printer_internal";
	public static final String KEY_PREF_PRINTER_DEV_PATH = "printer_wintec_dev_path";
	public static final String KEY_PREF_PRINTER_BAUD_RATE = "printer_wintec_baud_rate";
	public static final String KEY_PREF_MSR_DEV_PATH = "msr_wintec_dev_path";
	public static final String KEY_PREF_MSR_BAUD_RATE = "msr_wintec_baud_rate";
	public static final String KEY_PREF_DSP_DEV_PATH = "dsp_wintec_dev_path";
	public static final String KEY_PREF_DSP_BAUD_RATE = "dsp_wintec_baud_rate";
	public static final String KEY_PREF_DRW_DEV_PATH = "drw_wintec_dev_path";
	public static final String KEY_PREF_DRW_BAUD_RATE = "drw_wintec_baud_rate";
	public static final String KEY_PREF_SHOW_MENU_IMG = "show_menu_image";
	public static final String KEY_PREF_SECOND_DISPLAY_IP = "second_display_ip";
	public static final String KEY_PREF_SECOND_DISPLAY_PORT = "second_display_port";
	public static final String KEY_PREF_ENABLE_DSP = "enable_dsp";
	public static final String KEY_PREF_ENABLE_SECOND_DISPLAY = "enable_second_display";
	
	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        
		setupSimplePreferencesScreen();
	}

	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}
		
		addPreferencesFromResource(R.xml.pref_connection);
		addPreferencesFromResource(R.xml.pref_general);
		addPreferencesFromResource(R.xml.pref_printer);
		addPreferencesFromResource(R.xml.pref_drw);
		addPreferencesFromResource(R.xml.pref_magnetic_reader);
		addPreferencesFromResource(R.xml.pref_dsp);
		addPreferencesFromResource(R.xml.pref_second_display);
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_SERVER_URL));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_CONN_TIME_OUT_LIST));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_IP));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_LIST));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_DEV_PATH));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_BAUD_RATE));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_DRW_DEV_PATH));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_DRW_BAUD_RATE));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_DSP_DEV_PATH));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_DSP_BAUD_RATE));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_MSR_DEV_PATH));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_MSR_BAUD_RATE));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_FONT_LIST));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_SECOND_DISPLAY_IP));
		bindPreferenceSummaryToValue(findPreference(KEY_PREF_SECOND_DISPLAY_PORT));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			setResult(RESULT_OK);
			finish();
			return true;
		default :
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			} else {
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}
	
	public static class PrinterPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_printer);
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_IP));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_LIST));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_DEV_PATH));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_BAUD_RATE));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_PRINTER_FONT_LIST));
		}
	}
	
	public static class DrwPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_drw);
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_DRW_DEV_PATH));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_DRW_BAUD_RATE));
		}
	}
	
	public static class MsrPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_magnetic_reader);
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_MSR_DEV_PATH));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_MSR_BAUD_RATE));
		}
	}
	
	public static class ConnectionPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_connection);
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_SERVER_URL));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_CONN_TIME_OUT_LIST));
		}
	}
	
	public static class GeneralPreferenceFragment extends PreferenceFragment{

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);
		}
		
	}
	
	public static class DspPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_dsp);
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_DSP_DEV_PATH));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_DSP_BAUD_RATE));
		}
	}
	
	public static class SecondDisplayPreferenceFragment extends PreferenceFragment{

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_second_display);
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_SECOND_DISPLAY_IP));
			bindPreferenceSummaryToValue(findPreference(KEY_PREF_SECOND_DISPLAY_PORT));
		}
		
	}
	
	public void printTestClick(final View v){
		if(Utils.isInternalPrinterSetting(getApplicationContext())){
			WinTecTestPrint wt = new WinTecTestPrint(getApplicationContext());
			wt.prepareDataToPrint();
			wt.print();
		}else{
			EPSONTestPrint ep = new EPSONTestPrint(getApplicationContext());
			ep.prepareDataToPrint();
			ep.print();
		}
	}
	
	public static class WinTecTestPrint extends WintecPrinter{
		
		public WinTecTestPrint(Context context){
			super(context);
		}
		
		@Override
		public void prepareDataToPrint(int transactionId) {
		}

		@Override
		public void prepareDataToPrint() {
			mBuilder.append(mContext.getString(R.string.print_test_text));
		}
		
	}
	
	public static class EPSONTestPrint extends EPSONPrinter{

		public EPSONTestPrint(Context context) {
			super(context);
		}

		@Override
		public void onBatteryStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void onStatusChangeEvent(String arg0, int arg1) {
		}

		@Override
		public void prepareDataToPrint(int transactionId) {
		}

		@Override
		public void prepareDataToPrint() {
			String printText = mContext.getString(R.string.print_test_text).replaceAll("\\*", " ");
			try {
				mBuilder.addText(printText);
			} catch (EposException e) {
				switch(e.getErrorStatus()){
				case EposException.ERR_CONNECT:
					Utils.makeToask(mContext, e.getMessage());
					break;
				default :
					Utils.makeToask(mContext, mContext.getString(R.string.not_found_printer));
				}
			}
		}
		
	}
}
