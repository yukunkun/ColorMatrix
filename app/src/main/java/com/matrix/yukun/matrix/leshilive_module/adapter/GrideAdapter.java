package com.matrix.yukun.matrix.leshilive_module.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.haozhang.lib.SlantedTextView;
import com.matrix.yukun.matrix.R;

import java.util.ArrayList;

/**
 * Created by yukun on 17-3-31.
 */
public class GrideAdapter extends BaseAdapter {
    private int secPos;
    private ArrayList<Integer> integers;
    private Context context;
    private final String[] strings;

    public GrideAdapter(Context context,ArrayList<Integer> integers) {
        this.integers = integers;
        this.context = context;
        strings = context.getResources().getStringArray(R.array.gift);
    }

    public void setPos(int pos){
        secPos=pos;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return integers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View inflate, ViewGroup parent) {
        inflate = LayoutInflater.from(context).inflate(R.layout.gride_gift_item, null);
        ImageView imageViewGift= (ImageView) inflate.findViewById(R.id.gift);
        LinearLayout linCon= (LinearLayout) inflate.findViewById(R.id.gift_con);
        TextView textView=(TextView)inflate.findViewById(R.id.gift_name);
        SlantedTextView textViewTag=(SlantedTextView)inflate.findViewById(R.id.textviewtag);

        Glide.with(context).load(integers.get(position)).into(imageViewGift);
        textView.setText(strings[position]);
        if(position==secPos){
            linCon.setBackground(context.getResources().getDrawable(R.drawable.giftback));
            textView.setTextColor(context.getResources().getColor(R.color.color_44fc2c));
        }else {
            linCon.setBackground(context.getResources().getDrawable(R.drawable.giftbackclose));
            textView.setTextColor(context.getResources().getColor(R.color.color_whit));
        }
        if(position<11){
            textViewTag.setText("nor");
            textViewTag.setSlantedBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }else if(position<16){
            textViewTag.setText("vip");
            textViewTag.setSlantedBackgroundColor(context.getResources().getColor(R.color.color_0064f9));
        }else if(position<20){
            textViewTag.setText("svip");
            textViewTag.setSlantedBackgroundColor(context.getResources().getColor(R.color.color_d400f9));
        }
        return inflate;
    }
}
