package com.synature.mpos;

public interface ProgressListener {
	void onPre();
	void onPost();
	void onError(String msg);
}
