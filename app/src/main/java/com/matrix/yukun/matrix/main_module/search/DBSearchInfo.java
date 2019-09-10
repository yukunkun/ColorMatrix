package com.matrix.yukun.matrix.main_module.search;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;

import org.litepal.crud.DataSupport;

/**
 * author: kun .
 * date:   On 2019/3/22
 */
public class DBSearchInfo extends DataSupport {

    private int videoId;
    private String title;
    private long timeStamp;
    private String slogan;
    private String description;
    private String videoImage;
    private String playUrl;
    private long publicTime;
    private String avatar;
    private String type;
    private String nextUrl;
    private long duration;
    private int searchType;
    private String name;
    private String authorDes;

    public static DBSearchInfo countToSearchInfo(EyesInfo eyesInfo, String nextUrl){
        EyesInfo.DataBean data = eyesInfo.getData();
        DBSearchInfo dbSearchInfo=new DBSearchInfo();
        dbSearchInfo.setVideoId(data.getId());
        dbSearchInfo.setTimeStamp(System.currentTimeMillis());
        dbSearchInfo.setTitle(data.getTitle());
        dbSearchInfo.setName(data.getAuthor().getName());
        dbSearchInfo.setAuthorDes(data.getAuthor().getDescription());
        dbSearchInfo.setDescription(data.getDescription());
        dbSearchInfo.setVideoImage(data.getCover().getDetail());
        dbSearchInfo.setPlayUrl(data.getPlayUrl());
        dbSearchInfo.setPublicTime(data.getDate());
        dbSearchInfo.setAvatar(data.getAuthor().getIcon());
        dbSearchInfo.setType(data.getType());
        dbSearchInfo.setDuration(data.getDuration());
        dbSearchInfo.setNextUrl(nextUrl);
        return dbSearchInfo;
    }

    public static EyesInfo countToEyeInfo(DBSearchInfo dbSearchInfo){
        EyesInfo eyesInfo=new EyesInfo();
        EyesInfo.DataBean.CoverBean cover = new EyesInfo.DataBean.CoverBean();
        EyesInfo.DataBean.AuthorBean authorBean = new EyesInfo.DataBean.AuthorBean();
        EyesInfo.DataBean data = new EyesInfo.DataBean();
        data.setPlayUrl(dbSearchInfo.getPlayUrl());
        data.setDuration((int) dbSearchInfo.getDuration());
        data.setDescription(dbSearchInfo.getDescription());
        data.setTitle(dbSearchInfo.getTitle());
        cover.setDetail(dbSearchInfo.getVideoImage());
        data.setCover(cover);
        data.setDate(dbSearchInfo.getPublicTime());
        authorBean.setIcon(dbSearchInfo.getAvatar());
        authorBean.setDescription(dbSearchInfo.getAuthorDes());
        authorBean.setName(dbSearchInfo.getName());
        data.setAuthor(authorBean);
        eyesInfo.setData(data);
        return eyesInfo;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public long getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(long publicTime) {
        this.publicTime = publicTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getAuthorDes() {
        return authorDes;
    }

    public void setAuthorDes(String authorDes) {
        this.authorDes = authorDes;
    }
    @Override
    public String toString() {
        return "DBSearchInfo{" +
                "videoId=" + videoId +
                ", title='" + title + '\'' +
                ", timeStamp=" + timeStamp +
                ", slogan='" + slogan + '\'' +
                ", description='" + description + '\'' +
                ", videoImage='" + videoImage + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", publicTime=" + publicTime +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                ", nextUrl='" + nextUrl + '\'' +
                ", duration=" + duration +
                ", searchType=" + searchType +
                '}';
    }
}
