package com.matrix.yukun.matrix.leshilive_module.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshilive_module.bean.OnEventNumber;
import com.matrix.yukun.matrix.selfview.NumberView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by yukun on 17-3-31.
 */
public class ViewAdapter extends PagerAdapter {
    Context context;
    ArrayList<Integer> integers;
    private GridView gridView;
    private GrideAdapter adapter;
    private NumberView numberView;
    private TextView textViewSure;
    private int secPos=0;

    public ViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.gride_items, null);
        gridView = (GridView) inflate.findViewById(R.id.grideviews);
        numberView = (NumberView) inflate.findViewById(R.id.numview);
        textViewSure = (TextView) inflate.findViewById(R.id.textview_sure);
        setInfo();
        adapter = new GrideAdapter(context, integers);
        gridView.setAdapter(adapter);
        setListener();
        container.addView(inflate);
        return inflate;
    }

    private void setInfo() {
        integers=AppConstants.getGift();
    }

    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPos(position);
                adapter.notifyDataSetChanged();
                secPos = position;
            }
        });
        textViewSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = numberView.getNumber();
                EventBus.getDefault().post(new OnEventNumber(secPos,number));
            }
        });
    }

    public void setnum(int num){
        numberView.setNumber(num);
    }
}
