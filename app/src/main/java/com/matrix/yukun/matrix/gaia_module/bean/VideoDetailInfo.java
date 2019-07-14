package com.matrix.yukun.matrix.gaia_module.bean;

import java.io.Serializable;

public class VideoDetailInfo implements Serializable {

    private int likeCount;
    private int creationCount;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCreationCount() {
        return creationCount;
    }

    public void setCreationCount(int creationCount) {
        this.creationCount = creationCount;
    }

    /**

     * nickName : 米粒微电影
     * avatar : avatar/u895.jpg
     */

    private UserBean User;
    /**
     * commentCount : 0
     * downloadCount : 0
     * embedCount : 0
     * gradeCount : 0
     * likeCount : 0
     * playCount : 8
     * shareCount : 0
     */

    private WorksPropertiesBean WorksProperties;
    /**
     * fid : 1808
     * mp4 : http://7xpmfz.com5.z0.glb.clouddn.com/playlist/u895/v2030_mp4.m3u8?pm3u8/0/deadline/1461826911&e=1461826911&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:ADgEdstMJvc-Ti6i3nOvIJ4n85Q=
     */

    private ResourceBean resource;
    /**
     * allowCharge : 0
     * allowDownload : 0
     * allowEmbed : 0
     * requirePassword : 0
     */

    private WorksPermissionBean WorksPermission;
    /**
     * bitRate : 19087909
     * codec : mpeg2video
     * coder :
     * duration : 263
     * focal :
     * format : mpeg
     * fps : 25/1
     * height : 720
     * screenshot : original/u895/v2030_screen.png
     * size : 626751488
     * width : 1800
     */

    private VideoInfoBean VideoInfo;
    /**
     * address :
     * colorist :
     * cover :
     * createTime : 2015-12-17 10:43:07
     * cutter :
     * description :
     * director :
     * grade : 0
     * have1080 : 1
     * have2K : 0
     * have4K : 0
     * have720 : 0
     * imageCount : 0
     * is4K : 0
     * isOfficial : 0
     * keywords :
     * lens :
     * machine :
     * name : 龙岩中宝德化自驾游
     * photographer :
     * price1080 : 0
     * price2K : 0
     * price4K : 0
     * price720 : 0
     * priceOriginal : 0
     * priority : 0
     * remark :
     * scene :
     * textCount : 0
     * type : 12,
     * id : 895
     * videoInfoId : 1808
     * watermarkId : 0
     */

    private WorksBean Works;

    public UserBean getUser() {
        return User;
    }

    public void setUser(UserBean User) {
        this.User = User;
    }

    public WorksPropertiesBean getWorksProperties() {
        return WorksProperties;
    }

    public void setWorksProperties(WorksPropertiesBean WorksProperties) {
        this.WorksProperties = WorksProperties;
    }

    public ResourceBean getResource() {
        return resource;
    }

    public void setResource(ResourceBean resource) {
        this.resource = resource;
    }

    public WorksPermissionBean getWorksPermission() {
        return WorksPermission;
    }

    public void setWorksPermission(WorksPermissionBean WorksPermission) {
        this.WorksPermission = WorksPermission;
    }

    public VideoInfoBean getVideoInfo() {
        return VideoInfo;
    }

    public void setVideoInfo(VideoInfoBean VideoInfo) {
        this.VideoInfo = VideoInfo;
    }

    public WorksBean getWorks() {
        return Works;
    }

    public void setWorks(WorksBean Works) {
        this.Works = Works;
    }

    public static class UserBean implements Serializable {
        private long uid;
        private String nickName;
        private String avatar;
        private int isVip;
        private int vipLevel;

        public int getIsVip() {
            return isVip;
        }

        public void setIsVip(int isVip) {
            this.isVip = isVip;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class WorksPropertiesBean implements Serializable {
        private int commentCount;
        private int downloadCount;
        private int embedCount;
        private int gradeCount;
        private int likeCount;
        private int playCount;
        private int shareCount;
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        public int getEmbedCount() {
            return embedCount;
        }

        public void setEmbedCount(int embedCount) {
            this.embedCount = embedCount;
        }

        public int getGradeCount() {
            return gradeCount;
        }

        public void setGradeCount(int gradeCount) {
            this.gradeCount = gradeCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }
    }

    public static class ResourceBean implements Serializable {
        private int fid;
        private String mp4;
        private String k4;
        private String k2;
        private String p1080;
        private String p720;


        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }

        public String getK4() {
            return k4;
        }

        public void setK4(String k4) {
            this.k4 = k4;
        }

        public String getK2() {
            return k2;
        }

        public void setK2(String k2) {
            this.k2 = k2;
        }

        public String getP1080() {
            return p1080;
        }

        public void setP1080(String p1080) {
            this.p1080 = p1080;
        }

        public String getP720() {
            return p720;
        }

        public void setP720(String p720) {
            this.p720 = p720;
        }
    }

