package com.matrix.yukun.matrix.note_module.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.util.Base64Encode;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NotePreviewActivity extends BaseActivity implements View.OnClickListener {

    private String mTitle;
    private String mContent;
    private String mPath;
    private ImageView mIvBack;
    private ImageView mIvSetting;
    private ScrollView mSc;
    private TextView mTvcontent;
    private RelativeLayout mRlTitle;
    private TextView mTvSave;

    public static void start(Context context, String title, String content){
        Intent intent=new Intent(context, NotePreviewActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        context.startActivity(intent);
    }

    public static void start(Context context,String path){
        Intent intent=new Intent(context, NotePreviewActivity.class);
        intent.putExtra("path",path);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_note_preview;
    }

    @Override
    public void initView() {
        mTitle = getIntent().getStringExtra("title");
        mContent = getIntent().getStringExtra("content");
        mPath = getIntent().getStringExtra("path");
        mIvBack = findViewById(R.id.iv_back);
        mIvSetting = findViewById(R.id.iv_setting);
        mRlTitle = findViewById(R.id.rl_title);
        mTvSave = findViewById(R.id.tv_save);
        mSc = findViewById(R.id.sc);
        mTvcontent = findViewById(R.id.tv_content);
    }

    @Override
    public void initDate() {
        if(!TextUtils.isEmpty(mTitle)&&!TextUtils.isEmpty(mContent)){
            mTvcontent.setText(mTitle+'\n'+'\n'+mContent);
        }else if(!TextUtils.isEmpty(mPath)) {
            String decrypt = Base64Encode.setDecrypt(FileUtil.read(mPath));
            mTvcontent.setText(decrypt);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mIvSetting.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        OverScrollDecoratorHelper.setUpOverScroll(mSc);
//        mSc.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(scrollY>0){
//                    mRlTitle.setVisibility(View.GONE);
//                }else {
//                    mRlTitle.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_setting:
                break;
            case R.id.tv_save:
                long currentTimeMillis = System.currentTimeMillis();
                if(TextUtils.isEmpty(mPath)){
                    FileUtil.write(AppConstant.NOTEPATH,currentTimeMillis+".txt", Base64Encode.setEncryption(mTvcontent.getText().toString()));
                }else {

                }
                ToastUtils.showToast("保存成功");
                break;
        }
    }
}
