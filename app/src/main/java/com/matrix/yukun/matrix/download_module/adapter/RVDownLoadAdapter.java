package com.matrix.yukun.matrix.download_module.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.download_module.service.DownLoadEngine;
import com.matrix.yukun.matrix.util.FileUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class RVDownLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<FileInfo> mInfos;
    Context mContext;

    public RVDownLoadAdapter(List<FileInfo> infos, Context context) {
        mInfos = infos;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.download_layout_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            final FileInfo fileInfo = mInfos.get(position);
            Glide.with(mContext).load(fileInfo.imageUrl).into(((MyHolder) holder).mIvCover);
            ((MyHolder) holder).mTvName.setText(fileInfo.fileName);
            ((MyHolder) holder).mTvSize.setText("文件大小："+""+ FileUtil.formatFileSize(fileInfo.size));
            ((MyHolder) holder).mTvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(position);
                    mInfos.remove(position);
                    DataSupport.deleteAll(FileInfo.class, "fileName = ?", fileInfo.fileName);
                    //移除队列
                    DownLoadEngine.getInstance().getDownManager().cancelDownload(fileInfo.url);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mIvCover;
        RelativeLayout mLayout;
        TextView mTvName,mTvSize,mTvDel;
        public MyHolder(View itemView) {
            super(itemView);
            mIvCover=itemView.findViewById(R.id.iv_cover);
            mTvName=itemView.findViewById(R.id.tv_name);
            mTvSize=itemView.findViewById(R.id.tv_size);
            mTvDel=itemView.findViewById(R.id.tv_del);
            mLayout=itemView.findViewById(R.id.rl_item);
        }
    }

}
