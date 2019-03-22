package com.matrix.yukun.matrix.video_module.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class PlayAllBean extends DataSupport implements Serializable{

    /**
     * type : image
     * text : 猫会在乎铲屎官吗？
     * username : 知乎者也
     * uid : 22905335
     * header : http://wimg.spriteapp.cn/profile/20180810165141236540.png
     * comment : 83
     * top_commentsVoiceuri : null
     * top_commentsContent : 它等的是你背上的他它
     * top_commentsHeader : http://wx.qlogo.cn/mmopen/vi_32/r9tbp1yt51hAHryyAWUXSUhZdPw1PKNVbdFCG60q0KVY1l8cqgVe1nW53ytDc4JibpoSSWnfr2zWOATp0TKkd5Q/0
     * top_commentsName : 大神邢
     * passtime : 2018-10-12 08:26:02
     * soureid : 28699487
     * up : 581
     * down : 49
     * forward : 25
     * image : http://wimg.spriteapp.cn/ugc/2018/10/10/8beb747ecc3e11e890c0842b2b4c75ab_1.jpg
     * gif :
     * thumbnail : http://wimg.spriteapp.cn/ugc/2018/10/10/8beb747ecc3e11e890c0842b2b4c75ab_1.jpg
     * video :
     */

    private String type;
    private String text;
    private String username;
    private String uid;
    private String header;
    private int comment;
    private Object top_commentsVoiceuri;
    private String top_commentsContent;
    private String top_commentsHeader;
    private String top_commentsName;
    private String passtime;
    private int soureid;
    private int up;
    private int down;
    private int forward;
    private String image;
    private String gif;
    private String thumbnail;
    private String video;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public Object getTop_commentsVoiceuri() {
        return top_commentsVoiceuri;
    }

    public void setTop_commentsVoiceuri(Object top_commentsVoiceuri) {
        this.top_commentsVoiceuri = top_commentsVoiceuri;
    }

    public String getTop_commentsContent() {
        return top_commentsContent;
    }

    public void setTop_commentsContent(String top_commentsContent) {
        this.top_commentsContent = top_commentsContent;
    }

    public String getTop_commentsHeader() {
        return top_commentsHeader;
    }

    public void setTop_commentsHeader(String top_commentsHeader) {
        this.top_commentsHeader = top_commentsHeader;
    }

    public String getTop_commentsName() {
        return top_commentsName;
    }

    public void setTop_commentsName(String top_commentsName) {
        this.top_commentsName = top_commentsName;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    public int getSoureid() {
        return soureid;
    }

    public void setSoureid(int soureid) {
        this.soureid = soureid;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getForward() {
        return forward;
    }

    public void setForward(int forward) {
        this.forward = forward;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
