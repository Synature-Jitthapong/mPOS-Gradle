package com.synature.util;

import java.io.File;

import android.content.Context;

public class FileManager {
    protected File mSdcard;
    
    public FileManager(Context context, String dirName){
    	final String fileDir = File.separator + dirName;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			mSdcard = new File(
					android.os.Environment.getExternalStorageDirectory(),
					fileDir);
		else
			mSdcard = context.getCacheDir();
		if (!mSdcard.exists())
			mSdcard.mkdirs();
    }
    
    public File getHashFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(mSdcard, filename);
        return f;
 
    }
    
    public File[] getFiles(){
        File[] files = mSdcard.listFiles();
        return files;
    }
    
    public File getFile(String fileName){
        File f = new File(mSdcard, fileName);
        return f;
    }
 
    public void clear(){
        File[] files=mSdcard.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
 
}

