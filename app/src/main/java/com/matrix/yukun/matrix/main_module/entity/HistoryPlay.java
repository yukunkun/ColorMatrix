package com.matrix.yukun.matrix.main_module.entity;

import org.litepal.crud.DataSupport;

/**
 * author: kun .
 * date:   On 2019/1/17
 */
public class HistoryPlay extends DataSupport {
    private String header;//目的地ID
    private String mName;//中文名
    private String cover;//
    private String play_url;
    private String title;
    private String nextUrl;
    private String description;
    private long data;
    private boolean isVideo;
    private int type;
    private int duration;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static void setHistoryInfo(EyesInfo eyesInfo,String nextUrl,int type){
        HistoryPlay collectInfo = new HistoryPlay();
        collectInfo.setHeader(eyesInfo.getData().getAuthor().getIcon());
        collectInfo.setCover(eyesInfo.getData().getCover().getDetail());
        collectInfo.setTitle(eyesInfo.getData().getTitle());
        collectInfo.setName(eyesInfo.getData().getSlogan());
        collectInfo.setNextUrl(nextUrl);
        collectInfo.setDescription(eyesInfo.getData().getSlogan());
        collectInfo.setData(eyesInfo.getData().getDate());
        collectInfo.setDuration(eyesInfo.getData().getDuration());
        collectInfo.setType(type);
        collectInfo.setPlay_url(eyesInfo.getData().getPlayUrl());
        collectInfo.save();
    }

    @Override
    public String toString() {
        return "HistoryPlay{" +
                "header='" + header + '\'' +
                ", mName='" + mName + '\'' +
                ", cover='" + cover + '\'' +
                ", play_url='" + play_url + '\'' +
                ", title='" + title + '\'' +
                ", nextUrl='" + nextUrl + '\'' +
                ", description='" + description + '\'' +
                ", data=" + data +
                ", isVideo=" + isVideo +
                ", type=" + type +
                ", duration=" + duration +
                '}';
    }
}
