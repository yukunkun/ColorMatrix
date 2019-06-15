package com.matrix.yukun.matrix.note_module.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

public class NoteEditActivity extends BaseActivity implements View.OnClickListener {


    private String mPath;
    private EditText mEtTitle;
    private EditText mEtContent;
    private ImageView mIvBack;
    private TextView mTvPreView;
    private ImageView mIvTextAdd;
    private ImageView mIvTextColor;
    private ImageView mIvTextBg;
    private ImageView mIvTextReduce;

    public static void start (Context context){
        Intent intent=new Intent(context, NoteEditActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context,String path){
        Intent intent=new Intent(context, NoteEditActivity.class);
        intent.putExtra("path",path);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_note_edit;
    }

    @Override
    public void initView() {
        mPath = getIntent().getStringExtra("path");
        mEtTitle = findViewById(R.id.et_title);
        mEtContent = findViewById(R.id.et_context);
        mIvBack = findViewById(R.id.iv_back);
        mTvPreView = findViewById(R.id.tv_save);
        mIvTextAdd = findViewById(R.id.iv_text_add);
        mIvTextReduce = findViewById(R.id.iv_text_reduce);
        mIvTextColor = findViewById(R.id.iv_text_color);
        mIvTextBg = findViewById(R.id.iv_text_bg);
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvPreView.setOnClickListener(this);
        mIvTextAdd.setOnClickListener(this);
        mIvTextReduce.setOnClickListener(this);
        mIvTextColor.setOnClickListener(this);
        mIvTextBg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                if(!TextUtils.isEmpty(mEtContent.getText().toString())){
                    NotePreviewActivity.start(this,!TextUtils.isEmpty(mEtTitle.getText().toString()) ?  mEtTitle.getText().toString(): "我的日记",mEtContent.getText().toString());
                }else {
                    ToastUtils.showToast("请留下你的日记足迹");
                }
                break;
            case R.id.iv_text_add:
                break;
            case R.id.iv_text_reduce:
                break;
            case R.id.iv_text_color:
                break;
            case R.id.iv_text_bg:
                break;
        }
    }
}
