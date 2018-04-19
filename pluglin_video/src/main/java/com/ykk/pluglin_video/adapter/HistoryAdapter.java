package com.ykk.pluglin_video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.HistoryInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yukun on 17-11-20.
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<HistoryInfo> jokeInfoList;



    public HistoryAdapter(Context context, List<HistoryInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hidtory_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            final HistoryInfo recInfo = jokeInfoList.get(position);
            ((MHolder) holder).mTvTime.setText(recInfo.getYear()+"年"+recInfo.getMonth()+"月"+recInfo.getDay()+"日");
            ((MHolder) holder).mTvStory.setText(recInfo.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_time)
        TextView mTvTime;
        @BindView(R2.id.tv_story)
        TextView mTvStory;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
