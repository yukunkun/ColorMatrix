package com.matrix.yukun.matrix.main_module.entity;

import com.google.gson.Gson;

import cn.bmob.v3.BmobObject;

/**
 * author: kun .
 * date:   On 2019/11/14
 */
public class UserInfoBMob extends BmobObject {
    public String id;
    private String key;
    private String account;
    private String phone;
    private String name;
    private String passwd;
    private String text;
    private String avator;
    private String signature;
    private String gender;
    private String createTime;

    public String getId() {
        return getObjectId();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

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

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public static String toJson(UserInfoBMob userInfoBMob){
        Gson gson=new Gson();
        String toJson = gson.toJson(userInfoBMob);
        return toJson;
    }

    @Override
    public String toString() {
        return "UserInfoBMob{" +
                "id='" + getObjectId() + '\'' +
                ", key='" + key + '\'' +
                ", account='" + account + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", text='" + text + '\'' +
                ", avator='" + avator + '\'' +
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
