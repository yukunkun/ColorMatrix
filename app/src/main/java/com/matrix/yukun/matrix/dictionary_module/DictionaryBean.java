package com.matrix.yukun.matrix.dictionary_module;

import java.util.List;

/**
 * Created by Administrator on 2018/11/20.
 */

public class DictionaryBean {

    /**
     * zi : 聚
     * py : ju
     * wubi : bcti
     * pinyin : jù
     * bushou : 耳
     * jijie;
     * xiangjie
     * bihua : 14
     */

    private String zi;
    private String py;
    private String wubi;
    private String pinyin;
    private String bushou;
    private String bihua;
    private List<String> jijie;
    private List<String> xiangjie;

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getWubi() {
        return wubi;
    }

    public void setWubi(String wubi) {
        this.wubi = wubi;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getBushou() {
        return bushou;
    }

    public void setBushou(String bushou) {
        this.bushou = bushou;
    }

    public String getBihua() {
        return bihua;
    }

    public void setBihua(String bihua) {
        this.bihua = bihua;
    }

    public List<String> getJijie() {
        return jijie;
    }

    public void setJijie(List<String> jijie) {
        this.jijie = jijie;
    }

    public List<String> getXiangjie() {
        return xiangjie;
    }

    public void setXiangjie(List<String> xiangjie) {
        this.xiangjie = xiangjie;
    }

    @Override
    public String toString() {
        return "DictionaryBean{" +
                "zi='" + zi + '\'' +
                ", py='" + py + '\'' +
                ", wubi='" + wubi + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", bushou='" + bushou + '\'' +
                ", bihua='" + bihua + '\'' +
                ", jijie=" + jijie +
                ", xiangjie=" + xiangjie +
                '}';
    }
}
