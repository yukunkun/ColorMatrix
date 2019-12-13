package com.matrix.yukun.matrix.chat_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.BaseActivity;

import butterknife.OnClick;

public class ChatMemberActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent=new Intent(context,ChatMemberActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_chat_member;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.iv_backs, R.id.rl_chat_mem, R.id.rl_chat_womem})
    public void onViewClicked(View view) {
        if(view.getId()== R.id.iv_backs){
            finish();
        }if(view.getId()== R.id.rl_chat_mem){
            ChatBaseActivity.start(this,ChatBaseActivity.TYPE_MEM);
        }if(view.getId()== R.id.rl_chat_womem){
            ChatBaseActivity.start(this,ChatBaseActivity.TYPE_WOMEM);
        }
    }
}
