package com.matrix.yukun.matrix.video_module.entity;

import java.util.List;

/**
 * author: kun .
 * date:   On 2018/9/14
 */
public class NewsInfo {

    /**
     * liveInfo : null
     * tcount : 11
     * picInfo : [{"ref":null,"width":null,"url":"http://cms-bucket.nosdn.127.net/2018/09/14/fc3ade213fae4b72817fefb7276b7a38.jpeg","height":null}]
     * docid : DRMCUG6D00097U7R
     * videoInfo : null
     * channel : tech
     * link : https://3g.163.com/all/article/DRMCUG6D00097U7R.html
     * source : 网易科技报道
     * title : 易读|滴滴9月15日起恢复深夜出行服务
     * type : doc
     * imgsrcFrom : null
     * imgsrc3gtype : 1
     * unlikeReason : 重复、旧闻/6,内容质量差/6
     * digest : 网易科技讯9月14日消息，滴滴方面表示，9月15日起将按原计
     * typeid :
     * addata : null
     * tag :
     * category : 科技
     * ptime : 2018-09-14 18:03:28
     */

    private Object liveInfo;
    private int tcount;
    private String docid;
    private Object videoInfo;
    private String channel;
    private String link;
    private String source;
    private String title;
    private String type;
    private Object imgsrcFrom;
    private int imgsrc3gtype;
    private String unlikeReason;
    private String digest;
    private String typeid;
    private Object addata;
    private String tag;
    private String category;
    private String ptime;
    private List<PicInfoBean> picInfo;

    public Object getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(Object liveInfo) {
        this.liveInfo = liveInfo;
    }

    public int getTcount() {
        return tcount;
    }

    public void setTcount(int tcount) {
        this.tcount = tcount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public Object getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(Object videoInfo) {
        this.videoInfo = videoInfo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getImgsrcFrom() {
        return imgsrcFrom;
    }

    public void setImgsrcFrom(Object imgsrcFrom) {
        this.imgsrcFrom = imgsrcFrom;
    }

    public int getImgsrc3gtype() {
        return imgsrc3gtype;
    }

    public void setImgsrc3gtype(int imgsrc3gtype) {
        this.imgsrc3gtype = imgsrc3gtype;
    }

    public String getUnlikeReason() {
        return unlikeReason;
    }

    public void setUnlikeReason(String unlikeReason) {
        this.unlikeReason = unlikeReason;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public Object getAddata() {
        return addata;
    }

    public void setAddata(Object addata) {
        this.addata = addata;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public List<PicInfoBean> getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(List<PicInfoBean> picInfo) {
        this.picInfo = picInfo;
    }

    public static class PicInfoBean {
        /**
         * ref : null
         * width : null
         * url : http://cms-bucket.nosdn.127.net/2018/09/14/fc3ade213fae4b72817fefb7276b7a38.jpeg
         * height : null
         */

        private Object ref;
        private Object width;
        private String url;
        private Object height;

        public Object getRef() {
            return ref;
        }

        public void setRef(Object ref) {
            this.ref = ref;
        }

        public Object getWidth() {
            return width;
        }

        public void setWidth(Object width) {
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Object getHeight() {
            return height;
        }

        public void setHeight(Object height) {
            this.height = height;
        }
    }
}
