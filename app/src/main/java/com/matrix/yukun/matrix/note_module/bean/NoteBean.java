package com.matrix.yukun.matrix.note_module.bean;

/**
 * author: kun .
 * date:   On 2019/6/14
 */
public class NoteBean {
    private String title;
    private String name;
    private String filePath;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NoteBean{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
