package com.matrix.yukun.matrix.chat_module.adapter;

/**
 * author: kun .
 * date:   On 2019/5/20
 */
public enum ChatMesageType {
    LEFTCONTENT("LEFTCONTENT",1), //左边的文本
    RIGHTCONTENT("RIGHTCONTENT",2), //右边的文本
    RIGHTIMAGE("RIGHTIMAGE",3), //图片
    SHAKEWINDOW("SHAKEWINDOW",4), //抖一抖
    FILEMESSAGE("FILEMESSAGE",5); //文件


    private String type;
    private int value;

    ChatMesageType(String type, int value){
        this.type=type;
        this.value=value;
    }

    public int getValue(){
        return value;
    }

    public String getType() {
        return type;
    }
}
