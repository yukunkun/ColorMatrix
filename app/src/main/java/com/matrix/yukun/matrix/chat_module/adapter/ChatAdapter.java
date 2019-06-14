package com.matrix.yukun.matrix.chat_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.ChatType;
import com.matrix.yukun.matrix.chat_module.holder.LeftTextHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightFileHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightImageHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightTextHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightVideoHolder;
import com.matrix.yukun.matrix.chat_module.holder.ShakeHolder;
import com.matrix.yukun.matrix.chat_module.holderwrapper.LeftTextHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.RightFileHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.RightImageHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.RightTextHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.RightVideoHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.ShakeHolderWrapper;

import java.util.List;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ChatListInfo> mChatInfos;

    public ChatAdapter(Context context, List<ChatListInfo> chatInfos) {
        mContext = context;
        mChatInfos = chatInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType== ChatMesageType.LEFTCONTENT.getValue()){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_item,null);
            return new LeftTextHolder(view);
        }else if(viewType==ChatMesageType.RIGHTCONTENT.getValue()){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_item,null);
            return new RightTextHolder(view);
        }else if(viewType==ChatMesageType.RIGHTIMAGE.getValue()){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_image_item,null);
            return new RightImageHolder(view);
        } else if(viewType==ChatMesageType.SHAKEWINDOW.getValue()){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_shake_layout,null);
            return new ShakeHolder(view);
        }  else if(viewType==ChatMesageType.VIDEOMESSAGE.getValue()){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_video_item,null);
            return new RightVideoHolder(view);
        } else if(viewType==ChatMesageType.FILEMESSAGE.getValue()){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_file_item,null);
            return new RightFileHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ChatListInfo chatListInfo = mChatInfos.get(position);
        if(holder instanceof LeftTextHolder){
            LeftTextHolderWrapper.getInstance().content(mContext,chatListInfo, (LeftTextHolder) holder);
        }else if(holder instanceof RightTextHolder){
            //右边文本的布局
            RightTextHolderWrapper.getInstance().content(mContext,chatListInfo, (RightTextHolder) holder);
        }
        else if(holder instanceof RightImageHolder){
            //右边文本的布局
            RightImageHolderWrapper.getInstance().content(mContext,chatListInfo, (RightImageHolder) holder);
        }
        else if(holder instanceof ShakeHolder){
            //抖一抖
            ShakeHolderWrapper.getInstance().content(mContext,chatListInfo, (ShakeHolder) holder);
        }
        else if(holder instanceof RightFileHolder){
            //文件
            RightFileHolderWrapper.getInstance().content(mContext,chatListInfo, (RightFileHolder) holder);
        }
        else if(holder instanceof RightVideoHolder){
            //视频
            RightVideoHolderWrapper.getInstance().content(mContext,chatListInfo, (RightVideoHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return mChatInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatInfos.get(position).isReceive()){// 接收的
            if(mChatInfos.get(position).getMsgType().equals(ChatType.TEXT.getName())){
                return ChatMesageType.LEFTCONTENT.getValue(); //文本
            }
        }else { //发送端
            if(mChatInfos.get(position).getMsgType().equals(ChatType.TEXT.getName())){
                return ChatMesageType.RIGHTCONTENT.getValue();//文本
            }
            if(mChatInfos.get(position).getMsgType().equals(ChatType.IMAGE.getName())){
                return ChatMesageType.RIGHTIMAGE.getValue(); //图片
            }
            if(mChatInfos.get(position).getMsgType().equals(ChatType.SHAKE.getName())){
                return ChatMesageType.SHAKEWINDOW.getValue(); //抖一抖
            }
            if(mChatInfos.get(position).getMsgType().equals(ChatType.FILE.getName())){
                return ChatMesageType.FILEMESSAGE.getValue(); //文件
            }
            if(mChatInfos.get(position).getMsgType().equals(ChatType.VIDEO.getName())){
                return ChatMesageType.VIDEOMESSAGE.getValue(); //文件
            }
        }
        return position;
    }
}
