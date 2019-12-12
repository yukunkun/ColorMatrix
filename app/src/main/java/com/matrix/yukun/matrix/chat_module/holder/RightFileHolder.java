package com.matrix.yukun.matrix.chat_module.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class RightFileHolder extends RecyclerView.ViewHolder {
    public ImageView mImageViewRight;
    public TextView mTextViewRightTime, mTvFileName, mTvFileSize;
    public RelativeLayout mLayout;

    public RightFileHolder(View itemView) {
        super(itemView);
        mTextViewRightTime = (TextView) itemView.findViewById(R.id.rigth_time);
        mImageViewRight = (ImageView) itemView.findViewById(R.id.ci_right_head);
        mTvFileName = itemView.findViewById(R.id.tv_name);
        mTvFileSize = itemView.findViewById(R.id.tv_size);
        mLayout = itemView.findViewById(R.id.rl_con);
    }
}
