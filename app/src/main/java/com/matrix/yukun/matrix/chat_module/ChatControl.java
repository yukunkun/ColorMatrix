package com.matrix.yukun.matrix.chat_module;

/**
 * Created by yukun on 17-5-11.
 */
public interface ChatControl {

    public interface chatBasePresent{
        void getInfo(String msg);
    }

    public interface View{
        void initView();
        void setListener();
        void getMsg(String msg);
    }
}
