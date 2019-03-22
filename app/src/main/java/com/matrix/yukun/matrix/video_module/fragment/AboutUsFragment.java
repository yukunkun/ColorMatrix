package com.matrix.yukun.matrix.video_module.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.gesture_module.GestureActivity;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.matrix.yukun.matrix.video_module.common.AppBarStateChangeListener;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.video_module.play.AboutUsActivity;
import com.matrix.yukun.matrix.chat_module.ChatActivity;
import com.matrix.yukun.matrix.video_module.play.LoginActivity;
import com.matrix.yukun.matrix.video_module.play.MyGallaryActivity;
import com.matrix.yukun.matrix.video_module.play.ShareActivity;
import com.matrix.yukun.matrix.video_module.play.VideoSettingActivity;
import com.matrix.yukun.matrix.video_module.views.NoScrolledListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class AboutUsFragment extends BaseFragment {

    @BindView(R2.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R2.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R2.id.iv_bury)
    ImageView mIvBury;
    @BindView(R2.id.tv_version)
    TextView mTvVersion;
    @BindView(R2.id.tv_name)
    TextView mTvName;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.lv_list)
    NoScrolledListView mLvList;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.rl_header)
    RelativeLayout mRlheader;
    @BindView(R.id.circleview)
    CircleImageView mIvCircle;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    List<String> mStringList=new ArrayList();

    public static AboutUsFragment getInstance(){
        return new AboutUsFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mTvVersion.setText("V " + getVersion());
        mStringList= Arrays.asList(getResources().getStringArray(R.array.about_us_list));
        mLvList.setAdapter(new LvAdapter());
        initListener();
        if(MyApplication.userInfo!=null){
            Glide.with(this).load(MyApplication.getUserInfo().getImg()).into(mIvIcon);
            Glide.with(this).load(MyApplication.getUserInfo().getImg()).into(mIvCircle);
            mTvName.setText(MyApplication.getUserInfo().getName());
            //高斯模糊
            Glide.with(this).load(MyApplication.userInfo.getImg()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Blurry.with(getContext()).sampling(1).from(resource).into(mIvBury);
//                    //取色
                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            Palette.Swatch vibrant = palette.getDarkVibrantSwatch();
                            if (vibrant != null) {
                                mToolbar.setBackgroundColor(vibrant.getRgb());
                            }
                        }
                    });
                }
            });
        }
    }

    private void startAnimation(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
            Animator animationTop = ViewAnimationUtils.createCircularReveal(mIvBury, mIvBury.getWidth() / 2,
                    mIvBury.getHeight() / 2, 0,
                    Math.max(mIvBury.getWidth() / 2,
                            mIvBury.getHeight() / 2));
            animationTop.start();
        }
    }
    private void initTransition(){
            //代码制定过渡动画
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) getContext()).getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    mIvBury.setVisibility(View.GONE);
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTransitionEnd(Transition transition) {
                    mIvBury.setVisibility(View.VISIBLE);
                    Animator animationTop = ViewAnimationUtils.createCircularReveal(mIvBury, mIvBury.getWidth() / 2,
                            mIvBury.getHeight() / 2, 0,
                            Math.max(mIvBury.getWidth() / 2,
                                    mIvBury.getHeight() / 2));
                    animationTop.start();
                    ((Activity) getContext()).getWindow().getSharedElementEnterTransition().removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateHeader(EventUpdateHeader eventUpdateHeader){
        if(MyApplication.userInfo!=null){
            Glide.with(getContext()).load(MyApplication.getUserInfo().getImg()).into(mIvCircle);
            Glide.with(getContext()).load(MyApplication.userInfo.getImg()).into(mIvIcon);
            mTvName.setText(MyApplication.getUserInfo().getName());
            //高斯模糊
            Glide.with(this).load(MyApplication.userInfo.getImg()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Blurry.with(getContext()).sampling(1).from(resource).into(mIvBury);
                    //取色
                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                            if (vibrant != null) {
                                mToolbar.setBackgroundColor(vibrant.getRgb());
                            }
                        }
                    });
                }
            });
        }
    }
    private void initListener() {
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class aClass = null;
                switch (position) {
                    case 0:
                        aClass = ChatActivity.class;
                        break;
                    case 1:
                        aClass = GestureActivity.class;
                        break;
                    case 2:
                        aClass = MyGallaryActivity.class;
                        break;
                    case 3:
                        aClass = VideoSettingActivity.class;
                        break;
                    case 4:
                        aClass = ShareActivity.class;
                        break;
                }
                if (aClass != null) {
                    Intent intent = new Intent(getContext(), aClass);
                    startActivity(intent);
                }
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    mToolbar.setVisibility(View.GONE);
                    startAnimation();
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    mToolbar.setVisibility(View.VISIBLE);
                }else { //中间
                    mToolbar.setVisibility(View.GONE);
                }
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShareActivity.class);
                startActivity(intent);
            }
        });
        //share
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = ShareDialog.getInstance(getString(R.string.share_content), AppConstant.APP_STORE, AppConstant.APP_ICON_URl);
                shareDialog.show(getChildFragmentManager(),"");
            }
        });

        mIvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.userInfo==null){
                    Intent intentAbu = new Intent(getContext(), LoginActivity.class);
                    startActivity(intentAbu);
                }else {
                    Intent intent = new Intent(getContext(), AboutUsActivity.class);
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(),mIvIcon,"shareview").toBundle());
                    }else {
                        startActivity(intent);

                    }
                }
            }
        });
    }

    private String getVersion() {
        String mVersionName=null;
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            mVersionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVersionName;
    }

    class LvAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mStringList.size();
        }

        @Override
        public Object getItem(int position) {
            return mStringList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.setting_item_layout, null);
            TextView textView=inflate.findViewById(R.id.setting_con);
            TextView tvBottom=inflate.findViewById(R.id.tv_bottom);
            textView.setText(mStringList.get(position));
            if(position==2){
                tvBottom.setVisibility(View.VISIBLE);
            }
            return inflate;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
