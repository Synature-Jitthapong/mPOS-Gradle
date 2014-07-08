package com.synature.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyMediaPlayer implements OnCompletionListener, OnPreparedListener, 
		OnVideoSizeChangedListener, SurfaceHolder.Callback{
	
	private static final String TAG = MyMediaPlayer.class.getSimpleName();
	private MediaManager mediaManager;
	private String mediaPath;
	private ArrayList<HashMap<String, String>> playLst;
	
	private int mVideoWidth;
	private int mVideoHeight;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private int currMediaIndex = -1;
	
	private Context context;
	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	private MediaPlayer mMediaPlayer;
	
	private MediaPlayerStateListener listener;
	
	public MyMediaPlayer(Context c, SurfaceView surfaceView, String mPath, MediaPlayerStateListener state){
		context = c;
		surface = surfaceView;
		surfaceHolder = surface.getHolder();
		surfaceHolder.addCallback(this);
		mediaPath = mPath;
		listener = state;
		
		mMediaPlayer = new MediaPlayer();
	}

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mVideoWidth = width;
        mVideoHeight = height;
        mIsVideoSizeKnown = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
        	startVideoPlayback();
        }
    }

    public void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		
//		float boxWidth = surface.getWidth();
//		float boxHeight = surface.getHeight();
//		float videoWidth = mVideoWidth;
//		float videoHeight = mVideoHeight;
//		
//		float widthRatio = boxWidth / videoWidth;
//		float heightRatio = boxHeight / videoHeight;
//		float aspectRatio = videoWidth / videoHeight;
//
//		if (widthRatio > heightRatio)
//			mVideoWidth = (int) (boxHeight * aspectRatio);
//		else
//			mVideoHeight = (int) (boxWidth / aspectRatio);

		surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
    }

	@Override
	public void onCompletion(MediaPlayer mp) {
		nextTrack();
	}
	
	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}
	 
	private void nextTrack(){
		try {
			if(currMediaIndex < (playLst.size() - 1)){
				currMediaIndex ++;
				playMedia();
			}else{
				currMediaIndex = 0;
				readMedia();
				playMedia();
			}
		} catch (Exception e) {
			listener.onError(e);
			e.printStackTrace();
		}
	}
	
	public void next(){
		nextTrack();
	}
	
	public void back(){
		if(currMediaIndex-- < 0);
			currMediaIndex = 0;
		playMedia();
	}
	
	public void resume(){
		mMediaPlayer.start();
	}
	
	public void pause(){
		mMediaPlayer.pause();
	}
	
	private void playMedia(){
		doCleanUp();
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(playLst.get(currMediaIndex).get(MediaManager.FILE_PATH));
			mMediaPlayer.setDisplay(surfaceHolder);
			mMediaPlayer.prepare();
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			
			listener.onPlayedFileName(playLst.get(currMediaIndex).get(MediaManager.FILE_TITLE));
		} catch (IllegalArgumentException e) {
			listener.onError(e);
			e.printStackTrace();
		} catch (SecurityException e) {
			listener.onError(e);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			listener.onError(e);
			e.printStackTrace();
		} catch (IOException e) {
			listener.onError(e);
			e.printStackTrace();
		}
	}
	
	private void readMedia(){
		mediaManager = new MediaManager(context, mediaPath);
		playLst = mediaManager.getVideoPlayList();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void startPlayMedia(){
		readMedia();
		try {
			if(playLst.size() > 0){
				currMediaIndex = 0;
				playMedia();
			}
		} catch (Exception e) {
			listener.onError(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		startPlayMedia();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<HashMap<String, String>> getPlayLst() {
		return playLst;
	}

	public static interface MediaPlayerStateListener{
		public void onPlayedFileName(String fileName);
		public void onError(Exception e);
	}
}
