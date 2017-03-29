package com.matrix.yukun.matrix.leshilive_module.bean;

/**
 * Created by yukun on 17-3-29.
 */
public class LiveListBean {
    private String activityId;
    private String activityName;
    private String activityCategory;
    private int activityStatus;
    private String startTime;
    private String endTime;
    private String coverImgUrl;
    private String description;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LiveListBean{" +
                "activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityCategory='" + activityCategory + '\'' +
                ", activityStatus=" + activityStatus +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
