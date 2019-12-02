package com.matrix.yukun.matrix.mine_module.entity;

/**
 * author: kun .
 * date:   On 2019/8/21
 */
public enum WebType {
    AGREEMENT(0,"协议"),
    INTRODUCE(1,"简介"),
    RESPONSIBILITY(2,"免责"),
    QUESTION(3,"桌面提示"),
    SECRET(4,"隐私政策");

    int type;
    String value;

    WebType(int i, String s) {
        this.type=i;
        this.value=s;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
