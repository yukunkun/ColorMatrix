package com.matrix.yukun.matrix.chat_module.fragment.emoji;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.main_module.activity.MViewPagerAdapter;

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
    private TextView mTvTag_1;
    private TextView mTvTag_2;
    private TextView mTvTag_3;
    private List<View> mViews=new ArrayList<>();
    private LinearLayout mLlTags;

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
        mLlTags = inflate.findViewById(R.id.ll_tags);
        mTvTag_1 = inflate.findViewById(R.id.tv_tag_1);
        mTvTag_2 = inflate.findViewById(R.id.tv_tag_2);
        mTvTag_3 = inflate.findViewById(R.id.tv_tag_3);
        mViews.add(mTvTag_1);mViews.add(mTvTag_2);mViews.add(mTvTag_3);
        updateTagView(0);
        for (int i = 0; i < 3; i++) {
            mFragments.add(EmojiPreFragment.Instance(mString[i]));
        }
        mViewPager.setAdapter(new MViewPagerAdapter(getChildFragmentManager(),mFragments,mString));
    }

    public void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position<3){
                    updateTagView(position);
                }else { //TODO

                }
            }
        });
    }

    private void updateTagView(int i){
        for (int j = 0; j < mViews.size(); j++) {
            if(i==j){
                mViews.get(j).setBackground(getContext().getResources().getDrawable(R.drawable.ic_page_indicator));
            }else {
                mViews.get(j).setBackground(getContext().getResources().getDrawable(R.drawable.ic_page_indicatored));
            }
        }
    }
}
