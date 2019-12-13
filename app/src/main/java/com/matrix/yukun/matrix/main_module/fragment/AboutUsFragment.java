package com.matrix.yukun.matrix.main_module.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatMemberActivity;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.main_module.activity.MyGallaryActivity;
import com.matrix.yukun.matrix.main_module.activity.PersonCenterActivity;
import com.matrix.yukun.matrix.main_module.common.AppBarStateChangeListener;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.mine_module.activity.ResponsbilityActivity;
import com.matrix.yukun.matrix.mine_module.activity.SettingActivity;
import com.matrix.yukun.matrix.mine_module.activity.ShareActivity;
import com.matrix.yukun.matrix.mine_module.entity.WebType;
import com.matrix.yukun.matrix.selfview.NoScrollListView;
import com.matrix.yukun.matrix.tool_module.gesture.GestureActivity;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;

public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.iv_bury)
    ImageView mIvBury;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.lv_list)
    NoScrollListView mLvList;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.rl_header)
    RelativeLayout mRlheader;
    @BindView(R.id.circleview)
    ImageView mIvCircle;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    List<String> mStringList = new ArrayList();

    public static AboutUsFragment getInstance() {
        return new AboutUsFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setData();
    }

    private void setData() {
        mTvVersion.setText("V " + getVersion());
        mStringList = Arrays.asList(getResources().getStringArray(R.array.about_us_list));
        mLvList.setAdapter(new LvAdapter());
        if (MyApp.userInfo != null) {
            GlideUtil.loadCircleImage(MyApp.getUserInfo().getAvator(),mIvCircle);
            GlideUtil.loadCircleBoardImage(MyApp.getUserInfo().getAvator(),mIvIcon);
            GlideUtil.loadBlurImage(MyApp.getUserInfo().getAvator(),mIvBury);
            mTvName.setText(MyApp.getUserInfo().getName());
        }else {
            GlideUtil.loadCircleBoardImage(R.mipmap.snail_image,mIvIcon);
        }
    }

    private void startAnimation() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Animator animationTop = ViewAnimationUtils.createCircularReveal(mIvBury, mIvBury.getWidth() / 2,
                    mIvBury.getHeight() / 2, 0,
                    Math.max(mIvBury.getWidth() / 2,
                            mIvBury.getHeight() / 2));
            animationTop.start();
        }
    }

    private void initTransition() {
        //代码制定过渡动画
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
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
    public void updateHeader(EventUpdateHeader eventUpdateHeader) {
        if (MyApp.userInfo != null) {
            GlideUtil.loadCircleImage(MyApp.getUserInfo().getAvator(),mIvCircle);
            GlideUtil.loadCircleBoardImage(MyApp.userInfo.getAvator(),mIvIcon);
            mTvName.setText(MyApp.getUserInfo().getName());
            //高斯模糊
            GlideUtil.loadBlurImage(MyApp.getUserInfo().getAvator(),mIvBury);
        }
    }

    public void initListener() {
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class aClass = null;
                switch (position) {
                    case 0:
                        aClass = ChatMemberActivity.class;
                        break;
                    case 1:
                        aClass = GestureActivity.class;
                        break;
                    case 2:
                        aClass = MyGallaryActivity.class;
                        break;
                    case 3:
                        aClass = SettingActivity.class;
                        break;
                    case 4:
                        aClass = ShareActivity.class;
                        break;
                    case 5:
                        ResponsbilityActivity.start(getContext(), WebType.RESPONSIBILITY.getType());
                        return;
                }
                if (position != 5 && aClass != null) {
                    Intent intent = new Intent(getContext(), aClass);
                    startActivity(intent);
                }
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mToolbar.setVisibility(View.GONE);
                    startAnimation();
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mToolbar.setVisibility(View.VISIBLE);
                } else { //中间
                    mToolbar.setVisibility(View.GONE);
                }
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShareActivity.class);
                startActivity(intent);
            }
        });
        //share
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = ShareDialog.getInstance(getString(R.string.share_content), AppConstant.APP_STORE, AppConstant.APP_ICON_URl);
                shareDialog.show(getChildFragmentManager(), "");
            }
        });

        mIvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.userInfo == null) {
                    Intent intentAbu = new Intent(getContext(), LoginActivity.class);
                    startActivity(intentAbu);
                } else {
                    Intent intent = new Intent(getContext(), PersonCenterActivity.class);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(), mIvIcon, "shareview").toBundle());
                    } else {
                        startActivity(intent);

                    }
                }
            }
        });
    }

    private String getVersion() {
        String mVersionName = null;
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            mVersionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVersionName;
    }

    class LvAdapter extends BaseAdapter {
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
            TextView textView = inflate.findViewById(R.id.setting_con);
            TextView tvBottom = inflate.findViewById(R.id.tv_bottom);
            textView.setText(mStringList.get(position));
            if (position == 2) {
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
