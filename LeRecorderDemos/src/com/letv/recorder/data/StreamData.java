package com.letv.recorder.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class StreamData {
	private SharedPreferences sp;
	private final static String DEFULT = "StreamData_";
	private Editor edt;
	
	public StreamData(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	private String pushStream;
	private boolean isVertical;

	public String getPushStream() {
		pushStream = sp.getString(DEFULT +"pushStream", null);
		return pushStream;
	}
	public void setPushStream(String pushStream) {
		edt = sp.edit();
		this.pushStream = pushStream;
		edt.putString(DEFULT +"pushStream", pushStream);
		edt.commit();
	}
	public boolean isVertical() {
		isVertical = sp.getBoolean("isVertical", true);
		return isVertical;
	}
	public void setVertical(boolean isVertical) {
		edt = sp.edit();
		this.isVertical = isVertical;
		edt.putBoolean("isVertical", this.isVertical);
		edt.commit();
	}
}
