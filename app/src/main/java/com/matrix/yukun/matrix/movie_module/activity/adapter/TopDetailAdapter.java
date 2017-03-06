package com.matrix.yukun.matrix.movie_module.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.activity.TopMovieBean;
import com.matrix.yukun.matrix.movie_module.activity.TopPresent.TopPresent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yukun on 17-3-3.
 */
public class TopDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    TopPresent topPresent;
    ArrayList<TopMovieBean> topMovieBeen;
    private int selectPosition;

    public TopDetailAdapter(Context context, ArrayList<TopMovieBean> topMovieBeen, TopPresent topPresent) {
        this.context = context;
        this.topMovieBeen = topMovieBeen;
        this.topPresent=topPresent;
    }
    public void getSelectPosition(int selectPosition){
        this.selectPosition=selectPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(context).inflate(R.layout.top_detail,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Random random=new Random();
        ((Holder)holder).textViewName.setText(topMovieBeen.get(position).getName());
        Glide.with(context).load(topMovieBeen.get(position).getImage())
//                .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
//                .skipMemoryCache( true )//跳过内存缓存
//                .signature(new StringSignature(random.nextInt()+""))
                .into(((Holder)holder).imageViewCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnEventpos(position));
                selectPosition = position;
                topPresent.setWebUri(position);
            }
        });
        if(selectPosition==position){
            ((Holder)holder).textViewName.setTextColor(context.getResources().getColor(R.color.color_54fa3e));
        }else {
            ((Holder)holder).textViewName.setTextColor(context.getResources().getColor(R.color.color_whit));
        }
    }

    @Override
    public int getItemCount() {
        return topMovieBeen.size();
    }
    class Holder extends RecyclerView.ViewHolder{
        ImageView imageViewCover;
        TextView textViewName;
        public Holder(View itemView) {
            super(itemView);
            imageViewCover= (ImageView) itemView.findViewById(R.id.image_cover);
            textViewName=(TextView) itemView.findViewById(R.id.textview_name);
        }
    }


}
