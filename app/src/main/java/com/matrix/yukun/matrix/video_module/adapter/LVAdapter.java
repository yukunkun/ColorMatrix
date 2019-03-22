package com.matrix.yukun.matrix.video_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matrix.yukun.matrix.video_module.entity.SortModel;
import com.matrix.yukun.matrix.R;
import java.util.List;

/**
 * Created by yukun on 17-12-19.
 */

public class LVAdapter extends BaseAdapter {
    private List<SortModel> list = null;
    private Context mContext;

    public LVAdapter(List<SortModel> list, Context context) {
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
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.hidtory_layout_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv);
            viewHolder.tvStory = (TextView) view.findViewById(R.id.tv_story);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(list.get(position).getYear()+"年"+list.get(position).getMonth()+"月"+list.get(position).getDay()+"日");
        viewHolder.tvStory.setText(this.list.get(position).getName());
        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        TextView tvStory;
    }

    /**
     * 选中的位置
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 位置是否有,基本就能实现了，
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
