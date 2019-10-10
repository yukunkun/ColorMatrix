package com.matrix.yukun.matrix.tool_module.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.weather.activity.CityBean;
import com.matrix.yukun.matrix.tool_module.weather.activity.CitySortBean;

import java.util.List;

/**
 * Created by yukun on 17-12-19.
 */

public class LVCitySearchAdapter extends BaseAdapter {
    private List<CityBean.CitiesBean.CountiesBean> list = null;
    private Context mContext;

    public LVCitySearchAdapter(List<CityBean.CitiesBean.CountiesBean> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final CityBean.CitiesBean.CountiesBean mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.city_layout_item, null);
            viewHolder.tvCity = (TextView) view.findViewById(R.id.tv_city);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvCity.setText(mContent.getAreaName());
        return view;
    }

    final static class ViewHolder {
        TextView tvCity;
    }

}
