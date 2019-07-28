package com.matrix.yukun.matrix.video_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.btmovie_module.Constant;
import com.matrix.yukun.matrix.btmovie_module.SpecialActivity;
import com.matrix.yukun.matrix.dictionary_module.DictionaryActivity;
import com.matrix.yukun.matrix.phone_module.ContactActivity;
import com.matrix.yukun.matrix.util.AdvUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.common.Constanct;
import com.matrix.yukun.matrix.video_module.entity.SortModel;
import com.matrix.yukun.matrix.video_module.play.HistoryTodayActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yukun on 17-12-19.
 */

public class LVToolAdapter extends BaseAdapter implements UnifiedBannerADListener {
    private Context mContext;
    private List<String> mListName;
    private List<Integer> mListImage = new ArrayList<>();
    private Map<String, Class> mClassMap = new HashMap<>();

    public LVToolAdapter(Context context) {
        mContext = context;
        mListName = Arrays.asList(context.getResources().getStringArray(R.array.tool_life));

        mListImage.add(R.mipmap.icon_tool_history);
        mListImage.add(R.mipmap.icon_tool_zidian);
        mListImage.add(R.mipmap.icon_tool_movie);
        mListImage.add(R.mipmap.icon_tool_phone);

        mClassMap.put("历史今日", HistoryTodayActivity.class);
        mClassMap.put("新华字典", DictionaryActivity.class);
        mClassMap.put("BT电影", SpecialActivity.class);
        mClassMap.put("电话查询", ContactActivity.class);

    }

    @Override
    public int getCount() {
        return mListName.size();
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
        view = LayoutInflater.from(mContext).inflate(R.layout.tool_list_item_layout, null);
        ImageView imageView = view.findViewById(R.id.iv_image);
        RelativeLayout layout = view.findViewById(R.id.rl_adv);
        final TextView textView = view.findViewById(R.id.tv_tool_name);
        if (!TextUtils.isEmpty(mListName.get(position))) {
            Glide.with(mContext).load(mListImage.get(position-1)).into(imageView);
            textView.setText(mListName.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textView.getText().toString();
                    Class aClass = mClassMap.get(text);
                    if (aClass != null) {
                        Intent intent = new Intent(mContext, aClass);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else {
                        ToastUtils.showToast("developing...");
                    }
                }
            });
        } else {
            //设置广告
            UnifiedBannerView banner = AdvUtil.getBanner((Activity) mContext, layout,  Constant.APPID, Constant.BANNER_ADID,this);
            banner.loadAD();
        }
        return view;
    }

    @Override
    public void onNoAD(AdError adError) {
        LogUtil.i("===========",adError.getErrorMsg());
    }

    @Override
    public void onADReceive() {

    }

    @Override
    public void onADExposure() {

    }

    @Override
    public void onADClosed() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADLeftApplication() {

    }

    @Override
    public void onADOpenOverlay() {

    }

    @Override
    public void onADCloseOverlay() {

    }
}
