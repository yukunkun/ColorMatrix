package com.matrix.yukun.matrix.tool_module.phonebook;

/**
 * author: kun .
 * date:   On 2018/11/22
 */
public class PhoneBean {
    private PhoneNumberBean mPhoneNumberBean;
    private String sortLetters;

    public PhoneBean(PhoneNumberBean phoneNumberBean, String sortLetters) {
        mPhoneNumberBean = phoneNumberBean;
        this.sortLetters = sortLetters;
    }

    public PhoneNumberBean getPhoneNumberBean() {
        return mPhoneNumberBean;
    }

    public void setPhoneNumberBean(PhoneNumberBean phoneNumberBean) {
        mPhoneNumberBean = phoneNumberBean;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String toString() {
        return "PhoneBean{" +
                "mPhoneNumberBean=" + mPhoneNumberBean +
                ", sortLetters='" + sortLetters + '\'' +
                '}';
    }
}
