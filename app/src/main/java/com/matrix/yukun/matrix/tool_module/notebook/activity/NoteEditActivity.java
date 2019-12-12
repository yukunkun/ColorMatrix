package com.matrix.yukun.matrix.tool_module.notebook.activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.ColorInt;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;

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
    private int textSize=16;
    private LinearLayout mRlBg;
    private RelativeLayout mRlTitle;

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
        mRlBg = findViewById(R.id.ll_bg);
        mRlTitle = findViewById(R.id.rl_title);
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
    public void initDate() {
        if(SPUtils.getInstance().getEditSize()>0){
            textSize=SPUtils.getInstance().getEditSize();
            mEtContent.setTextSize(SPUtils.getInstance().getEditSize());
        }
        if(SPUtils.getInstance().getEditColor()!=0){
            mEtContent.setTextColor(SPUtils.getInstance().getEditColor());
        }
        if(SPUtils.getInstance().getEditBg()!=0){
            mRlBg.setBackgroundColor(SPUtils.getInstance().getEditBg());
            mRlTitle.setBackgroundColor(SPUtils.getInstance().getEditBg());
        }
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
                textSize++;
                mEtContent.setTextSize(textSize);
                SPUtils.getInstance().setEditSize(textSize);
                break;
            case R.id.iv_text_reduce:
                textSize--;
                mEtContent.setTextSize(textSize);
                SPUtils.getInstance().setEditSize(textSize);
                break;
            case R.id.iv_text_color:
                setTextColor();
                break;
            case R.id.iv_text_bg:
                setTextBg();
                break;
        }
    }

    private void setTextColor() {
        showColorPicker(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, @ColorInt int color) {
                mEtContent.setTextColor(color);
                SPUtils.getInstance().setEditColor(color);
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
    }

    private void setTextBg() {
        showColorPicker(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, @ColorInt int color) {
                mRlBg.setBackgroundColor(color);
                mRlTitle.setBackgroundColor(color);
                SPUtils.getInstance().setEditBg(color);
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
    }

    private void showColorPicker(ColorPickerDialogListener pickerDialogListener) {
        //传入的默认color
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder()/*.setColor(0)*/
                .setDialogTitle(R.string.color_picker)
                //设置dialog标题
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                //设置为自定义模式
                .setShowAlphaSlider(false)
                //设置有透明度模式，默认没有透明度
                .setDialogId(80)
                //设置Id,回调时传回用于判断
                .setAllowPresets(false)
                //不显示预知模式
                .create();
        //Buider创建
        colorPickerDialog.setColorPickerDialogListener(pickerDialogListener);
        //设置回调，用于获取选择的颜色
        colorPickerDialog.show(getFragmentManager(), "color-picker-dialog");
    }
}
