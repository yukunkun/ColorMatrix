package com.matrix.yukun.matrix.mine_module.activity;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.fragment.BaseCardFragment;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.mine_module.fragment.ShareCard1Fragment;
import com.matrix.yukun.matrix.mine_module.fragment.ShareCard2Fragment;
import com.matrix.yukun.matrix.mine_module.fragment.ShareCard3Fragment;
import com.matrix.yukun.matrix.mine_module.fragment.ShareCard4Fragment;
import com.matrix.yukun.matrix.mine_module.fragment.ShareCard5Fragment;
import com.matrix.yukun.matrix.mine_module.fragment.ShareCard6Fragment;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvBack;
    private TextView mTvSave;
    private TextView mTvShare;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private ShareViewPagerAdapter mShareViewPagerAdapter;
    private int pagePos;
    private RadioGroup mRadioGroup;
    private int mWidthPixels;
    private float mX;
    private float mY;
    private LinearLayout mLinearLayout;

    @Override
    public int getLayout() {
        return R.layout.activity_setting2;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mTvSave = findViewById(R.id.tv_save);
        mTvShare = findViewById(R.id.tv_share);
        mViewPager = findViewById(R.id.vp_card);
        mRadioGroup = findViewById(R.id.rg_layout);
        mLinearLayout = findViewById(R.id.ll);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidthPixels = displayMetrics.widthPixels;
    }

    @Override
    public void initListener() {
        ((RadioButton)mRadioGroup.getChildAt(0)).setChecked(true);
        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mTvShare.setOnClickListener(this);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                pagePos=position;
                ((RadioButton)mRadioGroup.getChildAt(position)).setChecked(true);
                super.onPageSelected(position);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        startAnimation();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX()-mX>0&& mX<=mWidthPixels/6
                        && Math.abs(event.getX() - mX) > Math.abs(event.getY() - mY)
                        && Math.abs(event.getX() - mX) > mWidthPixels/2){
                    mLinearLayout.scrollTo(Integer.valueOf((int) -(event.getX() - mX)), 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() < mWidthPixels/2) {
                    // 如果没有拉取过半，则回退
                    mLinearLayout.scrollTo(0, 0);
                } else {
                    if (event.getX() - mX > 0
                            && mX <= mWidthPixels/2
                            && Math.abs(event.getX() - mX) > Math.abs(event.getY() - mY)
                            && Math.abs(event.getX() - mX) > mWidthPixels/6) {
                        finish();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startAnimation(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            final TransitionSet transitionSet = new TransitionSet();
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.rl);
            transitionSet.addTransition(slide);
            getWindow().setEnterTransition(transitionSet);
        }
    }

    @Override
    public void initDate() {
        mFragmentList.add(ShareCard1Fragment.getInstance());
        mFragmentList.add(ShareCard2Fragment.getInstance());
        mFragmentList.add(ShareCard3Fragment.getInstance());
        mFragmentList.add(ShareCard4Fragment.getInstance());
        mFragmentList.add(ShareCard5Fragment.getInstance());
        mFragmentList.add(ShareCard6Fragment.getInstance());
        mShareViewPagerAdapter = new ShareViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mShareViewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                ((BaseCardFragment)mFragmentList.get(pagePos)).saveCardView();
                ToastUtils.showToast("卡片已经保存到存储卡");
                break;
            case R.id.tv_share:
                ((BaseCardFragment)mFragmentList.get(pagePos)).saveCardView();
                String imagePath = ((BaseCardFragment) mFragmentList.get(pagePos)).getImagePath();
                ShareDialog shareDialog = ShareDialog.getImageInstance(imagePath, AppConstant.APP_STORE);
                shareDialog.show(getSupportFragmentManager(),"");
                break;
        }
    }

    class ShareViewPagerAdapter extends FragmentPagerAdapter {
        public ShareViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
    }
}
