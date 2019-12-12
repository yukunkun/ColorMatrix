package com.matrix.yukun.matrix.tool_module.notebook.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.Base64Encode;
import com.matrix.yukun.matrix.util.FileUtil;

import java.io.File;

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
    private FloatingActionButton mFab;
    private FloatingToolbar mFloatingToolbar;
    private LinearLayout mLlBg;

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
        mLlBg = findViewById(R.id.ll_bg);
        mRlTitle = findViewById(R.id.rl_title);
        mTvSave = findViewById(R.id.tv_save);
        mFab = findViewById(R.id.fab);
        mFloatingToolbar = findViewById(R.id.floatingToolbar);
        mSc = findViewById(R.id.sc);
        mTvcontent = findViewById(R.id.tv_content);
        mFloatingToolbar.attachFab(mFab);
    }

    @Override
    public void initDate() {
        if(!TextUtils.isEmpty(mTitle)&&!TextUtils.isEmpty(mContent)){
            mTvcontent.setText(mTitle+'\n'+'\n'+mContent);
        }else if(!TextUtils.isEmpty(mPath)) {
            String decrypt = Base64Encode.setDecrypt(FileUtil.read(mPath));
            mTvcontent.setText(decrypt);
        }
        if(SPUtils.getInstance().getNoteSize()>0){
            mTvcontent.setTextSize(SPUtils.getInstance().getNoteSize());
        }
        if(SPUtils.getInstance().getNoteColor()!=0){
            mTvcontent.setTextColor(SPUtils.getInstance().getNoteColor());
        }
        if(SPUtils.getInstance().getNoteBg()!=0){
            mLlBg.setBackgroundColor(SPUtils.getInstance().getNoteBg());
            mRlTitle.setBackgroundColor(SPUtils.getInstance().getNoteBg());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mIvSetting.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        OverScrollDecoratorHelper.setUpOverScroll(mSc);
        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_share:
                        share(mTvcontent.getText().toString());
                        break;
                    case R.id.menu_out:
                        noteOut();
                        break;
                    case R.id.menu_del:
                        deleteNote();
                        break;
                    case R.id.menu_logout:
                        break;

                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });
    }

    private void deleteNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除日记")
                .setMessage("亲，是否删除日记?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(mPath)){
                    File file=new File(mPath);
                    file.delete();
                }
                finish();
            }
        }).show();
    }

    private void noteOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("导出日记")
                .setMessage("亲，是否导出日记到手机?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("导出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileUtil.write(AppConstant.PATH,System.currentTimeMillis()+".txt",mTvcontent.getText().toString());
                ToastUtils.showToast("日记已经保存到"+ AppConstant.PATH);
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_setting:
                NoteSettingActivity.start(this);
                break;
            case R.id.tv_save:
                long currentTimeMillis = System.currentTimeMillis();
                if(TextUtils.isEmpty(mPath)){
                    String fileName=currentTimeMillis+".txt";
                    FileUtil.write(AppConstant.NOTEPATH,fileName, Base64Encode.setEncryption(mTvcontent.getText().toString()));
                    mPath= AppConstant.NOTEPATH+File.separator+fileName;
                }else {

                }
                ToastUtils.showToast("保存成功");
                break;
        }
    }

    private void share(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享连接");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, ""));
    }
}
