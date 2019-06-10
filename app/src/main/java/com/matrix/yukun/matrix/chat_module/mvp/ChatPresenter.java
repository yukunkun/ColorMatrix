package com.matrix.yukun.matrix.chat_module.mvp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.ChatType;
import com.matrix.yukun.matrix.util.RecyclerViewUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtil;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2019/3/26
 */
public class ChatPresenter extends ChatControler.Presenter {
    private double firstTime;
    private double lastTime;
    private double timedevide = 8000;
    private int limit=20;
    private String chatUrl = "http://op.juhe.cn/robot/index";
    private String KEY = "12b5b1b14c7e1d25f18902728b9655b6";

    public ChatPresenter(Context context, ChatControler.View view) {
        super(context, view);
    }
    /**
     * 拼接消息
     * @param msg
     * @param type
     * @return
     */
    public ChatListInfo createTextChatInfo(String msg,int type,boolean isReceive){
        ChatListInfo chatListInfo = new ChatListInfo();
        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setTimeStamp(System.currentTimeMillis());
        chatListInfo.setMsgTime(System.currentTimeMillis());
        chatListInfo.setChatInfo(msg);
        chatListInfo.setTypeSn(type);
        chatListInfo.setReceive(isReceive);
        chatListInfo.setMsgType(ChatType.TEXT.getName());
        //保存到数据库
        chatListInfo.save();
        return chatListInfo;
    }

    public ChatListInfo createImageChatInfo(String imagePath,int type,boolean isReceive){
        ChatListInfo chatListInfo = new ChatListInfo();
        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setTimeStamp(System.currentTimeMillis());
        chatListInfo.setMsgTime(System.currentTimeMillis());
        chatListInfo.setImagePath(imagePath);
        chatListInfo.setTypeSn(type);
        chatListInfo.setReceive(isReceive);
        chatListInfo.setMsgType(ChatType.IMAGE.getName());
        //保存到数据库
        chatListInfo.save();
        return chatListInfo;
    }

    /**
     * 调用接口
     * @param info
     */
    public void sendRoboteMessage(String info){
        NetworkUtils.networkGet(chatUrl)
                .addParams("info", info)
                .addParams("key", KEY)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mView.getRoboteMessage("不想和你聊天了");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.optJSONObject("result");
                    String text = result.optString("text");
                    mView.getRoboteMessage(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<ChatListInfo> loadHistoryMsg(int type,int skip){
        List<ChatListInfo> chatListInfos = DataSupport.where("typeSn = ?",type+"").order("timeStamp desc").limit(limit).offset(skip).find(ChatListInfo.class);
        if(chatListInfos.size()>0) {
            Collections.reverse(chatListInfos);
        }
        return chatListInfos;
    }

    public ChatListInfo sendShakeListener(int type){
        ChatListInfo chatListInfo = new ChatListInfo();
        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setTimeStamp(System.currentTimeMillis());
        chatListInfo.setMsgTime(System.currentTimeMillis());
        chatListInfo.setTypeSn(type);
        chatListInfo.setReceive(false);
        chatListInfo.setMsgType(ChatType.SHAKE.getName());
        //保存到数据库
        chatListInfo.save();
        return chatListInfo;
    }

    public ChatListInfo createVideoChatInfo(String videoPath,int type,boolean isReceive){
        ChatListInfo chatListInfo = new ChatListInfo();
        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setTimeStamp(System.currentTimeMillis());
        chatListInfo.setMsgTime(System.currentTimeMillis());
        chatListInfo.setVideoPath(videoPath);
        chatListInfo.setTypeSn(type);
        chatListInfo.setReceive(isReceive);
        chatListInfo.setMsgType(ChatType.VIDEO.getName());
        //保存到数据库
        chatListInfo.save();
        return chatListInfo;
    }

    public ChatListInfo createFileChatInfo(String filePath,int type,boolean isReceive){
        ChatListInfo chatListInfo = new ChatListInfo();
        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setTimeStamp(System.currentTimeMillis());
        chatListInfo.setMsgTime(System.currentTimeMillis());
        chatListInfo.setFilePath(filePath);
        chatListInfo.setTypeSn(type);
        chatListInfo.setReceive(isReceive);
        chatListInfo.setMsgType(ChatType.FILE.getName());
        //保存到数据库
        chatListInfo.save();
        return chatListInfo;
    }
}
