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
public class RightTextHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewRight;
    public ImageView mImageViewRight;
    public TextView mTextViewRightTime;

    public RightTextHolder(View itemView) {
        super(itemView);
        mTextViewRightTime= (TextView) itemView.findViewById(R.id.rigth_time);
        mTextViewRight = (TextView) itemView.findViewById(R.id.tv_right_content);
        mImageViewRight = (ImageView) itemView.findViewById(R.id.ci_right_head);
    }
}
