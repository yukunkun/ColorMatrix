package com.matrix.yukun.matrix.main_module.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.service.DownloadNotificationService;
import com.matrix.yukun.matrix.main_module.adapter.RVVerticalAdapter;
import com.matrix.yukun.matrix.main_module.dialog.CommentFragmentDialog;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.DataUtils;

import java.util.ArrayList;

import static android.view.View.GONE;

public class VerticalVideoActivity extends BaseActivity implements RVVerticalAdapter.OnItemViewClickListener {

    private ArrayList<PlayAllBean> mPlayAllBeans;
    private int mType;
    private RecyclerView mRecyclerView;
    private RVVerticalAdapter mRVVerticalAdapter;
    private VideoView mVideoView;
    private ProgressBar mProgressBar;
    private ImageView mIvCover;
    private LinearLayoutManager mLinearLayoutManager;
    private int mLastVisibleItemPosition;
    private int mFirstVisibleItemPosition;
    private PopupWindow popupWindow;
    private int mMdy;
    private ImageView mIvMore;
    private PlayAllBean mMPlayAllBean;
    private Handler mHandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (mMdy > 0) {
                        mRVVerticalAdapter.setSelectPosition(mLastVisibleItemPosition);
                    } else {
                        mRVVerticalAdapter.setSelectPosition(mFirstVisibleItemPosition);
                    }
                    break;
                case 1:
                    mSeekBar.setProgress(mVideoView.getCurrentPosition());
                    mSeekBar.setSecondaryProgress(mVideoView.getBufferPercentage()*100+mVideoView.getCurrentPosition());
                    mHandle.sendEmptyMessageDelayed(1,1000);
                    mTvTime.setText(DataUtils.secToTime(mVideoView.getCurrentPosition()/1000)+"/"+ DataUtils.secToTime(mVideoView.getDuration()/1000));
                    break;
            }
        }
    };
    private TextView mTvTime;
    private SeekBar mSeekBar;
    private ImageView mIvZan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_vertical_video;
    }

    @Override
    public void initView() {
        mPlayAllBeans = (ArrayList<PlayAllBean>) getIntent().getSerializableExtra("playAllBean");
        mType = getIntent().getIntExtra("type", 0);
        mRecyclerView = findViewById(R.id.recyclerview);
        mVideoView = findViewById(R.id.videoview);
        mProgressBar = findViewById(R.id.progressbar);
        mIvCover = findViewById(R.id.iv_cover);
        mIvMore = findViewById(R.id.iv_more);
        mIvZan = findViewById(R.id.iv_zan);
        mTvTime = findViewById(R.id.tv_time);
        mSeekBar = findViewById(R.id.seekbar);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRVVerticalAdapter = new RVVerticalAdapter(this, mPlayAllBeans);
        mRecyclerView.setAdapter(mRVVerticalAdapter);
    }

    @Override
    public void initDate() {
        mProgressBar.setVisibility(View.VISIBLE);
        mVideoView.setVideoURI(Uri.parse(mPlayAllBeans.get(0).getVideo()));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBar.setMax(mVideoView.getDuration());
                mTvTime.setText("00:00/"+ DataUtils.secToTime(mVideoView.getDuration()/1000));
                mVideoView.start();
                mHandle.sendEmptyMessage(1);
                mp.setLooping(true);
                mProgressBar.setVisibility(GONE);
                mHandle.sendEmptyMessageDelayed(0,180);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtils.showToast("播放异常");
                mVideoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                return true;
            }
        });
    }

    @Override
    public void initListener() {
        mRVVerticalAdapter.setOnItemViewClickListener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               super.onScrollStateChanged(recyclerView, newState);
               if (newState == RecyclerView.SCROLL_STATE_SETTLING) {//停止
                   mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                   mFirstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                   if (mMdy > 0) {
                       int height = ScreenUtils.instance().getHeight(VerticalVideoActivity.this);
                       mVideoView.setVideoURI(Uri.parse(mPlayAllBeans.get(mLastVisibleItemPosition).getVideo()));
                   } else {
                       mVideoView.setVideoURI(Uri.parse(mPlayAllBeans.get(mFirstVisibleItemPosition).getVideo()));
                   }
                   mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mMdy = dy;
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    seekBar.setProgress(progress);
                    mVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onMoreClick(int position, PlayAllBean playAllBean) {
        mMPlayAllBean = playAllBean;
        showMorePopWindow(position,playAllBean);
    }

    @Override
    public void onLikeClick(int position, PlayAllBean playAllBean) {
        mMPlayAllBean=playAllBean;
        startAnimation(mIvZan);
    }

    @Override
    public void onCommentClick(int position, PlayAllBean playAllBean) {
        mMPlayAllBean=playAllBean;
        CommentFragmentDialog commentDialog= CommentFragmentDialog.getInstance(playAllBean.getSoureid()+"");
        commentDialog.show(getSupportFragmentManager(),"");
    }

    /**
     * 弹出popWindow
     * @param position
     * @param playAllBean
     */
    private void showMorePopWindow(int position, PlayAllBean playAllBean) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View popView = LayoutInflater.from(this).inflate(R.layout.main_plus_popupwindow, null);
        TextView mTvDownload = (TextView) popView.findViewById(R.id.create_group_tv);
        TextView tvShare = (TextView) popView.findViewById(R.id.add_friend_tv);
        popupWindow = new PopupWindow(popView, ScreenUtil.dip2px(130), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);// 设置弹出窗体可触摸
        popupWindow.setOutsideTouchable(true); // 设置点击弹出框之外的区域后，弹出框消失
        popupWindow.setAnimationStyle(R.style.TitleMorePopAnimationStyle); // 设置动画
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        ScreenUtil.setBackgroundAlpha(this, 0.2f);
        int popWidth = popupWindow.getContentView().getMeasuredWidth();
        int windowWidth = ScreenUtil.getDisplayWidth();
        int xOff = windowWidth - popWidth - ScreenUtil.dip2px(-45);    // x轴的偏移量
//        popupWindow.showAsDropDown(mIvMore, /*xOff*/0, /*-ScreenUtil.dip2px(4)*/0);  // 设置弹出框显示的位置
        popupWindow.showAsDropDown(mIvMore, -ScreenUtil.dip2px(130)+mIvMore.getWidth()/2+10, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ScreenUtil.setBackgroundAlpha(VerticalVideoActivity.this, 1f);
            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog= ShareDialog.getInstance(mMPlayAllBean.getText(),mMPlayAllBean.getVideo(),mMPlayAllBean.getThumbnail());
                shareDialog.show(getSupportFragmentManager(),"");
                popupWindow.dismiss();
            }
        });

        mTvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadNotificationService.getInstance().start(getApplicationContext());
                DownloadNotificationService.getInstance().startDownload(mMPlayAllBean.getVideo(),mMPlayAllBean.getThumbnail());
                ToastUtils.showToast("添加到下载列表成功");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandle!=null){
            mHandle.removeMessages(1);
            mHandle=null;
        }
    }
    private  void startAnimation(View view){
        mIvZan.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 20f, 0f, 20f, view.getWidth() / 2f, view.getHeight() / 2f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        view.setAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,0.6f);
        alphaAnimation.setDuration(800);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvZan.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
       animationSet.addAnimation(scaleAnimation);
       animationSet.addAnimation(alphaAnimation);
       view.setAnimation(animationSet);
    }
}
