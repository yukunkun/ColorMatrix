package com.matrix.yukun.matrix.main_module.entity;

/**
 * Created by yukun on 18-1-5.
 */

public class EventUpdateHeader {
    private boolean isLoginOut;
    public EventUpdateHeader() {

    }

    public boolean isLoginOut() {
        return isLoginOut;
    }

    public void setLoginOut(boolean loginOut) {
        isLoginOut = loginOut;
    }
}
