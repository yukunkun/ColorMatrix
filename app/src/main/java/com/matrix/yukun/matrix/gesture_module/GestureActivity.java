package com.matrix.yukun.matrix.gesture_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.GestureLockViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GestureActivity extends AppCompatActivity {

    @BindView(R.id.titile)
    TextView mTitile;
    @BindView(R.id.tv_forget)
    TextView mTvForget;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.lin)
    LinearLayout mLin;
    @BindView(R.id.id_gestureLockViewGroup)
    GestureLockViewGroup mGestureLockViewGroup;
    @BindView(R.id.tv_sure)
    TextView mTvSure;
    @BindView(R.id.et_secret)
    EditText mEtSecret;
    @BindView(R.id.tv_input_time)
    TextView mTvInputTime;
    @BindView(R.id.tv_reset)
    TextView mTvReset;

    private List<Integer> mListFirst = new ArrayList<>();
    private List<Integer> mListSecond = new ArrayList<>();
    private int secretPos = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        ButterKnife.bind(this);
        setListener();
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

    public void GestureBack(View view) {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    @OnClick({R.id.titile, R.id.tv_forget, R.id.title, R.id.lin, R.id.tv_sure,R.id.tv_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                showAlterDialog();
                break;
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
                    finish();
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                }
                break;
            case R.id.tv_reset:
                setSharePrefressSuccess("gesture",false);
                MyApp.showToast("重置手势密码成功");
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
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
        new AlertDialog.Builder(this)
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
        SharedPreferences sp = getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag, str);
        editor.commit();
    }

    private void setSharePrefressSuccess(String tag, boolean str) {
        SharedPreferences sp = getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(tag, str);
        editor.commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }


}
