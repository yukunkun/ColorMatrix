package com.matrix.yukun.matrix.chat_module.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: kun .
 * date:   On 2019/5/20
 */
public class ShakeHolder extends RecyclerView.ViewHolder {
    public CircleImageView mCircleImageView;
    public TextView mTextView;
    public ShakeHolder(View itemView) {
        super(itemView);
        mCircleImageView=itemView.findViewById(R.id.ci_right_head);
        mTextView=itemView.findViewById(R.id.tv_right_content);
    }
}
