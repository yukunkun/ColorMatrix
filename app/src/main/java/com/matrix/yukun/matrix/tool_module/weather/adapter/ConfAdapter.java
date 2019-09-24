package com.matrix.yukun.matrix.tool_module.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;

/**
 * author: kun .
 * date:   On 2019/9/24
 */
public class ConfAdapter extends BaseAdapter {
    Context context;
    List<LifestyleBase> suggestionBean;

    public ConfAdapter(Context context, List<LifestyleBase> suggestionBean) {
        this.context = context;
        this.suggestionBean = suggestionBean;
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
        convertView= LayoutInflater.from(context).inflate(R.layout.list_item,null);
        if(suggestionBean.size()>0){
            ((TextView)convertView.findViewById(R.id.conf_air_content)).setText(suggestionBean.get(0).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_shushi_dengji)).setText(suggestionBean.get(0).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_shushi_con)).setText(suggestionBean.get(0).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_xiche_con)).setText(suggestionBean.get(1).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_xiche_dengji)).setText(suggestionBean.get(1).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_jiayi_dengji)).setText(suggestionBean.get(2).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_jiayi_con)).setText(suggestionBean.get(2).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_ganmao_dengji)).setText(suggestionBean.get(3).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_ganmao_con)).setText(suggestionBean.get(3).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_chuxing_dengji)).setText(suggestionBean.get(4).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_chuxing_con)).setText(suggestionBean.get(4).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_lvyou_dengji)).setText(suggestionBean.get(5).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_lvyou_con)).setText(suggestionBean.get(5).getTxt());
            ((TextView)convertView.findViewById(R.id.conf_ziwaixian_dengji)).setText(suggestionBean.get(6).getBrf());
            ((TextView)convertView.findViewById(R.id.conf_ziwaixian_con)).setText(suggestionBean.get(6).getTxt());
        }
        return convertView;
    }
}
