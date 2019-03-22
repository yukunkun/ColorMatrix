package com.matrix.yukun.matrix.chat_module.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class LeftTextHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewLeft;
    public ImageView mImageViewLeft;
    public TextView mTextViewLeftTime;
    public LeftTextHolder(View itemView) {
        super(itemView);
        mTextViewLeftTime= (TextView) itemView.findViewById(R.id.left_time);
        mTextViewLeft= (TextView) itemView.findViewById(R.id.tv_left_content);
        mImageViewLeft=(ImageView)itemView.findViewById(R.id.ci_left_head);
    }
}
