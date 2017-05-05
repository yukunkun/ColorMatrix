package com.matrix.yukun.matrix.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.EventPos;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by yukun on 17-1-19.
 */
public class RecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    boolean check=false;
    private  ArrayList<Integer> integers= new ArrayList<>();
    private  ArrayList<String> names= new ArrayList<>();

    static int selectPosition;
    public RecAdapter(Context context,boolean check) {
        this.context = context;
        this.check=check;
        integers.add(R.mipmap.layer_3);
        integers.add(R.mipmap.layer_14);
        integers.add(R.mipmap.layer_12);
        integers.add(R.mipmap.layer_4);
        integers.add(R.mipmap.layer_11);
        integers.add(R.mipmap.layer_15);
        integers.add(R.mipmap.layer_6);
        integers.add(R.mipmap.layer_13);
        integers.add(R.mipmap.layer_9);
        integers.add(R.mipmap.layer_7);
        integers.add(R.mipmap.love);
        integers.add(R.mipmap.joke);
        integers.add(R.mipmap.juqing);

//        integers.add(R.mipmap.ad);
//        integers.add(R.mipmap.donghua);
//        integers.add(R.mipmap.journey);
//        for (int i = 0; i < 13; i++) {
//            integers.add(R.drawable.yuanjing);
//        }

        names.add("原图");
        names.add("陈旧片");
        names.add("底片色");
        names.add("黑白镜");
        names.add("红绿反转");
        names.add("红蓝反转");
        names.add("蓝绿反转");
        names.add("深蓝");
        names.add("魔绿");
        names.add("硒红");
        names.add("夕阳光");
        names.add("湛蓝");
        names.add("红霞");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rec_itrem,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
//            ((MyHolder) holder).imageView.setImageResource(integers.get(position));
            Glide.with(context).load(integers.get(position)).into(((MyHolder) holder).imageView);
            ((MyHolder) holder).textView.setText(names.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(selectPosition!=position){
                        selectPosition=position;
                        EventBus.getDefault().post(new EventPos(position));
                    }
                }
            });
        }

        if(selectPosition==position){
            ((MyHolder) holder).imageViewCheck.setVisibility(View.VISIBLE);
        }
        else {
            ((MyHolder) holder).imageViewCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return integers.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

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


}
