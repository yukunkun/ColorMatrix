package com.matrix.yukun.matrix.chat_module.fragment.emoji;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.video_module.BaseFragment;

/**
 * author: kun .
 * date:   On 2019/5/17
 */
@SuppressLint("ValidFragment")
public class EmojiFragment extends BaseFragment {

    private View     mChatContainer;
    private ChatBaseActivity mChatActivity;

    public EmojiFragment(View chatContainer, Activity chatActivity) {
        this.mChatContainer = chatContainer;
        this.mChatActivity = (ChatBaseActivity)chatActivity;
    }
    @Override
    public int getLayout() {
        return R.layout.fragment_emoji;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
