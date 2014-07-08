package com.synature.mpos;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment 
	implements DatePickerDialog.OnDateSetListener {

	private OnSetDateListener listener;
	
	public DatePickerFragment(OnSetDateListener onSetDateListener){
		listener = onSetDateListener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {

		Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		Log.i("DatePicker", calendar.getTime().toString());
		
		listener.onSetDate(calendar.getTimeInMillis());
	}

	public static interface OnSetDateListener{
		void onSetDate(long date);
	}
}
