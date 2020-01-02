package com.matrix.yukun.matrix.leancloud_module.adapter.chatholder;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

/**
 * author: kun .
 * date:   On 2019/12/26
 */
public abstract class BaseHolder {
    public Context context;
    public LeanChatMessage item;
    public LeanChatAdapter leanChatAdapter;
    public LinearLayout mRoot;
    public FrameLayout mLayout;
    private TextView mTvTime;
    private ImageView mIvOtherAvator;
    private ImageView mIvMyAvator;
    private LinearLayout mLinearLayout;

    public BaseHolder(Context context, LeanChatMessage item, LeanChatAdapter leanChatAdapter, LinearLayout view) {
        this.context = context;
        this.item = item;
        this.leanChatAdapter = leanChatAdapter;
        this.mRoot = view;
        initView();
        initData();
        initViewData();
        initListener();
    }

    private void initData() {
        //不是自己发送的消息
        if (item.isReceived()) {
            setGravity(mLayout, Gravity.LEFT| Gravity.CENTER_VERTICAL);
            GlideUtil.loadCircleImage(item.getMsgFromAvator(),mIvOtherAvator);
            mIvMyAvator.setVisibility(View.GONE);
        } else {
            setGravity(mLayout, Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            GlideUtil.loadCircleImage(item.getMsgFromAvator(),mIvMyAvator);
            mIvOtherAvator.setVisibility(View.GONE);
        }
        mTvTime.setText(DataUtils.getDataTime(item.getTimeStamp()));
        mIvMyAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailActivity.start(context,item.getMsgFromAvator(),false);

            }
        });

        mIvOtherAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailActivity.start(context,item.getMsgFromAvator(),false);

            }
        });
    }

    private void initView() {
        mLayout = mRoot.findViewById(R.id.fr_layout);
        mTvTime = mRoot.findViewById(R.id.left_time);
        mIvOtherAvator = mRoot.findViewById(R.id.iv_left_head);
        mIvMyAvator = mRoot.findViewById(R.id.iv_right_head);
        mLinearLayout = mRoot.findViewById(R.id.ll_content);
    }

    public void setGravity(View view, int gravity) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.gravity=gravity;
    }

    public void setChildGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity=gravity;
    }

    protected abstract void initListener();

    protected abstract void initViewData();
}
