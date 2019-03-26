package com.matrix.yukun.matrix.chat_module.mvp;

import android.content.Context;

/**
 * author: kun .
 * date:   On 2019/3/26
 */
public abstract class  BasePresenter <V extends BaseView>{
    protected Context mContext;
    protected V mView;
    public BasePresenter(Context context, V view) {
        this.mContext = context;
        this.mView = view;
    }
}
