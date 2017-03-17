package com.matrix.yukun.matrix.leshi_module.bean;

/**
 * Created by yukun on 17-3-17.
 */
public class VideoBean {
    private int video_id;
    private String tag;
    private String video_unique;
    private String video_name;
    private String video_desc;
    private String img;
    private int video_duration;
    private String add_time;

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVideo_unique() {
        return video_unique;
    }

    public void setVideo_unique(String video_unique) {
        this.video_unique = video_unique;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "video_id=" + video_id +
                ", tag='" + tag + '\'' +
                ", video_unique='" + video_unique + '\'' +
                ", video_name='" + video_name + '\'' +
                ", video_desc='" + video_desc + '\'' +
                ", img='" + img + '\'' +
                ", video_duration=" + video_duration +
                ", add_time='" + add_time + '\'' +
                '}';
    }
}
