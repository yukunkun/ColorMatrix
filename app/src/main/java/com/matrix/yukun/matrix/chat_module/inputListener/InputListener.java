package com.matrix.yukun.matrix.chat_module.inputListener;

import com.matrix.yukun.matrix.chat_module.entity.Photo;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/5/10
 */
public interface InputListener {

    void onSendMessageClick(String msg);

    void onPictureClick(List<Photo> picPath);

    void onBottomMove(int position);

    void onShaked();
}
