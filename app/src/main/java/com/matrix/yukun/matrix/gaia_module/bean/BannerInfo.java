package com.matrix.yukun.matrix.gaia_module.bean;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class BannerInfo {

    /**
     * id : 2
     * shuffl : smallFile/4/15451245964183545.jpg
     * type : 0
     * url : https://48.gaiamount.com/exhibit-list/7/1
     */

    private int id;
    private String shuffl;
    private int type;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShuffl() {
        return shuffl;
    }

    public void setShuffl(String shuffl) {
        this.shuffl = shuffl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
