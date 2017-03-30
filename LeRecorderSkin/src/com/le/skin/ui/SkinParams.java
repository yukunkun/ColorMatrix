package com.le.skin.ui;

import android.hardware.Camera;

import com.letv.recorder.letvrecorderskin.R;

public class SkinParams {
    //设置横屏推流还是竖屏推流，默认是竖屏形式
    private boolean isLanscape = false;
    //设置视频流的宽度，如果不设置，默认是640*368
    private int videoWidth = 0;
    //设置视频流的高度，如果不设置，默认是640*368
    private int videoHeight = 0;
    //设置默认前置摄像头还是默认后置摄像头。如果不设置，默认是后置摄像头
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    //设置默认推流码率，如果不设置，默认是1000*1000；
    private int videoBitrate = 1000*1000;
    //是否开启对焦功能，默认开启
    private boolean isOnTouch = true;
    //是否开启对焦点击动画，默认开启
    private boolean isOnAnimation = true;
    //对焦图片，在使用对焦功能时生效
    private int foceView = R.drawable.letv_focus_auto;
    //是否开启自动日志文件上传，如果开启，在SDK发生错误的时候，会自动上传。
    private boolean updateLogFile = false;
    //显示视频Title
    private String title;
    //是否开启音量，默认开启
    private boolean isVolumeGain = true;
    //是否在后台回到前台后自动推流，默认开启。****注意，对于云直播或者开启防盗链的移动直播上，这个功能暂时不能用。使用时一定要注意这一点***
    private boolean isResume = true;
//    private boolean isNetResume = true;
    //在云直播中，是否默认推第一个机位。
    private boolean isFirstMachine = false;
    //设置UI view的宽度。注意，这个宽度不影响视频流的宽度。如果不设置，默认是LayoutParams.MATCH_PARENT
    private int surfaceWidth = 0;
    //设置UI view的宽度。注意，这个宽度不影响视频流的高度。如果不设置，默认是LayoutParams.MATCH_PARENT
    private int surfaceHeight = 0;
//    private boolean isNowPush = false;
    //镜像模式。默认是开启镜像模式，表现效果和手机照相机一致。如果关闭表现效果是和实际动作相反
    private boolean isMirror = true;
    // 开启拉近拉远双指手势操作
    private boolean isOpenGestureZoom = true;

    public boolean isLanscape() {
        return isLanscape;
    }

    public void setLanscape(boolean lanscape) {
        isLanscape = lanscape;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public boolean isOnTouch() {
        return isOnTouch;
    }

    public void setOnTouch(boolean onTouch) {
        isOnTouch = onTouch;
    }

    public boolean isOnAnimation() {
        return isOnAnimation;
    }

    public void setOnAnimation(boolean onAnimation) {
        isOnAnimation = onAnimation;
    }

    public int getFoceView() {
        return foceView;
    }

    public void setFoceView(int foceView) {
        this.foceView = foceView;
    }

    public boolean isUpdateLogFile() {
        return updateLogFile;
    }

    public void setUpdateLogFile(boolean updateLogFile) {
        this.updateLogFile = updateLogFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVolumeGain() {
        return isVolumeGain;
    }

    public void setVolumeGain(boolean volumeGain) {
        isVolumeGain = volumeGain;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }

//    public boolean isNetResume() {
//        return isNetResume;
//    }
//
//    public void setNetResume(boolean netResume) {
//        isNetResume = netResume;
//    }

    public boolean isFirstMachine() {
        return isFirstMachine;
    }

    public void setFirstMachine(boolean firstMachine) {
        isFirstMachine = firstMachine;
    }

    public int getSurfaceWidth() {
        return surfaceWidth;
    }

    public void setSurfaceWidth(int surfaceWidth) {
        this.surfaceWidth = surfaceWidth;
    }

    public int getSurfaceHeight() {
        return surfaceHeight;
    }

    public void setSurfaceHeight(int surfaceHeight) {
        this.surfaceHeight = surfaceHeight;
    }

    public boolean isMirror() {
        return isMirror;
    }

    public void setMirror(boolean mirror) {
        isMirror = mirror;
    }

    public boolean isOpenGestureZoom() {
        return isOpenGestureZoom;
    }

    public void setOpenGestureZoom(boolean openGestureZoom) {
        isOpenGestureZoom = openGestureZoom;
    }

    @Override
    public String toString() {
        return "SkinParams{" +
                "isLanscape=" + isLanscape +
                ", videoWidth=" + videoWidth +
                ", videoHeight=" + videoHeight +
                ", cameraId=" + cameraId +
                ", videoBitrate=" + videoBitrate +
                ", isOnTouch=" + isOnTouch +
                ", isOnAnimation=" + isOnAnimation +
                ", foceView=" + foceView +
                ", updateLogFile=" + updateLogFile +
                ", title='" + title + '\'' +
                ", isVolumeGain=" + isVolumeGain +
                ", isResume=" + isResume +
                ", isFirstMachine=" + isFirstMachine +
                ", surfaceWidth=" + surfaceWidth +
                ", surfaceHeight=" + surfaceHeight +
                ", isMirror=" + isMirror +
                ", isOpenGestureZoom=" + isOpenGestureZoom +
                '}';
    }
}
