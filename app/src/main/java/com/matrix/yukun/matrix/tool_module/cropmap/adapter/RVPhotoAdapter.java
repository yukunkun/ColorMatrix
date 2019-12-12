package com.matrix.yukun.matrix.tool_module.cropmap.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

import java.util.List;

/**
 * Created by yukun on 17-1-19.
 */
public class RVPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private  List<String> names;
    private int selectPosition;
    public RVPhotoAdapter(Context context, List<String> strings) {
        this.context = context;
        this.names=strings;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rec_itrem,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            ((MyHolder) holder).textView.setText(names.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectPosition!=position){
                        selectPosition=position;
                        mOnItemClickListener.onItemClickListener(position);
                    }
                }
            });
        }
        if(selectPosition==position){
            ((MyHolder) holder).textView.setTextColor(context.getResources().getColor(R.color.color_00ff00));
        }
        else {
            ((MyHolder) holder).textView.setTextColor(context.getResources().getColor(R.color.color_whit));

        }
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public final TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }
}
