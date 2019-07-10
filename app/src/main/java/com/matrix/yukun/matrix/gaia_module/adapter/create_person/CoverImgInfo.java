package com.matrix.yukun.matrix.gaia_module.adapter.create_person;

/**
 * Created by yukun on 16-7-28.
 */
public class CoverImgInfo {

    /**
     * cover : null
     * id : 1
     * screenshot : null
     */

    private String cover;
    private int id;
    private String screenshot;
    private int type;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }
}
