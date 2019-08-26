package com.matrix.yukun.matrix.tool_module.calarder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.calarder.contant.CalandarBean;
import com.matrix.yukun.matrix.tool_module.calarder.contant.TypeBean;
import com.matrix.yukun.matrix.util.DataUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/22.
 */

public class EditCalandarDialog extends DialogFragment {
    @BindView(R.id.tv_add_con)
    TextView mTvAddCon;
    @BindView(R.id.tv_title)
    TextView mTv;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_real_time)
    TextView mTvRealTime;
    @BindView(R.id.et_time)
    TextView mEtTime;
    @BindView(R.id.tv_please)
    TextView mTvPlease;
    @BindView(R.id.et_please)
    EditText mEtPlease;
    @BindView(R.id.et_content)
    EditText mEtContent;
    String time = "";
    @BindView(R.id.tv_categray)
    TextView mTvCategray;
    @BindView(R.id.tv_default)
    TextView mTvDefault;
    @BindView(R.id.tv_life)
    TextView mTvLife;
    @BindView(R.id.tv_work)
    TextView mTvWork;
    @BindView(R.id.tv_other)
    TextView mTvOther;
    List<TextView> mTextViews=new ArrayList<>();
    private int selectPos=0;

    public static EditCalandarDialog getInstance(String time) {
        EditCalandarDialog editCalandarDialog = null;
        if (editCalandarDialog == null) {
            editCalandarDialog = new EditCalandarDialog();
            Bundle bundle = new Bundle();
            bundle.putString("time", time);
            editCalandarDialog.setArguments(bundle);
        }
        return editCalandarDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = getArguments().getString("time");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        View view = inflater.from(getContext()).inflate(R.layout.dialog_edit_calandar, null);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));//设置背景透明
        mTvRealTime.setText(time);
        mTextViews.add(mTvDefault);
        mTextViews.add(mTvLife);
        mTextViews.add(mTvWork);
        mTextViews.add(mTvOther);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            // 设置弹出框布局参数，宽度铺满全屏，底部。
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);
        }
    }

    @OnClick(R.id.tv_add_con)
    public void onViewClicked() {
        if (TextUtils.isEmpty(mEtTitle.getText().toString().trim()) ||
                TextUtils.isEmpty(mEtPlease.getText().toString().trim()) ||
                TextUtils.isEmpty(mEtTime.getText().toString().trim()) ||
                TextUtils.isEmpty(mEtContent.getText().toString().trim())) {
            Toast.makeText(getContext(), "请填写完整内容", Toast.LENGTH_SHORT).show();
        } else {
            CalandarBean calandarBean = new CalandarBean();
            calandarBean.setType(TypeBean.TYPE_NOT_START);
            calandarBean.setCalandarTitle(mEtTitle.getText().toString().trim());
            calandarBean.setCreateTime(time + "/" + mEtTime.getText().toString().trim());
            calandarBean.setPleace(mEtPlease.getText().toString().trim());
            calandarBean.setCategray(selectPos);
            calandarBean.setCalandarContent(mEtContent.getText().toString().trim());
            calandarBean.setTime(DataUtils.getStringToDate(time,"yyyy/MM/dd"));
            boolean save = calandarBean.save();
            if (save) {
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
            getDialog().dismiss();
        }
    }


    @OnClick({R.id.tv_default, R.id.tv_life, R.id.tv_work, R.id.tv_other, R.id.et_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_default:
                updateView(0);
                break;
            case R.id.tv_life:
                updateView(1);
                break;
            case R.id.tv_work:
                updateView(2);
                break;
            case R.id.tv_other:
                updateView(3);
                break;
            case R.id.et_time:
                initTimePicker(getContext());
                mEtTitle.setSelected(false);
                mEtContent.setSelected(false);
                mEtPlease.setSelected(false);
                break;
        }
    }

    private void updateView(int pos) {
        selectPos=pos;
        for (int i = 0; i < mTextViews.size(); i++) {
            if(pos==i){
                mTextViews.get(i).setTextColor(getContext().getResources().getColor(R.color.color_00ff00));
            }else {
                mTextViews.get(i).setTextColor(getContext().getResources().getColor(R.color.color_8a8a8a));
            }
        }
    }

    public void initTimePicker(Context context) {//Dialog 模式下，在底部弹出

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();

        startDate.set(2018, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 11, 30);
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String startTime=dataToString(date);
                mEtTime.setText(startTime);
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setCancelColor(context.getResources().getColor(R.color.color_2c2c2c))
                .setRangDate(selectedDate, endDate)
                .setType(new boolean[]{false, false, false, true, true, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        mDialog.show();
    }

    public String dataToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm");
        return simpleDateFormat.format(date);
    }

}
