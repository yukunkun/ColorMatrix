package com.matrix.yukun.matrix.note_module.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.barrage_module.views.CircleTextView;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NoteSettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private CheckBox mCb;
    private EditText mEtPassword;
    private EditText mEtEditSize;
    private EditText mEtNoteSize;
    private CircleTextView mCtvEditColor;
    private CircleTextView mCtvEditBg;
    private CircleTextView mCtvNoteColor;
    private CircleTextView mCtvNoteBg;
    private TextView mTvSave;

    public static void start(Context context){
        Intent intent=new Intent(context,NoteSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_note_setting;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mCb = findViewById(R.id.cb_play);
        mEtPassword = findViewById(R.id.et_password);
        mEtEditSize = findViewById(R.id.et_edit_size);
        mEtNoteSize = findViewById(R.id.et_note_size);
        mCtvEditColor = findViewById(R.id.ctv_edit_color);
        mCtvEditBg = findViewById(R.id.ctv_edit_bg);
        mCtvNoteColor = findViewById(R.id.ctv_note_color);
        mCtvNoteBg = findViewById(R.id.ctv_note_bg);
        mTvSave = findViewById(R.id.tv_save_setting);
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.sc));
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mCtvEditColor.setOnClickListener(this);
        mCtvEditBg.setOnClickListener(this);
        mCtvNoteColor.setOnClickListener(this);
        mCtvNoteBg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save_setting:
                break;
            case R.id.ctv_edit_color:
                finish();
                break;
            case R.id.ctv_edit_bg:
                break;
            case R.id.ctv_note_color:
                break;
            case R.id.ctv_note_bg:
                break;
        }
    }
}
