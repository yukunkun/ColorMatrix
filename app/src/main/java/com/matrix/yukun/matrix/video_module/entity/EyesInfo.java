package com.matrix.yukun.matrix.video_module.entity;

import com.qq.e.ads.nativ.NativeExpressADView;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beck on 2018/4/14.
 */

public class EyesInfo extends DataSupport implements Serializable{

    /**
     * type : video
     * data : {"dataType":"VideoBeanForClient","id":92503,"title":"别赖床啦，让它们来唤醒你的味蕾","description":"继上次分享了来自吉卜力动画中的美味后，这个周末，让这些来自全世界的经典动画美食来满足你的大胃口！别赖床了，一起去吃好吃的吧！From The Royal Ocean Film Society","library":"DAILY","tags":[{"id":14,"name":"动画","actionUrl":"eyepetizer://tag/14/?title=%E5%8A%A8%E7%94%BB","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/c4e5c0f76d21abbd23c9626af3c9f481.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/88da8548d31005032c37df4889d2104c.jpeg?imageMogr2/quality/100","tagRecType":"IMPORTANT"},{"id":20,"name":"开胃","actionUrl":"eyepetizer://tag/20/?title=%E5%BC%80%E8%83%83","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/afffaebe827656b0bb24e534ab35275c.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/afffaebe827656b0bb24e534ab35275c.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL"},{"id":36,"name":"集锦","actionUrl":"eyepetizer://tag/36/?title=%E9%9B%86%E9%94%A6","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/ec07f44858caa7c4d8b309cde5500a84.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/ec07f44858caa7c4d8b309cde5500a84.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL"}],"consumption":{"collectionCount":167,"shareCount":179,"replyCount":1},"resourceType":"video","slogan":"周末也要好好吃饭哦！","provider":{"name":"YouTube","alias":"youtube","icon":"http://img.kaiyanapp.com/fa20228bc5b921e837156923a58713f6.png"},"category":"集锦","author":{"id":2175,"icon":"http://img.kaiyanapp.com/d7186edff72b6a6ddd03eff166ee4cd8.jpeg","name":"开眼集锦精选","description":"最好的部分 + 有化学反应的混剪","link":"","latestReleaseTime":1523667600000,"videoNum":182,"adTrack":null,"follow":{"itemType":"author","itemId":2175,"followed":false},"shield":{"itemType":"author","itemId":2175,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true},"cover":{"feed":"http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/879690534854b650e65442011522e502.png?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":"http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"},"playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=default&source=aliyun","thumbPlayUrl":null,"duration":199,"webUrl":{"raw":"http://www.eyepetizer.net/detail.html?vid=92503","forWeibo":"http://www.eyepetizer.net/detail.html?vid=92503"},"releaseTime":1523667600000,"playInfo":[{"height":480,"width":854,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun","size":14894308},{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=qcloud","size":14894308},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=ucloud","size":14894308}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun"},{"height":720,"width":1280,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=aliyun","size":26345642},{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=qcloud","size":26345642},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=ucloud","size":26345642}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=aliyun"}],"campaign":null,"waterMarks":null,"adTrack":null,"type":"NORMAL","titlePgc":null,"descriptionPgc":null,"remark":"别赖床了！这么多好吃的在等你呀","ifLimitVideo":false,"idx":0,"shareAdTrack":null,"favoriteAdTrack":null,"webAdTrack":null,"date":1523667600000,"promotion":null,"label":null,"labelList":[],"descriptionEditor":"继上次分享了来自吉卜力动画中的美味后，这个周末，让这些来自全世界的经典动画美食来满足你的大胃口！别赖床了，一起去吃好吃的吧！From The Royal Ocean Film Society","collected":false,"played":false,"subtitles":[],"lastViewTime":null,"playlists":null,"src":null}
     * tag : 0
     * id : 0
     * adIndex : -1
     */

    private String type;
    private DataBean data;
    private String tag;
    private int id;
    private int adIndex;
    private String cover;
    private String description;
    private String slogan;
    private String icon;
    private String category;
    private int advType;
    private NativeExpressADView mNativeExpressADView;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdIndex() {
        return adIndex;
    }

