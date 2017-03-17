package com.matrix.yukun.matrix.leshi_module.bean;

/**
 * Created by yukun on 17-3-16.
 */
public class ListBean {
    private long video_id;
    private String video_unique;
    private String video_name;
    private String  img;
    private String init_pic;
    private long video_duration;
    private long initial_size;
    private String complete_time;
    private String add_time;
    private String video_desc;
    private String tag;
    private String  file_md5;
    private String usercategory1;
    private String usercategory2;

    public long getVideo_id() {
        return video_id;
    }

    public void setVideo_id(long video_id) {
        this.video_id = video_id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInit_pic() {
        return init_pic;
    }

    public void setInit_pic(String init_pic) {
        this.init_pic = init_pic;
    }

    public long getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(long video_duration) {
        this.video_duration = video_duration;
    }

    public long getInitial_size() {
        return initial_size;
    }

    public void setInitial_size(long initial_size) {
        this.initial_size = initial_size;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }

    public String getUsercategory1() {
        return usercategory1;
    }

    public void setUsercategory1(String usercategory1) {
        this.usercategory1 = usercategory1;
    }

    public String getUsercategory2() {
        return usercategory2;
    }

    public void setUsercategory2(String usercategory2) {
        this.usercategory2 = usercategory2;
    }

    @Override
    public String toString() {
        return "ListBean{" +
                "video_id=" + video_id +
                ", video_unique='" + video_unique + '\'' +
                ", video_name='" + video_name + '\'' +
                ", img='" + img + '\'' +
                ", init_pic='" + init_pic + '\'' +
                ", video_duration=" + video_duration +
                ", initial_size=" + initial_size +
                ", complete_time='" + complete_time + '\'' +
                ", add_time='" + add_time + '\'' +
                ", video_desc='" + video_desc + '\'' +
                ", tag='" + tag + '\'' +
                ", file_md5='" + file_md5 + '\'' +
                ", usercategory1='" + usercategory1 + '\'' +
                ", usercategory2='" + usercategory2 + '\'' +
                '}';
    }
}
