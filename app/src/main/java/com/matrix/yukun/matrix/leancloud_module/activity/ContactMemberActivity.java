package com.matrix.yukun.matrix.leancloud_module.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;

public class ContactMemberActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent=new Intent(context,ContactMemberActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.left_in,R.anim.right_out);
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
}
