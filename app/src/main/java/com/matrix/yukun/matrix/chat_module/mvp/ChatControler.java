package com.matrix.yukun.matrix.chat_module.mvp;

import android.content.Context;

/**
 * author: kun .
 * date:   On 2019/3/26
 */
public interface ChatControler {

    interface View extends BaseView{

    }

    abstract class Presenter extends BasePresenter<ChatControler.View> {

        public Presenter(Context context, ChatControler.View view) {
            super(context, view);
        }
    }

}
