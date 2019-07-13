package com.matrix.yukun.matrix.gaia_module.bean;

/**
 * author: kun .
 * date:   On 2019/7/13
 */
public enum VideoType {
    WORK("work",0),
    MATERIAL("material",1);

    String value;
    int type;

    VideoType(String value, int type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
