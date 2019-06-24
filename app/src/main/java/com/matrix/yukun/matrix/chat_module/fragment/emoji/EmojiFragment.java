package com.matrix.yukun.matrix.chat_module.fragment.emoji;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.play.MViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/5/17
 */
@SuppressLint("ValidFragment")
public class EmojiFragment extends BaseFragment {

    private View     mChatContainer;
    private ChatBaseActivity mChatActivity;
    private ViewPager mViewPager;
    private String[] mString=new String[]{"0","1","2"};
    private List<Fragment> mFragments=new ArrayList<>();
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
        mViewPager = inflate.findViewById(R.id.viewpager);
        for (int i = 0; i < 3; i++) {
            mFragments.add(EmojiPreFragment.Instance(mString[i]));
        }
        mViewPager.setAdapter(new MViewPagerAdapter(getChildFragmentManager(),mFragments,mString));
    }
}
