package com.letv.recorder.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class CreateStreamData {
	private SharedPreferences sp;
	private final static String DEFULT = "NoStreamData_";
	private Editor edt;
	
	public CreateStreamData(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	private String domainName;
	private String appKey;
	private String streamId;
	private String playUrl;
	private boolean isVertical;
	
	public String getDomainName() {
		domainName = sp.getString(DEFULT + "domainName" , null);
		return domainName;
	}
	public void setDomainName(String domainName) {
		edt = sp.edit();
		this.domainName = domainName;
		edt.putString(DEFULT + "domainName", domainName);
		edt.commit();
		
	}
	public String getAppKey() {
		appKey = sp.getString(DEFULT+"appKey", null);
		return appKey;
	}
	public void setAppKey(String appKey) {
		edt = sp.edit();
		this.appKey = appKey;
		edt.putString(DEFULT+"appKey", appKey);
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
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
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
