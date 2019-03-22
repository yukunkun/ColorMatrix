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
import com.matrix.yukun.matrix.chat_module.holder.RightImageHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightTextHolder;
import com.matrix.yukun.matrix.chat_module.holderwrapper.LeftTextHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.RightImageHolderWrapper;
import com.matrix.yukun.matrix.chat_module.holderwrapper.RightTextHolderWrapper;
import java.util.List;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ChatListInfo> mChatInfos;
    private int LEFTCONTENT=0;
    private int RIGHTCONTENT=1;
    private int RIGHTIMAGE=3;

    public ChatAdapter(Context context, List<ChatListInfo> chatInfos) {
        mContext = context;
        mChatInfos = chatInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==LEFTCONTENT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_item,null);
            return new LeftTextHolder(view);
        }else if(viewType==RIGHTCONTENT){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_item,null);
            return new RightTextHolder(view);
        }else if(viewType==RIGHTIMAGE){
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_image_item,null);
            return new RightImageHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return mChatInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatInfos.get(position).isReceive()){// 接收的
            if(mChatInfos.get(position).getMsgType().equals(ChatType.TEXT.getName())){
                return LEFTCONTENT; //文本
            }
        }else { //发送端
            if(mChatInfos.get(position).getMsgType().equals(ChatType.TEXT.getName())){
                return RIGHTCONTENT;//文本
            }
            if(mChatInfos.get(position).getMsgType().equals(ChatType.IMAGE.getName())){
                return RIGHTIMAGE; //图片
            }
        }
        return position;
    }
}
