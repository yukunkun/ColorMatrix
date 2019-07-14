package com.matrix.yukun.matrix.gaia_module.bean;

import java.io.Serializable;

/**
 * Created by yukun on 16-9-30.
 */
public class MaterialDetailInfo implements Serializable {


    /**
     * fid : 22
     * colorist :
     * keywords :
     * have1080 : 0
     * have2K : 0
     * mid : 22
     * cutter :
     * type : 12,10,
     * lens : 安琴
     * scene : 水下
     * duration : 0
     * shareCount : 0
     * price720 : 0
     * priceOriginal : 0
     * bitRate :
     * price4K : 0
     * price1080 : 0
     * price2K : 0
     * photographer : 汪士卿
     * height : 0
     * address :
     * director :
     * nickName : 你好世界
     * format :
     * fps : 0
     * isCollect : 0
     * avatar : avatar/u1688/1466747698611.png
     * userId : 1688
     * is4K : 0
     * playCount : 6
     * size : 0
     * createTime : {"date":10,"day":4,"hours":14,"minutes":51,"month":8,"nanos":0,"seconds":40,"time":1441867900000,"timezoneOffset":-480,"year":115}
     * have720 : 0
     * machine : Kiny
     * name : IMG_8694.mov
     * width : 0
     * have4K : 0
     * inputKey :
     * downloadCount : 0
     */

    private MaterialBean material;

    //加密用的时间
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public AudioBean getAudio() {
        return audio;
    }
    public void setAudio(AudioBean audio) {
        this.audio = audio;
    }

    /**
     * fid : 22
     * mp4 : http://7xpmfz.com5.z0.glb.clouddn.com/_mp4.m3u8?e=1475631536&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:hqXJkWINIFLf7ZwniefnuWV0lvA=
     */



    private ResourceBean resource;
    /**
     * focal :
     * stream :
     * screenshot : screenshot/u49/v1315.png
     * width : 1920
     * fps : 25/1
     * codec : h264
     * format : mov,mp4,m4a,3gp,3g2,mj2
     * size : 237755995
     * id : 21
     * duration : 93
     * height : 1080
     * avinfo : null
     */

    private VideoBean video;


    private AudioBean audio;

    public MaterialBean getMaterial() {
        return material;
    }

    public void setMaterial(MaterialBean material) {
        this.material = material;
    }

    public ResourceBean getResource() {
        return resource;
    }

