package com.ykk.pluglin_video.entity;

/**
 * Created by yukun on 17-11-20.
 */

public class RecInfo {
    private String play_url;
    private String user_name;
    private String text;
    private long play_time;
    private String share_url;
    private String create_time;
    private String header;
    private String cover;
    private boolean isGif;

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getPlay_time() {
        return play_time;
    }

    public void setPlay_time(long play_time) {
        this.play_time = play_time;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "RecInfo{" +
                "play_url='" + play_url + '\'' +
                ", user_name='" + user_name + '\'' +
                ", text='" + text + '\'' +
                ", play_time=" + play_time +
                ", share_url='" + share_url + '\'' +
                ", create_time='" + create_time + '\'' +
                ", header='" + header + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
