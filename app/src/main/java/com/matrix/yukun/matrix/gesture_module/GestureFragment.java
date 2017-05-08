package com.matrix.yukun.matrix.gesture_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.GestureLockViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yukun on 17-5-8.
 */
public class GestureFragment extends Fragment {
    @BindView(R.id.et_secret)
    EditText mEtSecret;
    @BindView(R.id.lin)
    LinearLayout mLin;
    @BindView(R.id.tv_input_time)
    TextView mTvInputTime;
    @BindView(R.id.id_gestureLockViewGroup)
    GestureLockViewGroup mGestureLockViewGroup;
    @BindView(R.id.tv_sure)
    TextView mTvSure;
    @BindView(R.id.tv_reset)
    TextView mTvReset;

    private List<Integer> mListFirst = new ArrayList<>();
    private List<Integer> mListSecond = new ArrayList<>();
    private int secretPos = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_gesture, null);
        ButterKnife.bind(this, inflate);
        setListener();
        return inflate;
    }

    private void setListener() {
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        secretPos = 2;
                        mTvInputTime.setText("第二次输入");
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                        if (secretPos == 1) {
                            mListFirst.add(cId);
                        } else {
                            mListSecond.add(cId);
                        }
                    }
                });

        mEtSecret.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    @OnClick({R.id.tv_sure, R.id.tv_reset})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_sure:
                if (mEtSecret.getText().toString().length() == 0) {
                    showAlterDialog();
                    MyApp.showToast("请输入数字密码");
                } else if (!isSame()) {
                    secretPos = 1;
                    mListFirst.clear();
                    mListSecond.clear();
                    MyApp.showToast("请保证两次的手势相同");
                    mTvInputTime.setText("第一次输入");
                } else {
                    setSharePrefressSuccess("gesture", true);
                    setSharePrefress("secretStr", mEtSecret.getText().toString());
                    String secret = "";
                    for (int i = 0; i < mListFirst.size(); i++) {
                        secret = secret + mListFirst.get(i);
                    }
                    setSharePrefress("getsureLock", secret);
                    MyApp.showToast("设置成功!");
                    EventBus.getDefault().post(new OnEventFinish(1));
                }
                break;
            case R.id.tv_reset:
                setSharePrefressSuccess("gesture", false);
                MyApp.showToast("重置手势密码成功");
                EventBus.getDefault().post(new OnEventFinish(1));

                break;
        }
    }

    //判断两次相同的集合;
    private boolean isSame() {
        if (mListFirst.size() != mListSecond.size() || mListFirst.size() == 0) {
            return false;
        }
        for (int i = 0; i < mListFirst.size(); i++) {
            if (mListFirst.get(i) == mListSecond.get(i)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private void showAlterDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("忘记手势密码怎么办?")
                .setMessage("你可以通过数字密码重新进入")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void setSharePrefress(String tag, String str) {
        SharedPreferences sp = getActivity().getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag, str);
        editor.commit();
    }

    private void setSharePrefressSuccess(String tag, boolean str) {
        SharedPreferences sp = getActivity().getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(tag, str);
        editor.commit();
    }
}
