package com.matrix.yukun.matrix.phone_module;

/**
 * author: kun .
 * date:   On 2018/11/21
 */
public class PhoneNumberBean {
    String phoneNum;
    String contactName;

    public PhoneNumberBean() {
    }

    public PhoneNumberBean( String contactName,String phoneNum) {
        this.phoneNum = phoneNum;
        this.contactName = contactName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        contactName = contactName;
    }

    @Override
    public String toString() {
        return "PhoneNumberBean{" +
                "phoneNum='" + phoneNum + '\'' +
                ", contactName='" + contactName + '\'' +
                '}';
    }
}
