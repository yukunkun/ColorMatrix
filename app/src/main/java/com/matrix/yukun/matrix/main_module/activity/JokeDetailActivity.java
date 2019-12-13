package com.matrix.yukun.matrix.main_module.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.emoji.CubeEmoticonTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class JokeDetailActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.tv_content)
    CubeEmoticonTextView mTvContent;
    private String mContent;

    @Override
    public int getLayout() {
        return R.layout.activity_joke_detail;
    }

    public static void start(Context context,String content,View view){
        Intent intent=new Intent(context,JokeDetailActivity.class);
        intent.putExtra("content",content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,view,"shareView").toBundle());
        }else {
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
        }
    }

    @Override
    public void initView() {
        mContent = getIntent().getStringExtra("content");
        mTvContent.setText(mContent);
        startAnimation(mTvContent);
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
                    animationTop.setDuration(500);
                    animationTop.start();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_share})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finish();

        } else if (i == R.id.iv_share) {
            shareSend();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键时所执行的命令
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 此处写你按返回键之后要执行的事件的逻辑
            finish();
            return true;
//            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void shareSend() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, mContent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, ""));
    }
}
