package com.synature.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

public class Logger {

	public static final String FILE_EXTENSION = ".txt";
	
	public static void appendLog(Context c, String logDir, String fileName, String mesg) {
		Date date = new Date();
		SimpleDateFormat timeFormat = 
				new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		SimpleDateFormat dateFormat = 
				new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		fileName += dateFormat.format(date) + FILE_EXTENSION;
		FileManager fileManager = new FileManager(c, logDir);
		String logFile = fileManager.getFile(fileName).getPath();
		
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.newLine();
			buf.append(timeFormat.format(date) + " " + mesg);
			buf.close();
		} catch (IOException e) {
			Log.d("LOGGER", e.getMessage());
		}
	}
}
