package com.matrix.yukun.matrix.chat_module.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * author: kun .
 * date:   On 2019/3/26
 */
public abstract class MVPBaseActivity<T extends BasePresenter> extends AppCompatActivity {
    T mPresenter;
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

    protected abstract T createPresenter();

    public abstract int getLayout();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initDate();

}
