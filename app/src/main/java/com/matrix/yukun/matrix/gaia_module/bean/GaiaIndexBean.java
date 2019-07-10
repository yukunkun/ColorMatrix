package com.matrix.yukun.matrix.gaia_module.bean;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class GaiaIndexBean {

    /**
     * flag : 0
     * keywords : 杭州,江干延时摄影,程方和程晓,杭州时之华,城市延时摄影,G20
     * nickName : 13806501080
     * have1080 : 1
     * have2k : 0
     * avatar : avatar/u4556.jpg
     * screenshot : screenshot/u4556/v11895.png
     * type : 2,4,6
     * commentCount : 4
     * cover :
     * duration : 138
     * playCount : 1649
     * have720 : 1
     * grade : 8.8
     * name : 江干交响诗
     * have4k : 0
     * inputKey : playlist/u4556/v11895
     * key : origin/u4556/v11895
     * likeCount : 16
     * wid : 10107
     * id : 8
     * playUrl : https://qv.gaiamount.com/playlist/u4556/v11895_720.m3u8
     */

    private int flag;
    private String keywords;
    private String nickName;
    private int have1080;
    private int have2k;
    private String avatar;
    private String screenshot;
    private String type;
    private int commentCount;
    private String cover;
    private int duration;
    private int playCount;
    private int have720;
    private double grade;
    private String name;
    private int have4k;
    private String inputKey;
    private String key;
    private int likeCount;
    private int wid;
    private int id;
    private String playUrl;
    /**
     * time : {"date":22,"day":6,"hours":19,"minutes":22,"month":3,"nanos":0,"seconds":29,"time":1492860149000,"timezoneOffset":-480,"year":117}
     */

    private TimeBean time;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHave1080() {
        return have1080;
    }

    public void setHave1080(int have1080) {
        this.have1080 = have1080;
    }

    public int getHave2k() {
        return have2k;
    }

    public void setHave2k(int have2k) {
        this.have2k = have2k;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getHave720() {
        return have720;
    }

    public void setHave720(int have720) {
        this.have720 = have720;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHave4k() {
        return have4k;
    }

    public void setHave4k(int have4k) {
        this.have4k = have4k;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public TimeBean getTime() {
        return time;
    }

    public void setTime(TimeBean time) {
        this.time = time;
    }

    public static class TimeBean {
        /**
         * date : 22
         * day : 6
         * hours : 19
         * minutes : 22
         * month : 3
         * nanos : 0
         * seconds : 29
         * time : 1492860149000
         * timezoneOffset : -480
         * year : 117
         */

        private int date;
        private int day;
        private int hours;
        private int minutes;
        private int month;
        private int nanos;
        private int seconds;
        private long time;
        private int timezoneOffset;
        private int year;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getNanos() {
            return nanos;
        }

        public void setNanos(int nanos) {
            this.nanos = nanos;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
