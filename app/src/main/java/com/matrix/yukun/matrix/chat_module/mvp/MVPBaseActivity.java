package com.matrix.yukun.matrix.chat_module.mvp;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

/**
 * author: kun .
 * date:   On 2019/3/26
 */
public abstract class MVPBaseActivity<T extends BasePresenter> extends AppCompatActivity {
    public T mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        mPresenter=createPresenter();
        initView();
        initDate();
        initListener();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null){
                return super.dispatchTouchEvent(ev);
            } else if(v!=null){
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected abstract T createPresenter();

    public abstract int getLayout();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initDate();

}
