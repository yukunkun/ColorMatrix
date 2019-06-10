package com.matrix.yukun.matrix.chat_module.entity;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public enum ChatType {

    IMAGE("image",0),
    TEXT("text",1),
    SHAKE("shake",2),
    FILE("file",3),
    VIDEO("video",4),
    UNKNOW("unknow",-1);

    private String name ;
    private int index ;

    ChatType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ChatType convert(int stateCode) {
        for (ChatType ec : ChatType.values()) {
            if (ec.index == stateCode) {
                return ec;
            }
        }
        return UNKNOW;
    }

}
