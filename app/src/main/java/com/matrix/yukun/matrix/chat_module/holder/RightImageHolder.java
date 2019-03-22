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
public class RightImageHolder extends RecyclerView.ViewHolder {
    public ImageView mIvRight;
    public ImageView mImageViewRight;
    public TextView mTextViewRightTime;

    public RightImageHolder(View itemView) {
        super(itemView);
        mTextViewRightTime= (TextView) itemView.findViewById(R.id.rigth_time);
        mIvRight = (ImageView) itemView.findViewById(R.id.iv_right_image);
        mImageViewRight = (ImageView) itemView.findViewById(R.id.ci_right_head);
    }
}
