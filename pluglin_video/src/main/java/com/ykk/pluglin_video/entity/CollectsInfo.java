package com.ykk.pluglin_video.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by yukun on 17-11-21.
 */

public class CollectsInfo extends DataSupport {
    private String header;//目的地ID
    private String mName;//中文名
    private String cover;//英文名
    private String play_url;
    private String title;
    private boolean isGif;
    private boolean isVideo;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

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
}
