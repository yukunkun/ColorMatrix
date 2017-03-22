package com.matrix.yukun.matrix.leshi_module.present;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import java.util.List;

/**
 * Created by yukun on 17-3-16.
 */
public class LeShiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<ListBean>  subjectsList;


    public LeShiAdapter(Context context, List<ListBean> list) {
        this.context=(Activity) context;
        this.subjectsList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.leshi_item,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ListBean listBean = subjectsList.get(position);
        if(listBean.getInit_pic()!=null&&listBean.getInit_pic().length()>0){
            Glide.with(context).load(listBean.getInit_pic())
                    .into(((MyHolder)holder).imageViewCover);
        }else {
            Glide.with(context).load(listBean.getImg())
                    .into(((MyHolder)holder).imageViewCover);
        }

        ((MyHolder)holder).textViewContent.setText(listBean.getVideo_desc());
        ((MyHolder)holder).textViewName.setText(listBean.getVideo_name());

        long video_duration = listBean.getVideo_duration();
        int min=(int)video_duration/60;
        int sec=(int) video_duration%60;
        ((MyHolder)holder).textViewTime.setText("时长:"+min+"`"+sec+"``");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LeShiActivity.class);
                intent.putExtra("video_id",listBean.getVideo_id()+"");
                intent.putExtra("pos",position%10);
                intent.putExtra("title",listBean.getVideo_name());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions transitionActivityOptions = ActivityOptions
                            .makeSceneTransitionAnimation((Activity) context, ((MyHolder) holder).imageViewCover, "share");
                    context.startActivity(intent, transitionActivityOptions.toBundle());
                }else {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewCover;
        private TextView textViewContent,textViewTime,textViewName,textViewRate,textViewDir;
        public MyHolder(View itemView) {
            super(itemView);
            imageViewCover= (ImageView) itemView.findViewById(R.id.leshi_image);
            textViewTime= (TextView) itemView.findViewById(R.id.leshi_time);
            textViewContent= (TextView) itemView.findViewById(R.id.leshi_content);
            textViewName= (TextView) itemView.findViewById(R.id.leshi_name);
//            textViewActor= (TextView) itemView.findViewById(R.id.movie_actor);
//            textViewRate=(TextView) itemView.findViewById(R.id.movie_rating);
//            textViewDir= (TextView) itemView.findViewById(R.id.movie_director);
        }
    }
}
