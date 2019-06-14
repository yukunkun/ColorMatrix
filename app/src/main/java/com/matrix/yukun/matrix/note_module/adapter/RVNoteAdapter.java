package com.matrix.yukun.matrix.note_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.note_module.activity.NotePreviewActivity;
import com.matrix.yukun.matrix.note_module.bean.NoteBean;
import com.matrix.yukun.matrix.util.DataUtils;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class RVNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<NoteBean> mImageBeans;

    public RVNoteAdapter(Context context, List<NoteBean> imageBeans) {
        mContext = context;
        mImageBeans = imageBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.note_list_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            final NoteBean noteBean = mImageBeans.get(position);
            ((MyHolder) holder).mTvTime.setText(noteBean.getName().length()==13? DataUtils.getNoteTime(Long.valueOf(noteBean.getName())):"未知");
            ((MyHolder) holder).mTvTitle.setText(noteBean.getTitle());
            ((MyHolder) holder).mTvContent.setText(noteBean.getContent());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotePreviewActivity.start(mContext,noteBean.getFilePath());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageBeans.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle,mTvTime,mTvContent;
        public MyHolder(View itemView) {
            super(itemView);
            mTvTitle=itemView.findViewById(R.id.tv_title);
            mTvTime=itemView.findViewById(R.id.tv_time);
            mTvContent=itemView.findViewById(R.id.tv_content);

        }
    }

}
