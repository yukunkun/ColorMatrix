package com.matrix.yukun.matrix.main_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.GestureLockViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LockActivity extends AppCompatActivity {

    @BindView(R.id.titile)
    TextView mTitile;
    @BindView(R.id.tv_forget)
    TextView mTvForget;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.tv_input)
    TextView mTvInput;
    @BindView(R.id.id_gestureLockViewGroup)
    GestureLockViewGroup mGestureLockViewGroup;
    private String secretResult="";
    private int inputTime=2;
    private EditText mEditText;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        mGestureLockViewGroup.setUnMatchExceedBoundary(3);

        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        mGestureLockViewGroup.setUnMatchExceedBoundary(3);
                        mGestureLockViewGroup.setClickable(false);
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                            if(istrue(secretResult)){
                                Intent intent=new Intent(LockActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                secretResult="";
                                if(inputTime==0){
                                    MyApp.showToast("你可能忘记手势密码了");
                                }
                                if(inputTime>0){
                                    MyApp.showToast("您还有"+inputTime+"次机会");
                                }
                                inputTime--;
                            }
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                       secretResult=secretResult+cId;

                    }
                });
    }
    private boolean istrue(String secret){
        SharedPreferences preferences=getSharedPreferences("getsureLock", Context.MODE_PRIVATE);
        String result = preferences.getString("getsureLock","");
        if(result.equals(secret)){
            return true;
        }else {
            return false;
        }
    }
    private boolean istrueLock(String secret){
        SharedPreferences preferences=getSharedPreferences("secretStr", Context.MODE_PRIVATE);
        String result = preferences.getString("secretStr","");
        if(result.equals(secret)){
            return true;
        }else {
            return false;
        }
    }


    public void LockBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_forget)
    public void onClick() {
        inputSecret();
    }

    private void inputSecret() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_secret, null);
        mEditText = (EditText) inflate.findViewById(R.id.et_secret);
        mAlertDialog =
                new AlertDialog.Builder(this).setTitle("输入密码")
                .setView(inflate)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (istrueLock(mEditText.getText().toString())) {
                            Intent intent = new Intent(LockActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            resetSecret();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void resetSecret(){
        mAlertDialog.dismiss();
        new AlertDialog.Builder(this).setTitle("是否重置手势密码")
                .setMessage("重置密码你的记录将会清零")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(istrueLock(mEditText.getText().toString())){
                            Intent intent = new Intent(LockActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            setSharePrefressSuccess("gesture",false);
                            MyApp.showToast("已重置手势密码,请重新进入APP");
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void setSharePrefressSuccess(String tag, boolean str) {
        SharedPreferences sp = getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(tag, str);
        editor.commit();
    }
}