    public static class WorksPermissionBean implements Serializable {
        private int allowCharge;
        private int allowDownload;
        private int allowEmbed;
        private int requirePassword;

        public int getAllowCharge() {
            return allowCharge;
        }

        public void setAllowCharge(int allowCharge) {
            this.allowCharge = allowCharge;
        }

        public int getAllowDownload() {
            return allowDownload;
        }

        public void setAllowDownload(int allowDownload) {
            this.allowDownload = allowDownload;
        }

        public int getAllowEmbed() {
            return allowEmbed;
        }

        public void setAllowEmbed(int allowEmbed) {
            this.allowEmbed = allowEmbed;
        }

        public int getRequirePassword() {
            return requirePassword;
        }

        public void setRequirePassword(int requirePassword) {
            this.requirePassword = requirePassword;
        }
    }

    public static class VideoInfoBean implements Serializable {
        private String bitRate;
        private String codec;
        private String coder;
        private int duration;
        private String focal;
        private String format;
        private String fps;
        private int height;
        private String screenshot;
        private long size;
        private int width;

        public String getBitRate() {
            return bitRate;
        }

        public void setBitRate(String bitRate) {
            this.bitRate = bitRate;
        }

        public String getCodec() {
            return codec;
        }

        public void setCodec(String codec) {
            this.codec = codec;
        }

        public String getCoder() {
            return coder;
        }

        public void setCoder(String coder) {
            this.coder = coder;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getFocal() {
            return focal;
        }

        public void setFocal(String focal) {
            this.focal = focal;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getFps() {
            return fps;
        }

        public void setFps(String fps) {
            this.fps = fps;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    public static class WorksBean implements Serializable {
        private String address;
        private String colorist;
        private String cover;
        private String createTime;
        private String cutter;
        private String description;
        private String director;
        private double grade;
        private int have1080;
        private int have2K;
        private int have4K;
        private int have720;
        private int imageCount;
        private int is4K;
        private int isOfficial;
        private String keywords;
        private String lens;
        private String machine;
        private String name;
        private String photographer;
        private double price1080;
        private double price2K;
        private double price4K;
        private double price720;
        private double priceOriginal;
        private int priority;
        private String remark;
        private String scene;
        private int textCount;
        private String type;
        private long userId;
        private long videoInfoId;
        private long watermarkId;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getColorist() {
            return colorist;
        }

        public void setColorist(String colorist) {
            this.colorist = colorist;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCutter() {
            return cutter;
        }

        public void setCutter(String cutter) {
            this.cutter = cutter;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public double getGrade() {
            return grade;
        }

        public void setGrade(double grade) {
            this.grade = grade;
        }

        public int getHave1080() {
            return have1080;
        }

        public void setHave1080(int have1080) {
            this.have1080 = have1080;
        }

        public int getHave2K() {
            return have2K;
        }

        public void setHave2K(int have2K) {
            this.have2K = have2K;
        }

        public int getHave4K() {
            return have4K;
        }

        public void setHave4K(int have4K) {
            this.have4K = have4K;
        }

        public int getHave720() {
            return have720;
        }

        public void setHave720(int have720) {
            this.have720 = have720;
        }

        public int getImageCount() {
            return imageCount;
        }

        public void setImageCount(int imageCount) {
            this.imageCount = imageCount;
        }

        public int getIs4K() {
            return is4K;
        }

        public void setIs4K(int is4K) {
            this.is4K = is4K;
        }

        public int getIsOfficial() {
            return isOfficial;
        }

        public void setIsOfficial(int isOfficial) {
            this.isOfficial = isOfficial;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getLens() {
            return lens;
        }

        public void setLens(String lens) {
            this.lens = lens;
        }

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public double getPrice1080() {
            return price1080;
        }

        public void setPrice1080(double price1080) {
            this.price1080 = price1080;
        }

        public double getPrice2K() {
            return price2K;
        }

        public void setPrice2K(double price2K) {
            this.price2K = price2K;
        }

        public double getPrice4K() {
            return price4K;
        }

        public void setPrice4K(double price4K) {
            this.price4K = price4K;
        }

        public double getPrice720() {
            return price720;
        }

        public void setPrice720(double price720) {
            this.price720 = price720;
        }

        public double getPriceOriginal() {
            return priceOriginal;
        }

        public void setPriceOriginal(double priceOriginal) {
            this.priceOriginal = priceOriginal;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        public int getTextCount() {
            return textCount;
        }

        public void setTextCount(int textCount) {
            this.textCount = textCount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getVideoInfoId() {
            return videoInfoId;
        }

        public void setVideoInfoId(long videoInfoId) {
            this.videoInfoId = videoInfoId;
        }

        public long getWatermarkId() {
            return watermarkId;
        }

        public void setWatermarkId(long watermarkId) {
            this.watermarkId = watermarkId;
        }
    }
}