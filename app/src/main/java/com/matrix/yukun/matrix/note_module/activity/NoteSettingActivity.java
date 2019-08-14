package com.matrix.yukun.matrix.note_module.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.barrage_module.views.CircleTextView;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.SPUtils;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.util.regex.Pattern;

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
    private String password;
    private @ColorInt int editColor;
    private @ColorInt int editBg;
    private @ColorInt int noteColor;
    private @ColorInt int noteBg;

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
        mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mEtPassword.setVisibility(View.VISIBLE);
                }else {
                    mEtPassword.setVisibility(View.GONE);
                    ToastUtils.showToast("密码将移除");
                }
            }
        });
    }

    @Override
    public void initDate() {
        if(SPUtils.getInstance().getNotePassword()!=null&&!SPUtils.getInstance().getNotePassword().equals("")){
            mCb.setChecked(true);
            mEtPassword.setVisibility(View.VISIBLE);
            mEtPassword.setText(SPUtils.getInstance().getNotePassword());
        }
        if(SPUtils.getInstance().getEditSize()>0){
            mEtEditSize.setText(SPUtils.getInstance().getEditSize()+"");
        }
        if(SPUtils.getInstance().getNoteSize()>0){
            mEtNoteSize.setText(SPUtils.getInstance().getNoteSize()+"");
        }
        if(SPUtils.getInstance().getEditColor()!=0){
            editColor=SPUtils.getInstance().getEditColor();
            mCtvEditColor.setBackColor(editColor);
        }
        if(SPUtils.getInstance().getEditBg()!=0){
            editBg=SPUtils.getInstance().getEditBg();
            mCtvEditBg.setBackColor(editBg);
        }
        if(SPUtils.getInstance().getNoteColor()!=0){
            noteColor=SPUtils.getInstance().getNoteColor();
            mCtvNoteColor.setBackColor(noteColor);
        }
        if(SPUtils.getInstance().getNoteBg()!=0){
            noteBg=SPUtils.getInstance().getNoteBg();
            mCtvNoteBg.setBackColor(noteBg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save_setting:
                saveSetting();
                break;
            case R.id.ctv_edit_color:
                setEditColor();
                break;
            case R.id.ctv_edit_bg:
                setEditBg();
                break;
            case R.id.ctv_note_color:
                setNoteColor();
                break;
            case R.id.ctv_note_bg:
                setNoteBg();
                break;
        }
    }

    private void saveSetting() {
        if(mCb.isChecked()&&!TextUtils.isEmpty(mEtPassword.getText().toString())){
            SPUtils.getInstance().setNotePassword(mEtPassword.getText().toString());
        }else {
            SPUtils.getInstance().removeNotePassword();
        }
        if(!TextUtils.isEmpty(mEtEditSize.getText().toString())){
            if(Pattern.matches("\\d+",mEtEditSize.getText().toString())){
                SPUtils.getInstance().setEditSize(Integer.valueOf(mEtEditSize.getText().toString()));
            }else {
                ToastUtils.showToast("文字大小必须是数字");
            }
        }
        if(!TextUtils.isEmpty(mEtNoteSize.getText().toString())){
            if(Pattern.matches("\\d+",mEtNoteSize.getText().toString())){
                SPUtils.getInstance().setNoteSize(Integer.valueOf(mEtNoteSize.getText().toString()));
            }else {
                ToastUtils.showToast("文字大小必须是数字");
            }
        }
        if(editColor!=0){
            SPUtils.getInstance().setEditColor(editColor);
        }
        if(editBg!=0){
            SPUtils.getInstance().setEditBg(editBg);
        }
        if(noteColor!=0){
            SPUtils.getInstance().setNoteColor(noteColor);
        }
        if(noteBg!=0){
            SPUtils.getInstance().setNoteBg(noteBg);
        }
        ToastUtils.showToast("保存设置成功");
    }

    private void setEditColor() {
        showColorPicker(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                mCtvEditColor.setBackColor(color);
                editColor=color;
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
    }
    private void setEditBg() {
        showColorPicker(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                mCtvEditBg.setBackColor(color);
                editBg=color;
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
    }
    private void setNoteColor() {
        showColorPicker(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                mCtvNoteColor.setBackColor(color);
                noteColor=color;
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
    }
    private void setNoteBg() {
        showColorPicker(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                mCtvNoteBg.setBackColor(color);
                noteBg=color;
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
