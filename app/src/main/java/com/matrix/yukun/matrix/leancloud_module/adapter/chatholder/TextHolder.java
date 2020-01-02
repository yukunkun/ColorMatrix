package com.matrix.yukun.matrix.leancloud_module.adapter.chatholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;

/**
 * author: kun .
 * date:   On 2019/12/26
 */
public class TextHolder extends BaseHolder {

    private TextView mTvTxt;

    public TextHolder(Context context, LeanChatMessage item, LeanChatAdapter leanChatAdapter, LinearLayout view) {
        super(context, item, leanChatAdapter,view);
    }

    @Override
    protected void initViewData() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.lean_chat_txt, mLayout);
        mTvTxt = inflate.findViewById(R.id.tv_txt);
        if(item.isReceived()){
            setGravity(mTvTxt, Gravity.LEFT| Gravity.CENTER_VERTICAL);
            mTvTxt.setBackgroundResource(R.drawable.balloon_l_pressed);
        }else {
            setGravity(mTvTxt, Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            mTvTxt.setBackgroundResource(R.drawable.balloon_r_pressed);
        }
        if(item.getContent().equals("抖一抖")){
            Drawable nav_up=context.getResources().getDrawable(R.mipmap.icon_chat_shake);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            mTvTxt.setCompoundDrawables(nav_up, null, null, null);
            mTvTxt.setText(" "+item.getContent());
        }else {
            mTvTxt.setText(item.getContent());
        }
    }

    @Override
    protected void initListener() {

    }

}
