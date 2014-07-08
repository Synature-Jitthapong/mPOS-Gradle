package com.synature.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class MediaManager extends FileManager{
	public static final String MP3_EXTENSION = ".mp3";
	public static final String M4A_EXTENSION = ".m4a";
	public static final String AVI_EXTENSION = ".avi";
	public static final String WMV_EXTENSION = ".wmv";
	public static final String MPEG_EXTENSION = ".mpeg";
	public static final String MP4_EXTENSION = ".mp4";
	public static final String WAV_EXTENSION = ".wav";
	public static final String OGG_EXTENSION = ".ogg";
	public static final String FILE_TITLE = "title";
	public static final String FILE_PATH = "path";

	private ArrayList<HashMap<String, String>> mVdoList = 
			new ArrayList<HashMap<String, String>>();

	public MediaManager(Context context, String folderName) {
		super(context, folderName);
	}

	public ArrayList<HashMap<String, String>> getVideoPlayList() {
		File[] files = mSdcard.listFiles(new VideoFileExtensionFilter());
		if (files.length > 0) {
			for (File f : files) {
				HashMap<String, String> vdo = new HashMap<String, String>();
				vdo.put(FILE_TITLE, f.getName());
				vdo.put(FILE_PATH, f.getPath());

				mVdoList.add(vdo);
			}
		}
		return mVdoList;
	}

	public File getSdCard(){
		return mSdcard;
	}
	
	public static class WAVExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(WAV_EXTENSION));
		}
	}
	
	public static class MP3ExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(MP3_EXTENSION));
		}
	}
	
	public static class VideoFileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(MP4_EXTENSION) || name.endsWith(AVI_EXTENSION) || 
					name.endsWith(MPEG_EXTENSION) || name.endsWith(WMV_EXTENSION));
		}
	}
}
