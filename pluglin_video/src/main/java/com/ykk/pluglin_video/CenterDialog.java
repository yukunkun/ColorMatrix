package com.ykk.pluglin_video;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.ykk.pluglin_video.video.MainActivity;

import java.util.List;

/**
 * Created by yukun on 17-9-29.
 */

public class CenterDialog extends BaseCenterDialog {
    static List<String> mList;
    static CenterDialog mCenterDialog;
    private ListView mListView;
    static MainActivity mMainActivity;
    private int sector=0;
    private LVAdapter mLvAdapter;

    public static CenterDialog getInstance(List<String> mLists,MainActivity mainActivity){
        mList=mLists;
        mMainActivity=mainActivity;
        if(mCenterDialog==null){
            mCenterDialog=new CenterDialog();
        }
        return mCenterDialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mListView = (ListView) inflate.findViewById(R.id.lv_lujing);
        mLvAdapter = new LVAdapter();
        mListView.setAdapter(mLvAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMainActivity.setCameraColor(mList.get(position));
                sector=position;
                mLvAdapter.notifyDataSetChanged();
                getDialog().dismiss();
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.base_dialog;
    }

    class LVAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View inflate, ViewGroup parent) {
             inflate = LayoutInflater.from(getContext()).inflate(R.layout.lv_layout, null);
            final TextView textView= (TextView) inflate.findViewById(R.id.tv_lv_lujing);
            textView.setText(mList.get(position));
            if(position==sector) {
                textView.setTextColor(((mMainActivity)).getResources().getColor(R.color.colorAccent));
            }else {
                textView.setTextColor((mMainActivity).getResources().getColor(R.color.white));
            }

            return inflate;
        }
    }
}
