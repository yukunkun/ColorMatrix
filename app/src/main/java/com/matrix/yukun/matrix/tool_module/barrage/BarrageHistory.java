package com.matrix.yukun.matrix.tool_module.barrage;

import org.litepal.crud.DataSupport;

/**
 * author: kun .
 * date:   On 2018/12/6
 */
public class BarrageHistory extends DataSupport {
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
