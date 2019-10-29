package com.matrix.yukun.matrix.tool_module.weather.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;

public class NavMapAdapter extends BaseAdapter {

    private List<Tip> data;
    private Context context;

    private int selectedPosition = 0;

    public NavMapAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<Tip> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_holder_nav, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindView(position);

        return convertView;
    }


    class ViewHolder {
        TextView textTitle;
        ImageView imageCheck;
        public ViewHolder(View view) {
            textTitle = (TextView) view.findViewById(R.id.text_title);
            imageCheck = (ImageView) view.findViewById(R.id.image_check);
        }

        public void bindView(int position) {
            if (position >= data.size())
                return;
            Tip tip = data.get(position);
            textTitle.setText(tip.getName());
        }
    }
}
