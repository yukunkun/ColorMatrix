package com.matrix.yukun.matrix.leshilive_module.present;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshilive_module.LeShiLiveActivity;
import com.matrix.yukun.matrix.leshilive_module.bean.LiveListBean;

import java.util.List;

/**
 * Created by yukun on 17-3-16.
 */
public class LiveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<LiveListBean>  subjectsList;


    public LiveListAdapter(Context context, List<LiveListBean> list) {
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
        final LiveListBean listBean = subjectsList.get(position);
        if(listBean.getCoverImgUrl()!=null){
            Glide.with(context).load(listBean.getCoverImgUrl())
                    .into(((MyHolder)holder).imageViewCover);
        }

        ((MyHolder)holder).textViewName.setText(listBean.getActivityName());
        ((MyHolder)holder).textViewContent.setText(listBean.getDescription());


//        ((MyHolder)holder).textViewTime.setText("时长:"+min+"`"+sec+"``");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LeShiLiveActivity.class);
                intent.putExtra("activity_id",listBean.getActivityId()+"");
                intent.putExtra("pos",position%10);
                intent.putExtra("activity_name",listBean.getActivityName());
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
