package com.matrix.yukun.matrix.leshi_module.bean;


/**
 * Created by yukun on 17-3-16.
 */
public class ListBeanJson<T> {
    private String code;
    private String message;
    private int total;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LeShiListBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", total='" + total + '\'' +
                ", date=" + data +
                '}';
    }
}
