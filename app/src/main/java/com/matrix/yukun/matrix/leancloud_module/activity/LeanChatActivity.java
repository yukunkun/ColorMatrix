package com.matrix.yukun.matrix.leancloud_module.activity;

import android.content.Context;
import android.content.Intent;

import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;

public class LeanChatActivity extends LeanBaseActivity {

    public static void start(Context context, ContactInfo contactInfo){
        Intent intent=new Intent(context,LeanChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("data",contactInfo);
        context.startActivity(intent);
    }
}
