package com.synature.mpos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.synature.util.FileManager;

@SuppressLint("SimpleDateFormat")
public class JSONSaleLogFile {
	public static final String JSON_EXTENSION = ".json";
	
	public static void appendSale(Context c, String json) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = dateFormat.format(calendar.getTime()) + JSON_EXTENSION;
		FileManager fileManager = new FileManager(c, Utils.LOG_SALE_DIR);
		String logFile = fileManager.getFile(fileName).getPath();
		try {
			BufferedWriter buf = new BufferedWriter(
					new FileWriter(logFile, true));
			buf.newLine();
			buf.append(json);
			buf.close();
		} catch (IOException e) {
			Log.d("LOGGER", e.getMessage());
		}
	}
	
	public static void appendEnddaySale(Context c, String sessDate, String json) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(sessDate));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = dateFormat.format(calendar.getTime()) + JSON_EXTENSION;
		FileManager fileManager = new FileManager(c, Utils.LOG_ENDDAY_DIR);
		String logFile = fileManager.getFile(fileName).getPath();
		try {
			BufferedWriter buf = new BufferedWriter(
					new FileWriter(logFile, false));
			buf.write(json);
			buf.close();
		} catch (IOException e) {
			Log.d("LOGGER", e.getMessage());
		}
	}
}
