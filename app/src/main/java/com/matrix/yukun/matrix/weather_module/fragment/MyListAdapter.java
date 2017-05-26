package com.matrix.yukun.matrix.weather_module.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;

/**
 * Created by yukun on 17-3-8.
 */
public class MyListAdapter extends BaseAdapter {
    Context context;
    WeaLifePoint.HeWeather5Bean.SuggestionBean suggestionBean;

    public MyListAdapter(Context context, WeaLifePoint.HeWeather5Bean.SuggestionBean suggestionBean) {
        this.context = context;
        this.suggestionBean = suggestionBean;
        Log.i("--wea",suggestionBean.toString());
    }

    @Override
    public int getCount() {
        return 1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.list_item,null);
        ((TextView)convertView.findViewById(R.id.conf_air_content)).setText(suggestionBean.getComf().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_shushi_dengji)).setText(suggestionBean.getComf().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_shushi_con)).setText(suggestionBean.getComf().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_xiche_dengji)).setText(suggestionBean.getCw().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_xiche_con)).setText(suggestionBean.getCw().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_jiayi_dengji)).setText(suggestionBean.getDrsg().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_jiayi_con)).setText(suggestionBean.getDrsg().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_ganmao_dengji)).setText(suggestionBean.getFlu().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_ganmao_con)).setText(suggestionBean.getFlu().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_chuxing_dengji)).setText(suggestionBean.getSport().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_chuxing_con)).setText(suggestionBean.getSport().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_lvyou_dengji)).setText(suggestionBean.getTrav().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_lvyou_con)).setText(suggestionBean.getTrav().getTxt());
        ((TextView)convertView.findViewById(R.id.conf_ziwaixian_dengji)).setText(suggestionBean.getUv().getBrf());
        ((TextView)convertView.findViewById(R.id.conf_ziwaixian_con)).setText(suggestionBean.getUv().getTxt());
        return convertView;
    }
}
