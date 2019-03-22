package com.matrix.yukun.matrix.video_module.entity;

/**
 * Created by yukun on 18-1-4.
 */

public class FeedInfo {


    /**
     * id : 1815
     * title : Android开发：Handler异步通信机制全面解析（包含Looper、Message Queue）
     * chapterId : 10
     * chapterName : Activity
     * envelopePic : null
     * link : http://www.jianshu.com/p/9fe944ee02f7
     * author : Carson_Ho
     * origin : null
     * publishTime : 1514802487000
     * zan : null
     * desc : null
     * visible : 1
     * niceDate : 2天前
     * courseId : 13
     * collect : false
     */

    private int id;
    private String title;
    private int chapterId;
    private String chapterName;
    private Object envelopePic;
    private String link;
    private String author;
    private Object origin;
    private long publishTime;
    private Object zan;
    private Object desc;
    private int visible;
    private String niceDate;
    private int courseId;
    private boolean collect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Object getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(Object envelopePic) {
        this.envelopePic = envelopePic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public Object getZan() {
        return zan;
    }

    public void setZan(Object zan) {
        this.zan = zan;
    }

    public Object getDesc() {
        return desc;
    }

    public void setDesc(Object desc) {
        this.desc = desc;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "FeedInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", envelopePic=" + envelopePic +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", origin=" + origin +
                ", publishTime=" + publishTime +
                ", zan=" + zan +
                ", desc=" + desc +
                ", visible=" + visible +
                ", niceDate='" + niceDate + '\'' +
                ", courseId=" + courseId +
                ", collect=" + collect +
                '}';
    }
}
