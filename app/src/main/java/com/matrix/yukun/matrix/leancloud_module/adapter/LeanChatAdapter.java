package com.matrix.yukun.matrix.leancloud_module.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.chatholder.ImageHolder;
import com.matrix.yukun.matrix.leancloud_module.adapter.chatholder.TextHolder;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/12/26
 */
public class LeanChatAdapter extends BaseMultiItemQuickAdapter<LeanChatMessage,BaseViewHolder> {

    private Context mContext;
    public LeanChatAdapter(List<LeanChatMessage> data, Context context) {
        super(data);
        mContext=context;
        addItemType(0, R.layout.item_lean_chat);//图片
        addItemType(1,R.layout.item_lean_chat);//文本
    }

    @Override
    protected void convert(BaseViewHolder helper, LeanChatMessage item) {
        int itemViewType = helper.getItemViewType();
        if(itemViewType==1){
            new TextHolder(mContext,item,this,helper.getView(R.id.ll));
        }else if(itemViewType==0){
            new ImageHolder(mContext,item,this,helper.getView(R.id.ll));
        }
    }
}
