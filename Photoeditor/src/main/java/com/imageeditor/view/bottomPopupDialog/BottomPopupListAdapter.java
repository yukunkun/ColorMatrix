package com.imageeditor.view.bottomPopupDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.miracle.view.imageeditor.R;

import java.util.List;

/**
 * @author PengZhenjin
 * @date 2016/4/20 15:56
 */
public class BottomPopupListAdapter extends BaseAdapter {

    private Context       mContext;
    private List<String>  mItemData;
    private List<Integer> mRedPositions;
    private List<Integer> mPutTheAshPositions;

    public BottomPopupListAdapter(Context context, List<String> itemData) {
        this.mContext = context;
        this.mItemData = itemData;
    }

    public void setRedPositions(List<Integer> mRedPositions) {
        this.mRedPositions = mRedPositions;
    }

    public void setPutTheAshPositions(List<Integer> mPutTheAshPositions) {
        this.mPutTheAshPositions = mPutTheAshPositions;
    }

    /**
     * 设置数据
     *
     * @param itemData
     */
    public void setData(List<String> itemData) {
        this.mItemData = itemData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItemData == null ? 0 : mItemData.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemData == null ? null : mItemData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItemData == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bottom_popup_dialog_item, parent, false);
            holder = new ListHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(holder);
        }
        else {
            holder = (ListHolder) convertView.getTag();
        }
        holder.textView.setText(mItemData.get(position));
        if (mRedPositions != null && mRedPositions.contains(position)) {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.tips_text));
        }
        else if (mPutTheAshPositions != null && mPutTheAshPositions.contains(position)) {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.C3));
        }
        else {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        }

        return convertView;
    }

    class ListHolder {
        TextView textView;
    }
}
