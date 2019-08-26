package com.matrix.yukun.matrix.main_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.activity.JokeDetailActivity;
import com.matrix.yukun.matrix.main_module.entity.JokeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class JokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<JokeInfo> jokeInfoList;
    List<Integer> mListHead=new ArrayList<>();
    List<String> mListName=new ArrayList<>();

    private Random mRandom=new Random();


    public JokeAdapter(Context context, List<JokeInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
        mListHead.add(R.drawable.head_1);
        mListHead.add(R.drawable.head_2);
        mListHead.add(R.drawable.head_3);
        mListHead.add(R.drawable.head_4);
        mListHead.add(R.drawable.head_5);
        mListHead.add(R.drawable.head_6);
        mListHead.add(R.drawable.head_7);
        mListHead.add(R.drawable.head_8);
        mListHead.add(R.drawable.head_9);
        mListName= Arrays.asList(context.getResources().getStringArray(R.array.name));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joke_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MHolder){
            ((MHolder) holder).mTvJoke.setText(jokeInfoList.get(position).getContent());
            ((MHolder) holder).mTvTime.setText(jokeInfoList.get(position).getUpdatetime());
            int headPos=mRandom.nextInt(9);
            Glide.with(context).load(mListHead.get(headPos)).into(((MHolder) holder).mCiHead);
            ((MHolder) holder).mTvName.setText(mListName.get(headPos));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JokeDetailActivity.start(context,jokeInfoList.get(position).getContent(),((MHolder) holder).mTvJoke);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.ci_head)
        CircleImageView mCiHead;
        @BindView(R2.id.tv_name)
        TextView mTvName;
        @BindView(R2.id.tv_joke)
        TextView mTvJoke;
        @BindView(R2.id.tv_time)
        TextView mTvTime;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
