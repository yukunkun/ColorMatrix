package com.matrix.yukun.matrix.movie_module.adapter;

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
import com.matrix.yukun.matrix.movie_module.activity.TopDetailActivity;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 17-3-3.
 */
public class MovieTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Subjects> subjectsList;
    private ArrayList<String> dirAvatar;
    private ArrayList<String> actAvatar;


    public MovieTopAdapter(Context context, List<Subjects> subjectsList) {
        this.context=context;
        this.subjectsList=subjectsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.movie_item_top,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //评分
        ((MyHolder)holder).textViewRate.setText(subjectsList.get(position).getRating().getAverage()+"");
        ((MyHolder)holder).textViewName.setText(subjectsList.get(position).getTitle());
        //类型
        List<String> genres = subjectsList.get(position).getGenres();
        ((MyHolder)holder).textViewGenres.setText("类型:"+genres.toString());
        //导演
        String directors="";
        final ArrayList<Subjects.DirectorsBean> director = (ArrayList<Subjects.DirectorsBean>) subjectsList.get(position).getDirectors();
        for (int i = 0; i < director.size(); i++) {
            directors=directors+director.get(i).getName()+",";
        }
        ((MyHolder)holder).textViewDir.setText("导演:"+directors);

        //主演
        String actors="";
        final ArrayList<Subjects.CastsBean> casts = (ArrayList<Subjects.CastsBean>) subjectsList.get(position).getCasts();
        for (int i = 0; i < casts.size(); i++) {
            actors=actors+casts.get(i).getName()+",";
        }
        ((MyHolder)holder).textViewActor.setText("主演:"+actors);
        Glide.with(context).load(subjectsList.get(position).getImages().getLarge())/*.placeholder(R.mipmap.tool_icon)*/.into(((MyHolder)holder).imageViewCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subjectsList.size()==0){
                    return;
                }
                final ArrayList<Subjects.DirectorsBean> directorpos = (ArrayList<Subjects.DirectorsBean>) subjectsList.get(position).getDirectors();
                dirAvatar = new ArrayList<>();
                for (int i = 0; i < directorpos.size(); i++) {
                    dirAvatar.add(directorpos.get(i).getAvatars().getLarge());
                }
                final ArrayList<Subjects.CastsBean> castss = (ArrayList<Subjects.CastsBean>) subjectsList.get(position).getCasts();
                actAvatar = new ArrayList<>();
                for (int i = 0; i < castss.size(); i++) {
                    actAvatar.add(castss.get(i).getAvatars().getLarge());
                }
                Intent intent=new Intent(context, TopDetailActivity.class);
                intent.putParcelableArrayListExtra("director",directorpos);
                intent.putParcelableArrayListExtra("actor",castss);
                intent.putStringArrayListExtra("actAvatar",actAvatar);
                intent.putStringArrayListExtra("dirAvatar",dirAvatar);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
        ImageView imageViewCover;
        TextView textViewName,textViewGenres,textViewActor,textViewRate,textViewDir;
        public MyHolder(View itemView) {
            super(itemView);
            imageViewCover= (ImageView) itemView.findViewById(R.id.movie_cover);
            imageViewCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textViewName= (TextView) itemView.findViewById(R.id.movie_name);
            textViewGenres= (TextView) itemView.findViewById(R.id.movie_genres);
            textViewActor= (TextView) itemView.findViewById(R.id.movie_actor);
            textViewRate=(TextView) itemView.findViewById(R.id.movie_rating);
            textViewDir= (TextView) itemView.findViewById(R.id.movie_director);
        }
    }
}
