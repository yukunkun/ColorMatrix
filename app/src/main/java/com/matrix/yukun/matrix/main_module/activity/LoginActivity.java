package com.matrix.yukun.matrix.main_module.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.mine_module.activity.ResponsbilityActivity;
import com.matrix.yukun.matrix.mine_module.entity.WebType;
import com.matrix.yukun.matrix.util.DataUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
    private TextView mTvBack;
    private TextView mTvSecret;
    private TextView mTvAccount;

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
        mTvSecret = findViewById(R.id.tv_secret);
        mTvAccount = findViewById(R.id.tv_account);
    }

    @Override
    public void initDate() {
        //开始动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
        alphaAnimation.setDuration(300);
        this.mIvBack.startAnimation(alphaAnimation);
        mCardViewRegister.postDelayed(new Runnable() {
            @Override
            public void run() {
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
        },100);
    }

    @Override
    public void initListener() {
        mCardViewLogin.setOnClickListener(this);
        mCardViewRegister.setOnClickListener(this);
        mTvRigister.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        mTvSecret.setOnClickListener(this);
        mTvAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.iv_back){
            if(!SPUtils.getInstance().getBoolean("first")){
                MainActivity.start(this);
                finish();
            }else {
                finish();
            }
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
                if(TextUtils.isEmpty(mTvRegisterName.getText().toString().trim())||!DataUtils.isNumericzidai(mTvRegisterName.getText().toString().trim())){
                    ToastUtils.showToast("账号不能为空且只能为数字");
                    return;
                }
                if(!TextUtils.isEmpty(mTvRegisterName.getText().toString().trim())&&!TextUtils.isEmpty(mTvRegisterPassword.getText().toString().trim())){
                    devideRegist(mTvRegisterName.getText().toString().trim(),new FindListener<UserInfoBMob>() {
                        @Override
                        public void done(List<UserInfoBMob> infoBMobs, BmobException e) {
                            if(e==null){
                                if(infoBMobs.size()>0){
                                    //查询到用户
                                    ToastUtils.showToast(mTvRegisterName.getText().toString().trim()+" "+"已经被注册");
                                }else {
                                    //跳转到注册界面
                                    register(mTvRegisterName.getText().toString().trim(),mTvRegisterPassword.getText().toString().trim());
                                }
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
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
            if(!SPUtils.getInstance().getBoolean("first")){
                MainActivity.start(this);
                finish();
            }else {
                finish();
            }
        }
        if(id== R.id.tv_secret){
            ResponsbilityActivity.start(this, WebType.SECRET.getType());
        }
        if(id== R.id.tv_account){
            ResponsbilityActivity.start(this, WebType.AGREEMENT.getType());
        }
    }

    private void devideRegist(String name,FindListener<UserInfoBMob> userInfoBMobFindListener) {
        BmobQuery<UserInfoBMob> query = new BmobQuery<UserInfoBMob>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("account", name);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(userInfoBMobFindListener);
    }


    /**
     * 登录
     * @param name
     * @param password
     */
    private void login(String name, String password) {
        BmobQuery<UserInfoBMob> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("account", name);
        query.addWhereEqualTo("passwd", password);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //执行查询方法
        query.findObjects(new FindListener<UserInfoBMob>() {
            @Override
            public void done(List<UserInfoBMob> list, BmobException e) {
                if(e==null){
                    UserInfoBMob userInfoBMob=new UserInfoBMob();
                    for (int i = 0; i < list.size(); i++) {
                        userInfoBMob=list.get(i);
                    }
                    if(!TextUtils.isEmpty(userInfoBMob.getName())){
                        MyApp.setUserInfo(userInfoBMob);
                        EventBus.getDefault().post(new EventUpdateHeader());
                        if(!SPUtils.getInstance().getBoolean("first")) {
                            MainActivity.start(LoginActivity.this);
                        }
                        finish();
                    }else {
                        ToastUtils.showToast("登录出错，请检查账号密码");
                        visiableLoad();
                    }
                }else {
                    ToastUtils.showToast("登录出错");
                    visiableLoad();
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
                    Thread.sleep(1000);
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

    private void visiableLoad(){
        mCardLoad.setVisibility(View.GONE);
        mCardViewRegister.setVisibility(View.VISIBLE);
        mCardViewLogin.setVisibility(View.VISIBLE);
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