    public void setAdIndex(int adIndex) {
        this.adIndex = adIndex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getAdvType() {
        return advType;
    }

    public NativeExpressADView getNativeExpressADView() {
        return mNativeExpressADView;
    }

    public void setNativeExpressADView(NativeExpressADView nativeExpressADView) {
        mNativeExpressADView = nativeExpressADView;
    }

    public void setAdvType(int advType) {
        this.advType = advType;
    }

    public static class DataBean extends DataSupport implements Serializable{
        /**
         * dataType : VideoBeanForClient
         * id : 92503
         * title : 别赖床啦，让它们来唤醒你的味蕾
         * description : 继上次分享了来自吉卜力动画中的美味后，这个周末，让这些来自全世界的经典动画美食来满足你的大胃口！别赖床了，一起去吃好吃的吧！From The Royal Ocean Film Society
         * library : DAILY
         * tags : [{"id":14,"name":"动画","actionUrl":"eyepetizer://tag/14/?title=%E5%8A%A8%E7%94%BB","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/c4e5c0f76d21abbd23c9626af3c9f481.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/88da8548d31005032c37df4889d2104c.jpeg?imageMogr2/quality/100","tagRecType":"IMPORTANT"},{"id":20,"name":"开胃","actionUrl":"eyepetizer://tag/20/?title=%E5%BC%80%E8%83%83","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/afffaebe827656b0bb24e534ab35275c.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/afffaebe827656b0bb24e534ab35275c.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL"},{"id":36,"name":"集锦","actionUrl":"eyepetizer://tag/36/?title=%E9%9B%86%E9%94%A6","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/ec07f44858caa7c4d8b309cde5500a84.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/ec07f44858caa7c4d8b309cde5500a84.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL"}]
         * consumption : {"collectionCount":167,"shareCount":179,"replyCount":1}
         * resourceType : video
         * slogan : 周末也要好好吃饭哦！
         * provider : {"name":"YouTube","alias":"youtube","icon":"http://img.kaiyanapp.com/fa20228bc5b921e837156923a58713f6.png"}
         * category : 集锦
         * author : {"id":2175,"icon":"http://img.kaiyanapp.com/d7186edff72b6a6ddd03eff166ee4cd8.jpeg","name":"开眼集锦精选","description":"最好的部分 + 有化学反应的混剪","link":"","latestReleaseTime":1523667600000,"videoNum":182,"adTrack":null,"follow":{"itemType":"author","itemId":2175,"followed":false},"shield":{"itemType":"author","itemId":2175,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true}
         * cover : {"feed":"http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/879690534854b650e65442011522e502.png?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":"http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"}
         * playUrl : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=default&source=aliyun
         * thumbPlayUrl : null
         * duration : 199
         * webUrl : {"raw":"http://www.eyepetizer.net/detail.html?vid=92503","forWeibo":"http://www.eyepetizer.net/detail.html?vid=92503"}
         * releaseTime : 1523667600000
         * playInfo : [{"height":480,"width":854,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun","size":14894308},{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=qcloud","size":14894308},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=ucloud","size":14894308}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun"},{"height":720,"width":1280,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=aliyun","size":26345642},{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=qcloud","size":26345642},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=ucloud","size":26345642}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=high&source=aliyun"}]
         * campaign : null
         * waterMarks : null
         * adTrack : null
         * type : NORMAL
         * titlePgc : null
         * descriptionPgc : null
         * remark : 别赖床了！这么多好吃的在等你呀
         * ifLimitVideo : false
         * idx : 0
         * shareAdTrack : null
         * favoriteAdTrack : null
         * webAdTrack : null
         * date : 1523667600000
         * promotion : null
         * label : null
         * labelList : []
         * descriptionEditor : 继上次分享了来自吉卜力动画中的美味后，这个周末，让这些来自全世界的经典动画美食来满足你的大胃口！别赖床了，一起去吃好吃的吧！From The Royal Ocean Film Society
         * collected : false
         * played : false
         * subtitles : []
         * lastViewTime : null
         * playlists : null
         * src : null
         */

        private String dataType;
        private int id;
        private String title;
        private String description;
        private String library;
        private ConsumptionBean consumption;
        private String resourceType;
        private String slogan;
        private ProviderBean provider;
        private String category;
        private AuthorBean author;
        private CoverBean cover;
        private String playUrl;
        private Object thumbPlayUrl;
        private int duration;
        private WebUrlBean webUrl;
        private long releaseTime;
        private Object campaign;
        private Object waterMarks;
        private Object adTrack;
        private String type;
        private Object titlePgc;
        private Object descriptionPgc;
        private String remark;
        private boolean ifLimitVideo;
        private int idx;
        private Object shareAdTrack;
        private Object favoriteAdTrack;
        private Object webAdTrack;
        private long date;
        private Object promotion;
        private Object label;
        private String descriptionEditor;
        private boolean collected;
        private boolean played;
        private Object lastViewTime;
        private Object playlists;
        private Object src;
        private List<TagsBean> tags;
        private List<PlayInfoBean> playInfo;
        private List<?> labelList;
        private List<?> subtitles;

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLibrary() {
            return library;
        }

        public void setLibrary(String library) {
            this.library = library;
        }

        public ConsumptionBean getConsumption() {
            return consumption;
        }

        public void setConsumption(ConsumptionBean consumption) {
            this.consumption = consumption;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
        }

        public ProviderBean getProvider() {
            return provider;
        }

        public void setProvider(ProviderBean provider) {
            this.provider = provider;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public CoverBean getCover() {
            return cover;
        }

        public void setCover(CoverBean cover) {
            this.cover = cover;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public Object getThumbPlayUrl() {
            return thumbPlayUrl;
        }

        public void setThumbPlayUrl(Object thumbPlayUrl) {
            this.thumbPlayUrl = thumbPlayUrl;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public WebUrlBean getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(WebUrlBean webUrl) {
            this.webUrl = webUrl;
        }

        public long getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(long releaseTime) {
            this.releaseTime = releaseTime;
        }

        public Object getCampaign() {
            return campaign;
        }

        public void setCampaign(Object campaign) {
            this.campaign = campaign;
        }

        public Object getWaterMarks() {
            return waterMarks;
        }

        public void setWaterMarks(Object waterMarks) {
            this.waterMarks = waterMarks;
        }

        public Object getAdTrack() {
            return adTrack;
        }

        public void setAdTrack(Object adTrack) {
            this.adTrack = adTrack;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getTitlePgc() {
            return titlePgc;
        }

        public void setTitlePgc(Object titlePgc) {
            this.titlePgc = titlePgc;
        }

        public Object getDescriptionPgc() {
            return descriptionPgc;
        }

        public void setDescriptionPgc(Object descriptionPgc) {
            this.descriptionPgc = descriptionPgc;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public boolean isIfLimitVideo() {
            return ifLimitVideo;
        }

        public void setIfLimitVideo(boolean ifLimitVideo) {
            this.ifLimitVideo = ifLimitVideo;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public Object getShareAdTrack() {
            return shareAdTrack;
        }

        public void setShareAdTrack(Object shareAdTrack) {
            this.shareAdTrack = shareAdTrack;
        }

        public Object getFavoriteAdTrack() {
            return favoriteAdTrack;
        }

        public void setFavoriteAdTrack(Object favoriteAdTrack) {
            this.favoriteAdTrack = favoriteAdTrack;
        }

        public Object getWebAdTrack() {
            return webAdTrack;
        }

        public void setWebAdTrack(Object webAdTrack) {
            this.webAdTrack = webAdTrack;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public Object getPromotion() {
            return promotion;
        }

        public void setPromotion(Object promotion) {
            this.promotion = promotion;
        }

        public Object getLabel() {
            return label;
        }

        public void setLabel(Object label) {
            this.label = label;
        }

        public String getDescriptionEditor() {
            return descriptionEditor;
        }

        public void setDescriptionEditor(String descriptionEditor) {
            this.descriptionEditor = descriptionEditor;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }

        public boolean isPlayed() {
            return played;
        }

        public void setPlayed(boolean played) {
            this.played = played;
        }

        public Object getLastViewTime() {
            return lastViewTime;
        }

        public void setLastViewTime(Object lastViewTime) {
            this.lastViewTime = lastViewTime;
        }

        public Object getPlaylists() {
            return playlists;
        }

        public void setPlaylists(Object playlists) {
            this.playlists = playlists;
        }

        public Object getSrc() {
            return src;
        }

        public void setSrc(Object src) {
            this.src = src;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<PlayInfoBean> getPlayInfo() {
            return playInfo;
        }

        public void setPlayInfo(List<PlayInfoBean> playInfo) {
            this.playInfo = playInfo;
        }

        public List<?> getLabelList() {
            return labelList;
        }

        public void setLabelList(List<?> labelList) {
            this.labelList = labelList;
        }

        public List<?> getSubtitles() {
            return subtitles;
        }

        public void setSubtitles(List<?> subtitles) {
            this.subtitles = subtitles;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "dataType='" + dataType + '\'' +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", library='" + library + '\'' +
                    ", consumption=" + consumption +
                    ", resourceType='" + resourceType + '\'' +
                    ", slogan='" + slogan + '\'' +
                    ", provider=" + provider +
                    ", category='" + category + '\'' +
                    ", author=" + author +
                    ", cover=" + cover +
                    ", playUrl='" + playUrl + '\'' +
                    ", thumbPlayUrl=" + thumbPlayUrl +
                    ", duration=" + duration +
                    ", webUrl=" + webUrl +
                    ", releaseTime=" + releaseTime +
                    ", campaign=" + campaign +
                    ", waterMarks=" + waterMarks +
                    ", adTrack=" + adTrack +
                    ", type='" + type + '\'' +
                    ", titlePgc=" + titlePgc +
                    ", descriptionPgc=" + descriptionPgc +
                    ", remark='" + remark + '\'' +
                    ", ifLimitVideo=" + ifLimitVideo +
                    ", idx=" + idx +
                    ", shareAdTrack=" + shareAdTrack +
                    ", favoriteAdTrack=" + favoriteAdTrack +
                    ", webAdTrack=" + webAdTrack +
                    ", date=" + date +
                    ", promotion=" + promotion +
                    ", label=" + label +
                    ", descriptionEditor='" + descriptionEditor + '\'' +
                    ", collected=" + collected +
                    ", played=" + played +
                    ", lastViewTime=" + lastViewTime +
                    ", playlists=" + playlists +
                    ", src=" + src +
                    ", tags=" + tags +
                    ", playInfo=" + playInfo +
                    ", labelList=" + labelList +
                    ", subtitles=" + subtitles +
                    '}';
        }

        public static class ConsumptionBean implements Serializable{
            /**
             * collectionCount : 167
             * shareCount : 179
             * replyCount : 1
             */

            private int collectionCount;
            private int shareCount;
            private int replyCount;

            public int getCollectionCount() {
                return collectionCount;
            }

            public void setCollectionCount(int collectionCount) {
                this.collectionCount = collectionCount;
            }

            public int getShareCount() {
                return shareCount;
            }

            public void setShareCount(int shareCount) {
                this.shareCount = shareCount;
            }

            public int getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(int replyCount) {
                this.replyCount = replyCount;
            }
        }

        public static class ProviderBean implements Serializable{
            /**
             * name : YouTube
             * alias : youtube
             * icon : http://img.kaiyanapp.com/fa20228bc5b921e837156923a58713f6.png
             */

            private String name;
            private String alias;
            private String icon;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }

        public static class AuthorBean implements Serializable{
            /**
             * id : 2175
             * icon : http://img.kaiyanapp.com/d7186edff72b6a6ddd03eff166ee4cd8.jpeg
             * name : 开眼集锦精选
             * description : 最好的部分 + 有化学反应的混剪
             * link :
             * latestReleaseTime : 1523667600000
             * videoNum : 182
             * adTrack : null
             * follow : {"itemType":"author","itemId":2175,"followed":false}
             * shield : {"itemType":"author","itemId":2175,"shielded":false}
             * approvedNotReadyVideoCount : 0
             * ifPgc : true
             */

            private int id;
            private String icon;
            private String name;
            private String description;
            private String link;
            private long latestReleaseTime;
            private int videoNum;
            private Object adTrack;
            private FollowBean follow;
            private ShieldBean shield;
            private int approvedNotReadyVideoCount;
            private boolean ifPgc;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public long getLatestReleaseTime() {
                return latestReleaseTime;
            }

            public void setLatestReleaseTime(long latestReleaseTime) {
                this.latestReleaseTime = latestReleaseTime;
            }

            public int getVideoNum() {
                return videoNum;
            }

            public void setVideoNum(int videoNum) {
                this.videoNum = videoNum;
            }

            public Object getAdTrack() {
                return adTrack;
            }

            public void setAdTrack(Object adTrack) {
                this.adTrack = adTrack;
            }

            public FollowBean getFollow() {
                return follow;
            }

            public void setFollow(FollowBean follow) {
                this.follow = follow;
            }

            public ShieldBean getShield() {
                return shield;
            }

            public void setShield(ShieldBean shield) {
                this.shield = shield;
            }

            public int getApprovedNotReadyVideoCount() {
                return approvedNotReadyVideoCount;
            }

            public void setApprovedNotReadyVideoCount(int approvedNotReadyVideoCount) {
                this.approvedNotReadyVideoCount = approvedNotReadyVideoCount;
            }

            public boolean isIfPgc() {
                return ifPgc;
            }

            public void setIfPgc(boolean ifPgc) {
                this.ifPgc = ifPgc;
            }

            public static class FollowBean implements Serializable{
                /**
                 * itemType : author
                 * itemId : 2175
                 * followed : false
                 */

                private String itemType;
                private int itemId;
                private boolean followed;

                public String getItemType() {
                    return itemType;
                }

                public void setItemType(String itemType) {
                    this.itemType = itemType;
                }

                public int getItemId() {
                    return itemId;
                }

                public void setItemId(int itemId) {
                    this.itemId = itemId;
                }

                public boolean isFollowed() {
                    return followed;
                }

                public void setFollowed(boolean followed) {
                    this.followed = followed;
                }
            }

            public static class ShieldBean implements Serializable{
                /**
                 * itemType : author
                 * itemId : 2175
                 * shielded : false
                 */

                private String itemType;
                private int itemId;
                private boolean shielded;

                public String getItemType() {
                    return itemType;
                }

                public void setItemType(String itemType) {
                    this.itemType = itemType;
                }

                public int getItemId() {
                    return itemId;
                }

                public void setItemId(int itemId) {
                    this.itemId = itemId;
                }

                public boolean isShielded() {
                    return shielded;
                }

                public void setShielded(boolean shielded) {
                    this.shielded = shielded;
                }
            }
        }

        public static class CoverBean implements Serializable{
            /**
             * feed : http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageMogr2/quality/60/format/jpg
             * detail : http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageMogr2/quality/60/format/jpg
             * blurred : http://img.kaiyanapp.com/879690534854b650e65442011522e502.png?imageMogr2/quality/60/format/jpg
             * sharing : null
             * homepage : http://img.kaiyanapp.com/70d81486df0618abfd492bb5d51082b2.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim
             */

            private String feed;
            private String detail;
            private String blurred;
            private Object sharing;
            private String homepage;

            public String getFeed() {
                return feed;
            }

            public void setFeed(String feed) {
                this.feed = feed;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getBlurred() {
                return blurred;
            }

            public void setBlurred(String blurred) {
                this.blurred = blurred;
            }

            public Object getSharing() {
                return sharing;
            }

            public void setSharing(Object sharing) {
                this.sharing = sharing;
            }

            public String getHomepage() {
                return homepage;
            }

            public void setHomepage(String homepage) {
                this.homepage = homepage;
            }
        }

        public static class WebUrlBean implements Serializable{
            /**
             * raw : http://www.eyepetizer.net/detail.html?vid=92503
             * forWeibo : http://www.eyepetizer.net/detail.html?vid=92503
             */

            private String raw;
            private String forWeibo;

            public String getRaw() {
                return raw;
            }

            public void setRaw(String raw) {
                this.raw = raw;
            }

            public String getForWeibo() {
                return forWeibo;
            }

            public void setForWeibo(String forWeibo) {
                this.forWeibo = forWeibo;
            }
        }

        public static class TagsBean implements Serializable{
            /**
             * id : 14
             * name : 动画
             * actionUrl : eyepetizer://tag/14/?title=%E5%8A%A8%E7%94%BB
             * adTrack : null
             * desc : null
             * bgPicture : http://img.kaiyanapp.com/c4e5c0f76d21abbd23c9626af3c9f481.jpeg?imageMogr2/quality/100
             * headerImage : http://img.kaiyanapp.com/88da8548d31005032c37df4889d2104c.jpeg?imageMogr2/quality/100
             * tagRecType : IMPORTANT
             */

            private int id;
            private String name;
            private String actionUrl;
            private Object adTrack;
            private String desc;
            private String bgPicture;
            private String headerImage;
            private String tagRecType;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getActionUrl() {
                return actionUrl;
            }

            public void setActionUrl(String actionUrl) {
                this.actionUrl = actionUrl;
            }

            public Object getAdTrack() {
                return adTrack;
            }

            public void setAdTrack(Object adTrack) {
                this.adTrack = adTrack;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getBgPicture() {
                return bgPicture;
            }

            public void setBgPicture(String bgPicture) {
                this.bgPicture = bgPicture;
            }

            public String getHeaderImage() {
                return headerImage;
            }

            public void setHeaderImage(String headerImage) {
                this.headerImage = headerImage;
            }

            public String getTagRecType() {
                return tagRecType;
            }

            public void setTagRecType(String tagRecType) {
                this.tagRecType = tagRecType;
            }
        }

        public static class PlayInfoBean implements Serializable{
            /**
             * height : 480
             * width : 854
             * urlList : [{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun","size":14894308},{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=qcloud","size":14894308},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=ucloud","size":14894308}]
             * name : 标清
             * type : normal
             * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun
             */

            private int height;
            private int width;
            private String name;
            private String type;
            private String url;
            private List<UrlListBean> urlList;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<UrlListBean> getUrlList() {
                return urlList;
            }

            public void setUrlList(List<UrlListBean> urlList) {
                this.urlList = urlList;
            }

            public static class UrlListBean implements Serializable{
                /**
                 * name : aliyun
                 * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=92503&editionType=normal&source=aliyun
                 * size : 14894308
                 */

                private String name;
                private String url;
                private int size;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }
            }
        }
    }
}
