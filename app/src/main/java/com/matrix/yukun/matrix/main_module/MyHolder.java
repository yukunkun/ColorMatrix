package com.matrix.yukun.matrix.main_module;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

/**
 * Created by yukun on 17-5-4.
 */
public class MyHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public final TextView textView;
    public final ImageView imageViewCheck;

    public MyHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        textView = (TextView) itemView.findViewById(R.id.text);
        imageViewCheck = (ImageView) itemView.findViewById(R.id.checked);

    }
}
