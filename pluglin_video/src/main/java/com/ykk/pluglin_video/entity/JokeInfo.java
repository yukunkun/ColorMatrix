package com.ykk.pluglin_video.entity;

/**
 * Created by yukun on 17-11-20.
 */

public class JokeInfo {

    /**
     * content : 看到一句很好的名言：我们无法拉伸生命的长度，但是我们可以拓展生命的宽度。我觉得这句话太有道理了！意思就是：虽然我们无法再长高了，但是我们还可以继续长胖。
     * hashId : fd8e364a4c70d46e77c1610879748a9a
     * unixtime : 1418814837
     * updatetime : 2014-12-17 19:13:57
     */

    private String content;
    private String hashId;
    private long unixtime;
    private String updatetime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(long unixtime) {
        this.unixtime = unixtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "JokeInfo{" +
                "content='" + content + '\'' +
                ", hashId='" + hashId + '\'' +
                ", unixtime=" + unixtime +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
