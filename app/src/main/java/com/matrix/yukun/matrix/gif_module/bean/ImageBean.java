package com.matrix.yukun.matrix.gif_module.bean;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class ImageBean {
    private String path;
    private boolean isChecked;

    public ImageBean() {
    }

    public ImageBean(String path, boolean isChecked) {
        this.path = path;
        this.isChecked = isChecked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
