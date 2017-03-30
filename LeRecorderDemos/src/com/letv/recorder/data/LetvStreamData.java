package com.letv.recorder.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class LetvStreamData {
	private SharedPreferences sp;
	private final static String DEFULT = "LetvStreamData_";
	private Editor edt;
	
	public LetvStreamData(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	private String userID;
	private String appKey;
	private String streamId;
	private boolean isVertical;
	
	public String getUserID() {
		userID = sp.getString(DEFULT+"userID", null);
		return userID;
	}
	public void setUserID(String userID) {
		edt = sp.edit();
		this.userID = userID;
		edt.putString(DEFULT+"userID", userID);
		edt.commit();
	}
	public String getAppKey() {
		appKey = sp.getString(DEFULT +"appKey", null);
		return appKey;
	}
	public void setAppKey(String appKey) {
		edt = sp.edit();
		this.appKey = appKey;
		edt.putString(DEFULT +"appKey", appKey);
		edt.commit();
	}
	public String getStreamId() {
		streamId = sp.getString(DEFULT+"streamId", null);
		return streamId;
	}
	public void setStreamId(String streamId) {
		edt = sp.edit();
		this.streamId = streamId;
		edt.putString(DEFULT+"streamId", streamId);
		edt.commit();
	}
	public boolean isVertical() {
		isVertical = sp.getBoolean("isVertical", true);
		return isVertical;
	}
	public void setOrientation(boolean isVertical) {
		edt = sp.edit();
		this.isVertical = isVertical;
		edt.putBoolean("isOrientation", this.isVertical);
		edt.commit();
	}
	
	
}
