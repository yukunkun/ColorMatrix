package com.matrix.yukun.matrix.main_module.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.main_module.entity.UserInfo;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private CardView mCardViewLogin;
    private CardView mCardViewRegister;
    private boolean loginIsFront=false;
    private TextView mTvRigister;
    private CardView mCardLoad;
    private ImageView mIvBack;
    private EditText mTvLoginName;
    private EditText mTvLoginPassword;
    private EditText mTvRegisterName;
    private EditText mTvRegisterPassword;
    private TextView mTvLogin;
    private String url="https://www.apiopen.top/login";
    private TextView mTvBack;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    public static void start(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.left_out, R.anim.right_in);
    }

    @Override
    public void initView() {
        mCardViewLogin = findViewById(R.id.card_login);
        mCardViewRegister = findViewById(R.id.card_register);
        mTvRigister = findViewById(R.id.tv_register);
        mCardLoad = findViewById(R.id.card_load);
        mIvBack = findViewById(R.id.iv_back);
        mTvBack = findViewById(R.id.tv_back);
        mTvLoginName = findViewById(R.id.et_login_name);
        mTvLoginPassword = findViewById(R.id.et_login_password);
        mTvRegisterName = findViewById(R.id.et_register_name);
        mTvRegisterPassword = findViewById(R.id.et_register_password);
        mTvLogin = findViewById(R.id.tv_login);
    }

    @Override
    public void initDate() {
        //开始动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
        alphaAnimation.setDuration(300);
        this.mIvBack.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCardViewLogin.bringToFront();
                startAnimationSmall(mCardViewRegister);
                startAnimationBig(mCardViewLogin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void initListener() {
        mCardViewLogin.setOnClickListener(this);
        mCardViewRegister.setOnClickListener(this);
        mTvRigister.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.iv_back){
            finish();
        }
        if(id== R.id.card_login){
            loginIsFront=false;
            mCardViewLogin.bringToFront();
            startAnimationSmall(mCardViewRegister);
            startAnimationBig(mCardViewLogin);
        }
        if(id== R.id.card_register){
            loginIsFront=true;
            mCardViewRegister.bringToFront();
            startAnimationSmall(mCardViewLogin);
            startAnimationBig(mCardViewRegister);
        }
        if(id== R.id.tv_register){
            if(!loginIsFront){
                loginIsFront=true;
                mCardViewRegister.bringToFront();
                startAnimationSmall(mCardViewLogin);
                startAnimationBig(mCardViewRegister);
            }else {
                if(!TextUtils.isEmpty(mTvRegisterName.getText().toString().trim())&&!TextUtils.isEmpty(mTvRegisterPassword.getText().toString().trim())){
                    register(mTvRegisterName.getText().toString().trim(),mTvRegisterPassword.getText().toString().trim());
                }else {
                    ToastUtils.showToast("账号密码不能为空");
                }
            }
        }
        if(id== R.id.tv_login){
            if(!TextUtils.isEmpty(mTvLoginName.getText().toString().trim())&&!TextUtils.isEmpty(mTvLoginPassword.getText().toString().trim())){
                showLoad();
                login(mTvLoginName.getText().toString().trim(),mTvLoginPassword.getText().toString().trim());
            }else {
                ToastUtils.showToast("账号密码不能为空");
            }
        }
        if(id== R.id.tv_back){
            finish();
        }
    }

    /**
     * 登录
     * @param name
     * @param password
     */
    private void login(String name, String password) {
        OkHttpUtils.get().url(url)
                .addParams("key", AppConstant.Appkey)
                .addParams("phone",name)
                .addParams("passwd",password)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String code = jsonObject.optString("code");
                    if(code.equals("200")){
                        JSONObject data = jsonObject.optJSONObject("data");
                        Gson gson=new Gson();
                        UserInfo userInfo = gson.fromJson(data.toString(), UserInfo.class);
                        MyApp.setUserInfo(userInfo);
                        //存数据库
                        userInfo.save();
                        ToastUtils.showToast("登录成功");
                        EventBus.getDefault().post(new EventUpdateHeader());
                        finish();
                    }else {
                        mCardViewLogin.setVisibility(View.VISIBLE);
                        mCardViewRegister.setVisibility(View.VISIBLE);
                        mCardLoad.setVisibility(View.GONE);
                        ToastUtils.showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 注册
     * @param name
     * @param password
     */
    private void register(final String name, final String password) {
        showLoad();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, ReViewActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("password",password);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void showLoad(){
        mCardLoad.setVisibility(View.VISIBLE);
        startAnimationLoad(mCardLoad);
        mCardViewRegister.setVisibility(View.GONE);
        mCardViewLogin.setVisibility(View.GONE);
    }

    private void startAnimationSmall(View view){
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f,0.85f);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
    }
    private void startAnimationBig(View view){
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.85f,1f);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
    }
    private void startAnimationLoad(View view){
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f,0.6f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f,0.6f, 1f);
        scaleX.setRepeatCount(1000);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleY.setRepeatCount(1000);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
    }

}
