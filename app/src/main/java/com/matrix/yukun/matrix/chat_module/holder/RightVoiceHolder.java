package com.matrix.yukun.matrix.chat_module.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.emoji.CubeEmoticonTextView;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class RightVoiceHolder extends RecyclerView.ViewHolder {
    public CubeEmoticonTextView mTextViewRight;
    public ImageView mImageViewRight,mIvPlay;
    public TextView mTextViewRightTime,tvSenond;
    public SeekBar mSeekBar;

    public RightVoiceHolder(View itemView) {
        super(itemView);
        mTextViewRightTime= (TextView) itemView.findViewById(R.id.rigth_time);
        mImageViewRight = (ImageView) itemView.findViewById(R.id.ci_right_head);
        tvSenond=itemView.findViewById(R.id.tv_second);
        mSeekBar=itemView.findViewById(R.id.seekbar);
        mIvPlay=itemView.findViewById(R.id.iv_play);
    }
}
