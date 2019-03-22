package com.matrix.yukun.matrix.calarder_module.contant;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/22.
 */

public class CalandarBean extends DataSupport implements Serializable{
    private long time;
    private String createTime;
    private String calandarTitle;
    private String calandarContent;
    private String type;
    private String pleace;
    private int Categray;

    public int getCategray() {
        return Categray;
    }

    public void setCategray(int categray) {
        Categray = categray;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCalandarTitle() {
        return calandarTitle;
    }

    public void setCalandarTitle(String calandarTitle) {
        this.calandarTitle = calandarTitle;
    }

    public String getCalandarContent() {
        return calandarContent;
    }

    public void setCalandarContent(String calandarContent) {
        this.calandarContent = calandarContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPleace() {
        return pleace;
    }

    public void setPleace(String pleace) {
        this.pleace = pleace;
    }

    @Override
    public String toString() {
        return "CalandarBean{" +
                "time=" + time +
                ", createTime='" + createTime + '\'' +
                ", calandarTitle='" + calandarTitle + '\'' +
                ", calandarContent='" + calandarContent + '\'' +
                ", type='" + type + '\'' +
                ", pleace='" + pleace + '\'' +
                ", Categray='" + Categray + '\'' +
                '}';
    }
}
