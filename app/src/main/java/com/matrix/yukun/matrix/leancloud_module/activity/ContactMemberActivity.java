package com.matrix.yukun.matrix.leancloud_module.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactMemberActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;

    public static void start(Context context) {
        Intent intent = new Intent(context, ContactMemberActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_contact_member;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDate() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                break;
        }
    }
}
