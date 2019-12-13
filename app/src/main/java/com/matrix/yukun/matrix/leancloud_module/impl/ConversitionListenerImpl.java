package com.matrix.yukun.matrix.leancloud_module.impl;

import java.util.List;

import cn.leancloud.im.v2.AVIMConversation;

/**
 * author: kun .
 * date:   On 2019/12/13
 */
public interface ConversitionListenerImpl {
    void conversitionData(List<AVIMConversation> convs);
    void error(Exception e);
}
