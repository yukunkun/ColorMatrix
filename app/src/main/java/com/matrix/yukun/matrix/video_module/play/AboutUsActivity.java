package com.matrix.yukun.matrix.video_module.play;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.download_module.DownLoadActivity;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.matrix.yukun.matrix.video_module.adapter.CollectAdapter;
import com.matrix.yukun.matrix.video_module.adapter.ShareCallBack;
import com.matrix.yukun.matrix.video_module.common.AppBarStateChangeListener;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.entity.AttentList;
import com.matrix.yukun.matrix.video_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.video_module.entity.HistoryPlay;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tv_coll)
    TextView mTvColl;
    @BindView(R.id.tv_download)
    TextView mTvdownload;
    @BindView(R.id.tv_history)
    TextView mTvhistory;
    @BindView(R.id.tv_attent)
    TextView mTvAttent;
    @BindView(R.id.tv_sig)
    TextView mTvSig;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.iv_header_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_avator)
    CircleImageView mCircleImageView;
    @BindView(R.id.app_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_header)
    ImageView mIvHeader;

    private RecyclerView mRecyclerView;
    GridLayoutManager mLayoutManager;
    CollectAdapter mCollectAdapter;
    RelativeLayout mRelativeLayout;
    @Override
    public int getLayout() {
        return R.layout.activity_about_us_video_show;
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.rv);
        mRelativeLayout=findViewById(R.id.rl_remind);
        final List<CollectsInfo> collectInfoList = DataSupport.findAll(CollectsInfo.class);
        if(collectInfoList!=null&&collectInfoList.size()>0){
            mRelativeLayout.setVisibility(View.GONE);
            Collections.reverse(collectInfoList);
            mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mCollectAdapter = new CollectAdapter(this, collectInfoList);
            mRecyclerView.setAdapter(mCollectAdapter);
            mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(0, 4, 8, 16));
            mCollectAdapter.setShareCallBack(new ShareCallBack() {
                @Override
                public void onShareCallback(int pos) {
                    CollectsInfo collectsInfo = collectInfoList.get(pos);
                    ShareDialog instance = ShareDialog.getInstance(collectsInfo.getTitle(),collectsInfo.getPlay_url(),collectsInfo.getCover());
                    instance.show(getSupportFragmentManager(),"");
                }
            });
        }
        if(collectInfoList!=null&&collectInfoList.size()>0){
            mTvColl.setText(collectInfoList.size()+"");
        }
        List<HistoryPlay> historyPlays = DataSupport.findAll(HistoryPlay.class);
        if(historyPlays!=null&&historyPlays.size()>0){
            mTvhistory.setText(historyPlays.size()+"");
        }
        List<AttentList> attentLists = DataSupport.findAll(AttentList.class);
        if(attentLists!=null&&attentLists.size()>0){
            mTvAttent.setText(attentLists.size()+"");
        }
        File file=new File(AppConstant.VIDEOPATH);
        File[] listFiles = file.listFiles();
        if(listFiles!=null){
            mTvdownload.setText(listFiles.length+"");
        }
    }

    private void startAnimation(final View view){
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
                    Animator animationTop = ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2,
                            view.getHeight() / 2, 0,
                            Math.max(view.getWidth() / 2,
                                    view.getHeight() / 2));
                    animationTop.start();
                }
            }
        });
    }

    @Override
    public void initDate() {
        if(MyApplication.userInfo!=null){
            Glide.with(this).load(MyApplication.userInfo.getImg()).into(mCircleImageView);
            Glide.with(this).load(MyApplication.userInfo.getImg()).into(mIvHeader);
            mTvName.setText(MyApplication.userInfo.getName());
            mTvSig.setText(MyApplication.userInfo.getText());
            //高斯模糊
            Glide.with(this).load(MyApplication.userInfo.getImg()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Blurry.with(AboutUsActivity.this).sampling(1).from(resource).into(mIvBg);
                    startAnimation(mIvBg);
                }
            });
        }
    }

    @Override
    public void initListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    mIvHeader.setVisibility(View.GONE);
                    mTvTitle.setVisibility(View.VISIBLE);
                    startAnimation(mIvBg);
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    mIvHeader.setVisibility(View.VISIBLE);
                    mTvTitle.setVisibility(View.GONE);
                }else { //中间

                }
            }
        });
    }

    @OnClick({R2.id.iv_back,R2.id.iv_avator,R2.id.iv_more,R2.id.ll_coll,R2.id.ll_download,R2.id.ll_history,R2.id.ll_attent,R2.id.iv_header})
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.iv_back){
            finish();
        }if(id==R2.id.iv_avator||id==R2.id.iv_header){
            Intent intent=new Intent(this, ImageDetailActivity.class);
            intent.putExtra("url",MyApplication.userInfo.getImg());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,mCircleImageView,"shareView").toBundle());
            }else {
                startActivity(intent);
               overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
            }
        }if(id==R2.id.iv_more){
            Intent intent=new Intent(this, ShareActivity.class);
            startActivity(intent);
        }if(id==R2.id.ll_coll){
            Intent intentCol = new Intent(this, MyCollectActivity.class);
            startActivity(intentCol);
        }if(id==R2.id.ll_download){
            Intent intent=new Intent(this, DownLoadActivity.class);
            startActivity(intent);
        }if(id==R2.id.ll_history){
            Intent intent = new Intent(this, HistoryPlayActivity.class);
            startActivity(intent);
        }if(id==R2.id.ll_attent){
            Intent intent = new Intent(this, AttentionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
        return super.onKeyDown(keyCode, event);
    }
}
