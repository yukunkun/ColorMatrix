package com.ykk.pluglin_video.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ykk.pluglin_video.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by yukun on 17-10-9.
 */

public class LVAdapter extends BaseAdapter {
    Context mContext;
    List<String> mList;

    public LVAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(mContext).inflate(R.layout.video_item, null);
        ImageView imageViewCover= (ImageView) convertView.findViewById(R.id.iv_cover);
        TextView mTViewName= (TextView) convertView.findViewById(R.id.tv_filename);
        TextView mTViewSize= (TextView) convertView.findViewById(R.id.tv_filesize);
        Glide.with(mContext).load(mList.get(position)).into(imageViewCover);
        String path = mList.get(position);
        String[] split = path.split("/");
        //name
        mTViewName.setText(split[split.length-1]);
        float f = (float) (new File((mList.get(position))).length() / 1024) / 1024;
        DecimalFormat fnum  =   new  DecimalFormat("##0.00");
        mTViewSize.setText(fnum.format(f)+"M");
        return convertView;
    }
}
