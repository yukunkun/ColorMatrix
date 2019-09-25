package com.matrix.yukun.matrix.tool_module.weather.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowStation;

/**
 * author: kun .
 * date:   On 2019/9/25
 */
public class RVPosizonAdapter extends BaseQuickAdapter<AirNowStation,BaseViewHolder> {

    Context mContext;
    public RVPosizonAdapter(Context context,int layoutResId, @Nullable List<AirNowStation> data) {
        super(layoutResId, data);
        mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AirNowStation item) {
        helper.setText(R.id.tv_local,item.getAir_sta());
        helper.setText(R.id.tv_co,"CO:"+item.getCo());
        helper.setText(R.id.tv_so2,"SO2:"+item.getSo2());
        helper.setText(R.id.tv_no2,"No2:"+item.getNo2());
        helper.setText(R.id.tv_o3,"O3:"+item.getO3());
        String qlty=item.getQlty();
        TextView view = helper.getView(R.id.tv_quality);
        if(qlty.equals("优")){
            view.setTextColor(mContext.getResources().getColor(R.color.color_44fc2c));
        }else if(qlty.equals("良")){
            view.setTextColor(mContext.getResources().getColor(R.color.color_2299ee));
        }else {
            view.setTextColor(mContext.getResources().getColor(R.color.color_fc2c5d));
        }
        view.setText("空气质量:"+qlty+"  污染物："+item.getMain());
        helper.setText(R.id.tv_pm,"pm10:"+item.getPm10()+"  pm2.5:"+item.getPm25());
    }
}
