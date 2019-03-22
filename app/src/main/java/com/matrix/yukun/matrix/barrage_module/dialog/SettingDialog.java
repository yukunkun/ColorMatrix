package com.matrix.yukun.matrix.barrage_module.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.barrage_module.BarrageActivity;
import com.matrix.yukun.matrix.barrage_module.views.CircleTextView;


/**
 * author: kun .
 * date:   On 2018/10/8
 */
public class SettingDialog extends BaseBottomDialog implements View.OnClickListener {

    private CircleTextView mCtvBg;
    private CircleTextView mCtvTextColor;
    private boolean isBg;
    private TextView mTvBig;
    private TextView mTvXie;
    private SeekBar mSbTextSize;
    private SeekBar mSbTextSpeed;
    private static boolean isBig;
    private static boolean isLean;
    private static  int size;
    private static  int speed;
    private static  int mBgColr=-1;
    private static  int mTextColor=-14575885;

    public static SettingDialog getInstance(boolean  mIsBig,boolean mIsLean,int mSize,int mSpeed,int bgColor,int textColor ){
        SettingDialog settingDialog = new SettingDialog();
        isBig=mIsBig;
        isLean=mIsLean;
        size=mSize;
        speed=mSpeed;
        mBgColr=bgColor;
        mTextColor=textColor;
        return settingDialog;
    }

    @Override
    public int setLayout() {
        return R.layout.setting_dialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mCtvBg = inflate.findViewById(R.id.ctv_bg);
        mCtvTextColor = inflate.findViewById(R.id.ctv_tv_color);
        mTvBig = inflate.findViewById(R.id.ctv_style_big);
        mTvXie = inflate.findViewById(R.id.ctv_style_xie);
        mSbTextSize = inflate.findViewById(R.id.sb_text_size);
        mSbTextSpeed = inflate.findViewById(R.id.sb_text_speed);
        mSbTextSize.setMax(150);
        mSbTextSpeed.setMax(80);
        mCtvBg.setBackColor(mBgColr);
        mCtvTextColor.setBackColor(mTextColor);
    }

    @Override
    protected void initData() {
        //是否加粗
        if(isBig){
            mTvBig.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_selected));
            mTvBig.setTextColor(getContext().getResources().getColor(R.color.color_00ff00));
        }else {
            mTvBig.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_unselected));
            mTvBig.setTextColor(getContext().getResources().getColor(R.color.color_b3ffffff));
        }
        //是否倾斜
        if(isLean){
            mTvXie.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_selected));
            mTvXie.setTextColor(getContext().getResources().getColor(R.color.color_00ff00));
        }else {
            mTvXie.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_unselected));
            mTvXie.setTextColor(getContext().getResources().getColor(R.color.color_b3ffffff));
        }
        mSbTextSize.setProgress(size);
        mSbTextSpeed.setProgress(speed);
    }

    @Override
    protected void initListener() {
        mCtvBg.setOnClickListener(this);
        mCtvTextColor.setOnClickListener(this);
        mTvBig.setOnClickListener(this);
        mTvXie.setOnClickListener(this);
        mSbTextSize.setOnSeekBarChangeListener(new SBSizeListener());
        mSbTextSpeed.setOnSeekBarChangeListener(new SBSpeedlistener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ctv_bg:
                isBg=true;
                showColorPicker();
                break;
            case R.id.ctv_tv_color:
                isBg=false;
                showColorPicker();
                break;
            case R.id.ctv_style_big:
                isBig=!isBig;
                if(isBig){
                    mTvBig.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_selected));
                    mTvBig.setTextColor(getContext().getResources().getColor(R.color.color_00ff00));
                }else {
                    mTvBig.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_unselected));
                    mTvBig.setTextColor(getContext().getResources().getColor(R.color.color_b3ffffff));
                }
                ((BarrageActivity)getActivity()).getIsBig(isBig);
                break;
            case R.id.ctv_style_xie:
                isLean=!isLean;
                if(isLean){
                    mTvXie.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_selected));
                    mTvXie.setTextColor(getContext().getResources().getColor(R.color.color_00ff00));
                }else {
                    mTvXie.setBackground(getContext().getResources().getDrawable(R.drawable.shape_text_unselected));
                    mTvXie.setTextColor(getContext().getResources().getColor(R.color.color_b3ffffff));
                }
                ((BarrageActivity)getActivity()).getIsLean(isLean);
                break;
        }
    }

    private void showColorPicker() {
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
        colorPickerDialog.show(getActivity().getFragmentManager(), "color-picker-dialog");
    }

    private ColorPickerDialogListener pickerDialogListener = new ColorPickerDialogListener() {
        @Override
        public void onColorSelected(int dialogId, @ColorInt int color) {
                if(isBg){
                    mCtvBg.setBackColor(color);
                    ((BarrageActivity)getActivity()).getBgColor(color);
                }else {
                    mCtvTextColor.setBackColor(color);
                    ((BarrageActivity)getActivity()).getTextColor(color);
                }
        }

        @Override
        public void onDialogDismissed(int dialogId) {

        }
    };

    class SBSizeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            ((BarrageActivity)getActivity()).getTextSize(progress+1);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class SBSpeedlistener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            ((BarrageActivity)getActivity()).getTextSpeed(progress+2);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((BarrageActivity)getActivity()).dialogDismiss();
    }
}
