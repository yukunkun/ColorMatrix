package com.matrix.yukun.matrix.wallpaper_module;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 17-5-17.
 */
public class PagerAdapters extends PagerAdapter {
    List<Integer> mList;
    Context mContext;
    List<View> mScaleViews = new ArrayList<>();

    public PagerAdapters(Context mContext,List<Integer> list) {
        this.mList = list;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return mList.size()*100;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.wall_viewpager_item, null);
        ImageView imageView= (ImageView) inflate.findViewById(R.id.image_show);
        Glide.with(mContext).load(mList.get(position%(mList.size()))).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mScaleViews.add(inflate);
        container.addView(inflate);
        return inflate;
    }
}