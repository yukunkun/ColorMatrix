package com.matrix.yukun.matrix.gaia_module.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author: kun .
 * date:   On 2019/8/14
 */
public class ItemBean implements MultiItemEntity {



    private int id;
    private String name;
    private Object alias;
    private String description;
    private String bgPicture;
    private String bgColor;
    private String headerImage;
    private int defaultAuthorId;
    private int itemType=0;

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

    public Object getAlias() {
        return alias;
    }

    public void setAlias(Object alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBgPicture() {
        return bgPicture;
    }

    public void setBgPicture(String bgPicture) {
        this.bgPicture = bgPicture;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public int getDefaultAuthorId() {
        return defaultAuthorId;
    }

    public void setDefaultAuthorId(int defaultAuthorId) {
        this.defaultAuthorId = defaultAuthorId;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

}
