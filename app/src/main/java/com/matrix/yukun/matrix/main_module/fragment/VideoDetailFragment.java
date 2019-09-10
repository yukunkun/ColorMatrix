package com.matrix.yukun.matrix.main_module.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.PersonActivity;
import com.matrix.yukun.matrix.main_module.entity.AttentList;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * author: kun .
 * date:   On 2018/12/27
 */
public class VideoDetailFragment extends BaseFragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private EyesInfo mEyesInfo;
    private CircleImageView mCircleImageView;
    private ScrollView mScrollView;
    private TextView mTvTitle;
    private TextView mTvTime;
    private TextView mTvStar;
    private RatingBar mRatingBar;
    private TextView mTvSlogn;
    private TextView mTvCollect;
    private TextView mTvAttent;
    private TextView mTvDetail;
    private Random mRandom=new Random();
    private int mNextInt;
    private String mNextUrl;
    private boolean flag;
    private LinearLayout mLlRootView;

    public static VideoDetailFragment getInstance(EyesInfo eyesInfo, String nextUrl, int type){
        VideoDetailFragment videoDetailFragment=new VideoDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("eyesInfo",eyesInfo);
        bundle.putString("nextUrl",nextUrl);
        videoDetailFragment.setArguments(bundle);
        return videoDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEyesInfo = (EyesInfo)getArguments().getSerializable("eyesInfo");
        mNextUrl = getArguments().getString("nextUrl");
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_detail;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mCircleImageView = inflate.findViewById(R.id.cv_cover);
        mScrollView = inflate.findViewById(R.id.sc);
        mTvTitle = inflate.findViewById(R.id.tv_video_title);
        mTvTime = inflate.findViewById(R.id.tv_send_time);
        mTvStar = inflate.findViewById(R.id.tv_video_star);
        mRatingBar = inflate.findViewById(R.id.rating_bar);
        mTvSlogn = inflate.findViewById(R.id.tv_slogn);
        mTvCollect = inflate.findViewById(R.id.tv_collect);
        mTvAttent = inflate.findViewById(R.id.tv_attent);
        mLlRootView = inflate.findViewById(R.id.ll);

        mTvDetail = inflate.findViewById(R.id.tv_video_detail);
        OverScrollDecoratorHelper.setUpOverScroll(mScrollView);
        initData();
        isCollect();
        isAttent();
        mTvCollect.setOnClickListener(this);
        mTvAttent.setOnClickListener(this);
        mRatingBar.setOnRatingBarChangeListener(this);
        mCircleImageView.setOnClickListener(this);
    }

    private void isCollect() {
        List<CollectsInfo> collectsInfos = DataSupport.findAll(CollectsInfo.class);
        for (int i = 0; i < collectsInfos.size(); i++) {
            if(collectsInfos.get(i).getPlay_url().equals(mEyesInfo.getData().getPlayUrl())){
                updateCollectView(true);
                return;
            }
        }
    }

    private void startAnimation(final View view){
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
                    Animator animationTop = ViewAnimationUtils.createCircularReveal(view, 0,
                            0, 0,
                            Math.max(view.getWidth(),
                                    view.getHeight()));
                    animationTop.setDuration(1000);
                    animationTop.setInterpolator(new AccelerateInterpolator());
                    animationTop.start();

                }
            }
        });
    }
    private void isAttent() {
        List<AttentList> collectsInfos = DataSupport.findAll(AttentList.class);
        for (int i = 0; i < collectsInfos.size(); i++) {
            if(collectsInfos.get(i).getPlay_url().equals(mEyesInfo.getData().getPlayUrl())){
                updateAttentView(true);
                return;
            }
        }
    }

    private void initData() {
        mNextInt = mRandom.nextInt(10);
        EyesInfo.DataBean data = mEyesInfo.getData();
        Glide.with(getContext()).load(data.getAuthor().getIcon()).into(mCircleImageView);
        mTvSlogn.setText(data.getAuthor().getName());
        mTvTitle.setText(data.getAuthor().getDescription());
        mTvDetail.setText(data.getDescription());
        mTvTime.setText("发布于："+getTime(mEyesInfo.getData().getDate()));
        mTvStar.setText(mNextInt+"");
        mRatingBar.setRating(mNextInt/2);
        startAnimation(mLlRootView);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.tv_collect){
            if(mTvCollect.getText().equals("已收藏")){
                //取消收藏
                DataSupport.deleteAll(CollectsInfo.class, "play_url = ?", mEyesInfo.getData().getPlayUrl());
                Toast.makeText(getContext(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                updateCollectView(false);
            }else {
                //加入
                CollectsInfo collectInfo = new CollectsInfo();
                collectInfo.setHeader(mEyesInfo.getData().getAuthor().getIcon());
                collectInfo.setCover(mEyesInfo.getData().getCover().getDetail());
                collectInfo.setTitle(mEyesInfo.getData().getTitle());
                collectInfo.setName(mEyesInfo.getData().getAuthor().getName());
                collectInfo.setNextUrl(mNextUrl);
                collectInfo.setDescription(mEyesInfo.getData().getDescription());
                collectInfo.setAuthorDes(mEyesInfo.getData().getAuthor().getDescription());
                collectInfo.setData(mEyesInfo.getData().getDate());
                collectInfo.setType(1);
                collectInfo.setPlay_url(mEyesInfo.getData().getPlayUrl());
                collectInfo.save();
                Toast.makeText(getContext(), "添加到收藏成功", Toast.LENGTH_SHORT).show();
                updateCollectView(true);
            }
        }
        if(v.getId()== R.id.cv_cover){

            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.putExtra("eyesInfo",mEyesInfo);
            intent.putExtra("next_url",mNextUrl);
            intent.putExtra("type",3);//小视频界面要用
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)  getContext(),mCircleImageView,"shareView").toBundle());
            }else {
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
            }

        }if(v.getId()== R.id.tv_attent){
            if(mTvAttent.getText().equals("已关注")){
                //取消收藏
                DataSupport.deleteAll(AttentList.class, "play_url = ?", mEyesInfo.getData().getPlayUrl());
                Toast.makeText(getContext(), "取消关注成功", Toast.LENGTH_SHORT).show();
                updateAttentView(false);
            }else {
                //加入
                AttentList collectInfo = new AttentList();
                collectInfo.setHeader(mEyesInfo.getData().getAuthor().getIcon());
                collectInfo.setCover(mEyesInfo.getData().getCover().getDetail());
                collectInfo.setTitle(mEyesInfo.getData().getTitle());
                collectInfo.setName(mEyesInfo.getData().getAuthor().getName());
                collectInfo.setNextUrl(mNextUrl);
                collectInfo.setDescription(mEyesInfo.getData().getDescription());
                collectInfo.setAuthorDes(mEyesInfo.getData().getAuthor().getDescription());
                collectInfo.setData(mEyesInfo.getData().getDate());
                collectInfo.setType(1);
                collectInfo.setPlay_url(mEyesInfo.getData().getPlayUrl());
                collectInfo.save();
                Toast.makeText(getContext(), "添加关注成功", Toast.LENGTH_SHORT).show();
                updateAttentView(true);
            }
        }
    }

    private void updateCollectView(boolean isCollect) {
        if(isCollect){
            mTvCollect.setBackgroundResource(R.drawable.shape_collect_bg_checked);
            mTvCollect.setTextColor(getResources().getColor(R.color.color_e9e7e7));
            mTvCollect.setText("已收藏");
        }else {
            mTvCollect.setBackgroundResource(R.drawable.shape_collect_bg_unchecked);
            mTvCollect.setTextColor(getResources().getColor(R.color.color_ff2323));
            mTvCollect.setText("+收藏");
        }
    }
    private void updateAttentView(boolean isAttent) {
        if(isAttent){
            mTvAttent.setBackgroundResource(R.drawable.shape_collect_bg_checked);
            mTvAttent.setTextColor(getResources().getColor(R.color.color_e9e7e7));
            mTvAttent.setText("已关注");
        }else {
            mTvAttent.setBackgroundResource(R.drawable.shape_collect_bg_unchecked);
            mTvAttent.setTextColor(getResources().getColor(R.color.color_ff2323));
            mTvAttent.setText("+关注");
        }
    }

    private String getTime(long times){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(times);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(true){
            ToastUtils.showToast("您的评分为"+rating*2+"分");
            double lastRat = mNextInt * 0.8 + rating * 2 * 0.2;
            mTvStar.setText(String.format("%.1f", lastRat));
        }
    }
}
