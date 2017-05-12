package com.matrix.yukun.matrix.image_module.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.image_module.bean.ImageBean;

import java.util.List;

/**
 * Created by yukun on 17-1-19.
 */
public class GroupAdapter extends BaseAdapter{
    private List<ImageBean> list;
    private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
    private GridView mGridView;
    protected LayoutInflater mInflater;
    private Context context;


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public GroupAdapter(Context context, List<ImageBean> list, GridView mGridView){
        this.list = list;
        this.context=context;
        this.mGridView = mGridView;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        ImageBean mImageBean = list.get(position);
        String path = mImageBean.getTopImagePath();
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_group_item, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.group_image);
            viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.group_title);
            viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.group_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
        viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean.getImageCounts())+"张");

        if(mImageBean.getTopImagePath()!=null){
            viewHolder.mImageView.setImageBitmap(null);
            Glide.with(context).load(path).into(viewHolder.mImageView);
        }else{
//            viewHolder.mImageView.setImageResource(R.mipmap.layer_3);
        }
        return convertView;
    }

    public static class ViewHolder{
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewCounts;
    }
}
