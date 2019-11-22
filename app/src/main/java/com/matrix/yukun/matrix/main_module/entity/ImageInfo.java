package com.matrix.yukun.matrix.main_module.entity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Beck on 2018/4/21.
 */

public class ImageInfo extends DataSupport {

    /**
     * startdate : 20191121
     * fullstartdate : 201911211600
     * enddate : 20191122
     * url : /th?id=OHR.SaltireClouds_ZH-CN0002027700_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
     * urlbase : /th?id=OHR.SaltireClouds_ZH-CN0002027700
     * copyright : Chon湖上空的低空云，苏格兰特罗萨克斯   (© Alistair Dick/Alamy)
     * copyrightlink : https://www.bing.com/search?q=%E4%BD%8E%E7%A9%BA%E4%BA%91&form=hpcapt&mkt=zh-cn
     * title :
     * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20191121_SaltireClouds%22&FORM=HPQUIZ
     * wp : true
     * hsh : 1b3e4bf970977dca21c4e53d5ab71bea
     * drk : 1
     * top : 1
     * bot : 1
     * hs : ["1.2.3"]
     */

    private String startdate;
    private String fullstartdate;
    private String enddate;
    private String url;
    private String urlbase;
    private String copyright;
    private String copyrightlink;
    private String title;
    private String quiz;
    private boolean wp;
    private String hsh;
    private int drk;
    private int top;
    private int bot;
    private List<String> hs;

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getFullstartdate() {
        return fullstartdate;
    }

    public void setFullstartdate(String fullstartdate) {
        this.fullstartdate = fullstartdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlbase() {
        return urlbase;
    }

    public void setUrlbase(String urlbase) {
        this.urlbase = urlbase;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyrightlink() {
        return copyrightlink;
    }

    public void setCopyrightlink(String copyrightlink) {
        this.copyrightlink = copyrightlink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public boolean isWp() {
        return wp;
    }

    public void setWp(boolean wp) {
        this.wp = wp;
    }

    public String getHsh() {
        return hsh;
    }

    public void setHsh(String hsh) {
        this.hsh = hsh;
    }

    public int getDrk() {
        return drk;
    }

    public void setDrk(int drk) {
        this.drk = drk;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBot() {
        return bot;
    }

    public void setBot(int bot) {
        this.bot = bot;
    }

    public List<String> getHs() {
        return hs;
    }

    public void setHs(List<String> hs) {
        this.hs = hs;
    }
}