    public void setResource(ResourceBean resource) {
        this.resource = resource;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public static class MaterialBean implements Serializable {
        private int fid;
        private String colorist;
        private String keywords;
        private int have1080;
        private int have2K;
        private int mid;
        private String cutter;
        private String type;
        private String lens;
        private String scene;
        private int duration;
        private int shareCount;
        private double price720;
        private double priceOriginal;
        private String bitRate;
        private double price4K;
        private double price1080;
        private double price2K;
        private String photographer;
        private int height;
        private String address;
        private String director;
        private String nickName;
        private String format;
        private String fps;
        private int isCollect;
        private String avatar;
        private int userId;
        private int is4K;
        private int playCount;
        private int size;
        private int isVip;
        private int vipLevel;
        private int fansCount;
        private int createCount;
        private int works_collectCount;
        private String description;
        private String fileFormat;
        private String software;
        private String template;
        private String screenshot;

        public void setPrice2K(double price2K) {
            this.price2K = price2K;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getSoftware() {
            return software;
        }

        public void setSoftware(String software) {
            this.software = software;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getFileFormat() {
            return fileFormat;
        }

        public void setFileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getWorks_collectCount() {
            return works_collectCount;
        }

        public void setWorks_collectCount(int works_collectCount) {
            this.works_collectCount = works_collectCount;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public int getCreateCount() {
            return createCount;
        }

        public void setCreateCount(int createCount) {
            this.createCount = createCount;
        }

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

        /**
         * date : 10
         * day : 4
         * hours : 14
         * minutes : 51
         * month : 8
         * nanos : 0
         * seconds : 40
         * time : 1441867900000
         * timezoneOffset : -480
         * year : 115
         */

        private CreateTimeBean createTime;
        private int have720;
        private String machine;
        private String name;
        private int width;
        private int have4K;
        private String inputKey;
        private int downloadCount;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public String getColorist() {
            return colorist;
        }

        public void setColorist(String colorist) {
            this.colorist = colorist;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
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

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getCutter() {
            return cutter;
        }

        public void setCutter(String cutter) {
            this.cutter = cutter;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLens() {
            return lens;
        }

        public void setLens(String lens) {
            this.lens = lens;
        }

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
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

        public String getBitRate() {
            return bitRate;
        }

        public void setBitRate(String bitRate) {
            this.bitRate = bitRate;
        }

        public double getPrice4K() {
            return price4K;
        }

        public void setPrice4K(double price4K) {
            this.price4K = price4K;
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

        public void setPrice2K(long price2K) {
            this.price2K = price2K;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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

        public int getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(int isCollect) {
            this.isCollect = isCollect;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getIs4K() {
            return is4K;
        }

        public void setIs4K(int is4K) {
            this.is4K = is4K;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public CreateTimeBean getCreateTime() {
            return createTime;
        }

        public void setCreateTime(CreateTimeBean createTime) {
            this.createTime = createTime;
        }

        public int getHave720() {
            return have720;
        }

        public void setHave720(int have720) {
            this.have720 = have720;
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

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHave4K() {
            return have4K;
        }

        public void setHave4K(int have4K) {
            this.have4K = have4K;
        }

        public String getInputKey() {
            return inputKey;
        }

        public void setInputKey(String inputKey) {
            this.inputKey = inputKey;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        public static class CreateTimeBean implements Serializable {
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


    public static class VideoBean implements Serializable {
        /**
         * encoder_des :
         * duration : 69.880000
         * height : 720
         * refs : 4
         * width : 1280
         * colorSampling : 420p
         * colorSpace : yuv
         * display_aspect_ratio : 16:9
         * bits_per_raw_sample : 8
         * avg_frame_rate : 25/1
         * bit_rate : 1774529
         * videoSize : 1.552712875E7
         * encoder : H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10
         */

        private String encoder_des;
        private String duration;
        private int height;
        private int refs;
        private int width;
        private String colorSampling;
        private String colorSpace;
        private String display_aspect_ratio;
        private String bits_per_raw_sample;
        private String avg_frame_rate;
        private String bit_rate;
        private double videoSize;
        private String encoder;

        public String getEncoder_des() {
            return encoder_des;
        }

        public void setEncoder_des(String encoder_des) {
            this.encoder_des = encoder_des;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getRefs() {
            return refs;
        }

        public void setRefs(int refs) {
            this.refs = refs;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getColorSampling() {
            return colorSampling;
        }

        public void setColorSampling(String colorSampling) {
            this.colorSampling = colorSampling;
        }

        public String getColorSpace() {
            return colorSpace;
        }

        public void setColorSpace(String colorSpace) {
            this.colorSpace = colorSpace;
        }

        public String getDisplay_aspect_ratio() {
            return display_aspect_ratio;
        }

        public void setDisplay_aspect_ratio(String display_aspect_ratio) {
            this.display_aspect_ratio = display_aspect_ratio;
        }

        public String getBits_per_raw_sample() {
            return bits_per_raw_sample;
        }

        public void setBits_per_raw_sample(String bits_per_raw_sample) {
            this.bits_per_raw_sample = bits_per_raw_sample;
        }

        public String getAvg_frame_rate() {
            return avg_frame_rate;
        }

        public void setAvg_frame_rate(String avg_frame_rate) {
            this.avg_frame_rate = avg_frame_rate;
        }

        public String getBit_rate() {
            return bit_rate;
        }

        public void setBit_rate(String bit_rate) {
            this.bit_rate = bit_rate;
        }

        public double getVideoSize() {
            return videoSize;
        }

        public void setVideoSize(double videoSize) {
            this.videoSize = videoSize;
        }

        public String getEncoder() {
            return encoder;
        }

        @Override
        public String toString() {
            return "VideoBean{" +
                    "encoder_des='" + encoder_des + '\'' +
                    ", duration='" + duration + '\'' +
                    ", height=" + height +
                    ", refs=" + refs +
                    ", width=" + width +
                    ", colorSampling='" + colorSampling + '\'' +
                    ", colorSpace='" + colorSpace + '\'' +
                    ", display_aspect_ratio='" + display_aspect_ratio + '\'' +
                    ", bits_per_raw_sample='" + bits_per_raw_sample + '\'' +
                    ", avg_frame_rate='" + avg_frame_rate + '\'' +
                    ", bit_rate='" + bit_rate + '\'' +
                    ", videoSize=" + videoSize +
                    ", encoder='" + encoder + '\'' +
                    '}';
        }

        public void setEncoder(String encoder) {
            this.encoder = encoder;
        }
    }

    public static class AudioBean implements Serializable {


        /**
         * audioSize : 1118845
         * channels : 2
         * codec_name : aac
         * code_model :  (Advanced Audio Coding)
         * sample_rate : 48000
         * codec_long_name : AAC
         * bit_rate : 127868
         */

        private double audioSize;
        private int channels;
        private String codec_name;
        private String code_model;
        private String sample_rate;
        private String codec_long_name;
        private String bit_rate;

        public double getAudioSize() {
            return audioSize;
        }

        public void setAudioSize(double audioSize) {
            this.audioSize = audioSize;
        }

        public int getChannels() {
            return channels;
        }

        public void setChannels(int channels) {
            this.channels = channels;
        }

        public String getCodec_name() {
            return codec_name;
        }

        public void setCodec_name(String codec_name) {
            this.codec_name = codec_name;
        }

        public String getCode_model() {
            return code_model;
        }

        public void setCode_model(String code_model) {
            this.code_model = code_model;
        }

        public String getSample_rate() {
            return sample_rate;
        }

        public void setSample_rate(String sample_rate) {
            this.sample_rate = sample_rate;
        }

        public String getCodec_long_name() {
            return codec_long_name;
        }

        public void setCodec_long_name(String codec_long_name) {
            this.codec_long_name = codec_long_name;
        }

        public String getBit_rate() {
            return bit_rate;
        }

        public void setBit_rate(String bit_rate) {
            this.bit_rate = bit_rate;
        }

        @Override
        public String toString() {
            return "AudioBean{" +
                    "audioSize=" + audioSize +
                    ", channels=" + channels +
                    ", codec_name='" + codec_name + '\'' +
                    ", code_model='" + code_model + '\'' +
                    ", sample_rate='" + sample_rate + '\'' +
                    ", codec_long_name='" + codec_long_name + '\'' +
                    ", bit_rate='" + bit_rate + '\'' +
                    '}';
        }
    }
}
