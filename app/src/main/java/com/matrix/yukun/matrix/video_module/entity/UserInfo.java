package com.matrix.yukun.matrix.video_module.entity;

import org.litepal.crud.DataSupport;

/**
 * author: kun .
 * date:   On 2019/1/18
 */
public class UserInfo extends DataSupport {

    /**
     * key : 00d91e8e0cca2b76f515926a36db68f5
     * phone : 上岸的与
     * name :
     * passwd : 123654
     * text :
     * img :
     * other :
     * other2 :
     * createTime : 2019-01-18 17:27:06
     */

    private String key;
    private String phone;
    private String name;
    private String passwd;
    private String text;
    private String img;
    private String other;
    private String other2;
    private String createTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
