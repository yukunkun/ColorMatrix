package com.matrix.yukun.matrix.leshi_module.present;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 17-1-19.
 */
public class PlayAdapter extends BaseAdapter {
    List<ListBean> videoBeans;
    Context context;
    int setPos;

    public PlayAdapter(Context context, List<ListBean> videoBean) {
        this.videoBeans = videoBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSectPos(int i) {
        this.setPos=i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        ListBean videoBean = videoBeans.get(position);
//        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gride_item, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.play_name);
            viewHolder.textViewTime = (TextView) convertView.findViewById(R.id.play_time);
            viewHolder.layout= (RelativeLayout) convertView.findViewById(R.id.real_play);
            viewHolder.layout.setVisibility(View.VISIBLE);
            viewHolder.textViewName.setVisibility(View.VISIBLE);
            viewHolder.textViewTime.setVisibility(View.VISIBLE);
        convertView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
        viewHolder.textViewName.setText(videoBean.getVideo_name());
        long video_duration = videoBean.getVideo_duration();
        int min=(int)video_duration/60;
        int sec=(int) video_duration%60;
        String tag = videoBean.getTag();
        viewHolder.textViewTime.setText(tag+"/"+min+"`"+sec+"``");

        if(videoBean.getInit_pic()!=null&&videoBean.getInit_pic().length()>0){
            Glide.with(context).load(videoBean.getInit_pic())
                    .into(viewHolder.mImageView);
        }else {
            Glide.with(context).load(videoBean.getImg())
                    .into(viewHolder.mImageView);
        }
        if(position==setPos){
//            viewHolder.layout.setVisibility(View.VISIBLE);
        }else {
            viewHolder.layout.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class ViewHolder{
        public ImageView mImageView;
        public TextView textViewName, textViewTime;
        public RelativeLayout layout;
    }
}
