package com.matrix.yukun.matrix.image_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.image_module.bean.ImageBean;

import java.util.ArrayList;

/**
 * Created by yukun on 17-1-19.
 */
public class GlideViewAdapter extends BaseAdapter {
    ArrayList<String> strings;
    Context context;

    public GlideViewAdapter(Context context,ArrayList<String> strings) {
        this.strings = strings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        String mImageBean = strings.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gride_item, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mImageView.setImageBitmap(null);
        Glide.with(context).load(mImageBean)
                .into(viewHolder.mImageView);

        return convertView;
    }

    public static class ViewHolder{
        public ImageView mImageView;
    }
}
