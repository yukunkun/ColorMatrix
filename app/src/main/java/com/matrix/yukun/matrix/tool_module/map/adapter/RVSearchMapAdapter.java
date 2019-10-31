package com.matrix.yukun.matrix.tool_module.map.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/10/25
 */
public class RVSearchMapAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {

    OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public RVSearchMapAdapter(int layoutResId, @Nullable List<Tip> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tip item) {
        helper.setText(R.id.tv_address,item.getName());
        helper.setText(R.id.tv_detail,item.getDistrict()+" "+item.getAddress());
        View view=helper.getView(R.id.rl_contain);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(helper.getAdapterPosition(),item);
            }
        });

    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position,Tip tip);
    };
}
