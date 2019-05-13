package com.matrix.yukun.matrix.chat_module.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * author: kun .
 * date:   On 2019/5/13
 */
public class Photo {
    private static final String  TAG = "Photo";
    public               String  name;//图片名称
    public               String  path;//图片全路径
    public               String  type;//图片类型
    public               int     width;//图片宽度
    public               int     height;//图片高度
    public               long    size;//图片文件大小，单位：Bytes
    public               long    time;//图片拍摄的时间戳,单位：毫秒
    public               int     duration; // 视频时长
    public               boolean selected;//是否被选中,内部使用,无需关心
    public               boolean selectedOriginal;//用户选择时是否选择了原图选项
    private              Uri     uri;

    public Photo(String name, String path, long time, int width, int height, long size, String type) {
        this.name = name;
        this.path = path;
        this.time = time;
        this.width = width;
        this.height = height;
        this.type = type;
        this.size = size;
        this.selected = false;
        this.selectedOriginal = false;
    }

    public Photo(String name, String path, String type, int width, int height, long size, long time, boolean selected, boolean selectedOriginal) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.width = width;
        this.height = height;
        this.size = size;
        this.time = time;
        this.selected = selected;
        this.selectedOriginal = selectedOriginal;
    }

    public Photo() {
    }

    @Override
    public boolean equals(Object o) {
        try {
            Photo other = (Photo) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            Log.e(TAG, "equals: " + Log.getStackTraceString(e));
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "Photo{" + "name='" + name + '\'' + ", path='" + path + '\'' + ", type='" + type + '\'' + ", width=" + width + ", height=" + height + ", size=" + size + "}";
    }

    protected Photo(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.type = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.size = in.readLong();
        this.time = in.readLong();
        this.selected = in.readByte() != 0;
        this.selectedOriginal = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectedOriginal() {
        return selectedOriginal;
    }

    public void setSelectedOriginal(boolean selectedOriginal) {
        this.selectedOriginal = selectedOriginal;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
