package com.letv.recorder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class ListActivity extends Activity {
	private ExpandableListView pushList;
	private final String[] STREAM_NAME = new String[]{"美国输入","成都电信输入","成都联通输入"};
	private final String[][] STREAM_PATH = new String[][]{
			{"rtmp://65.255.32.107/live802","rtmp://65.255.32.108/live802"},
			{"rtmp://123.59.122.58/live/201607273000004us99","rtmp://115.182.200.88/live/201607273000004us99","rtmp://61.147.169.151/live/201607273000004us99","rtmp://183.60.152.25/live/201607273000004us99"},
			{"rtmp://123.59.122.58/live/201607273000004us99","rtmp://115.182.200.88/live/201607273000004us99","rtmp://153.3.50.208/live/201607273000004us99"}
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		pushList = (ExpandableListView) findViewById(R.id.expLv_push_stream);
		pushList.setChildIndicator(null);
		pushList.setGroupIndicator(null);
		pushList.setAdapter(new MyAdapter());
		int groupCount = pushList.getCount(); 
		for (int i=0; i<groupCount; i++){ 
			pushList.expandGroup(i);
		 }; 
		 pushList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,int arg3, long arg4) {
				Intent intent = new Intent(ListActivity.this,RecorderActivity.class);
				intent.putExtra("streamUrl", STREAM_PATH[arg2][arg3]);
				intent.putExtra("pushName", STREAM_NAME[arg2]);
				// 获取当前 横屏还是竖屏推流。并且把参数传递给推流界面
				intent.putExtra("isVertical", true);
				// 启动推流界面
				startActivity(intent);
				return true;
			}
		});
	}
	
	private class MyAdapter extends BaseExpandableListAdapter{

		@Override
		public Object getChild(int arg0, int arg1) {
			return STREAM_PATH[arg0][arg1];
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			return arg0;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View arg3,ViewGroup arg4) {
			TextView view;
			if(arg3 == null){
				view = new TextView(ListActivity.this);
				view.setBackgroundColor(0xffEEEEEE);
				view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(ListActivity.this, 60)));
				view.setGravity(Gravity.CENTER);
			}else{
				view = (TextView) arg3;
			}
			view.setText(STREAM_PATH[arg0][arg1]);
			
			return view;
		}

		@Override
		public int getChildrenCount(int arg0) {
			return STREAM_PATH[arg0].length;
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return STREAM_NAME[arg0];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return STREAM_NAME.length;
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,ViewGroup arg3) {
			TextView view;
			if(arg2 == null){
				view = new TextView(ListActivity.this);
				view.setBackgroundColor(0xffFFFFFF);
				view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(ListActivity.this, 30)));
				view.setGravity(Gravity.CENTER_VERTICAL);
			}else{
				view = (TextView) arg2;
			}
			view.setText(STREAM_NAME[arg0]);
			return view;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}
		public int dip2px(Context context, float dpValue)
		{
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}

	}
}
