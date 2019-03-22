package com.matrix.yukun.matrix.calarder_module.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.calarder_module.contant.CalandarBean;
import com.matrix.yukun.matrix.calarder_module.contant.TypeBean;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/29.
 */

public class RVCalandarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CalandarBean> mCalandarBeans;
    Context mContext;
    List<Integer> mDrawables=new ArrayList<>();
    Random mRandom=new Random();
    OnSelectCallBack mOnSelectCallBack;
    public RVCalandarAdapter(List<CalandarBean> calandarBeans, Context context) {
        mCalandarBeans = calandarBeans;
        mContext = context;
        mDrawables.add(R.drawable.shape_calandar_left_color_blue);
        mDrawables.add(R.drawable.shape_calandar_left_color_red);
        mDrawables.add(R.drawable.shape_calandar_left_color_yello);
        mDrawables.add(R.drawable.shape_calandar_left_color_green);
        mDrawables.add(R.drawable.shape_calandar_left_color);
    }

    public void setOnSelectCallBack(OnSelectCallBack onSelectCallBack) {
        mOnSelectCallBack = onSelectCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.calarder_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
            int pos = mRandom.nextInt(mDrawables.size());
            ((MyHolder) holder).mTvTypeColor.setBackgroundResource(mDrawables.get(pos));
            CalandarBean calandarBean = mCalandarBeans.get(position);
            ((MyHolder) holder).mTvTime.setText(calandarBean.getCreateTime().substring(5,calandarBean.getCreateTime().length()));
            ((MyHolder) holder).mTvTitle.setText(calandarBean.getCalandarTitle());
            if(calandarBean.getCategray()== TypeBean.TYPE_DEFEAT){
                ((MyHolder) holder).mTvCategray.setText(mContext.getResources().getString(R.string.moren));
            }else if(calandarBean.getCategray()== TypeBean.TYPE_LIFE){
                ((MyHolder) holder).mTvCategray.setText(mContext.getResources().getString(R.string.life));
            }else if(calandarBean.getCategray()== TypeBean.TYPE_WORK){
                ((MyHolder) holder).mTvCategray.setText(mContext.getResources().getString(R.string.work));
            }else if(calandarBean.getCategray()== TypeBean.TYPE_OTHER){
                ((MyHolder) holder).mTvCategray.setText(mContext.getResources().getString(R.string.other));
            }
            if(date2TimeStamp(calandarBean.getCreateTime())>System.currentTimeMillis()&&calandarBean.getType().equals(TypeBean.TYPE_NOT_START)){
                ((MyHolder) holder).mTvType.setText(mContext.getResources().getString(R.string.not_start));
                ((MyHolder) holder).mTvType.setBackgroundResource(R.drawable.shape_calandar_event_bg);
                ((MyHolder) holder).mTvType.setTextColor(mContext.getResources().getColor(R.color.gplus_color_1));
                ((MyHolder) holder).mTvType.setPaintFlags(((MyHolder) holder).mTvType.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }else if(date2TimeStamp(calandarBean.getCreateTime())<System.currentTimeMillis()&&calandarBean.getType().equals(TypeBean.TYPE_NOT_START)) {
                ((MyHolder) holder).mTvType.setText(mContext.getResources().getString(R.string.not_finish));
                ((MyHolder) holder).mTvType.setBackgroundResource(R.drawable.shape_calandar_event_not_bg);
                ((MyHolder) holder).mTvType.setTextColor(mContext.getResources().getColor(R.color.color_bfbfbf));
                ((MyHolder) holder).mTvType.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if(calandarBean.getType().equals(TypeBean.TYPE_FINISH)){
                ((MyHolder) holder).mTvType.setText(mContext.getResources().getString(R.string.finish));
                ((MyHolder) holder).mTvType.setBackgroundResource(R.drawable.shape_calandar_event_finish_bg);
                ((MyHolder) holder).mTvType.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                ((MyHolder) holder).mTvType.setPaintFlags(((MyHolder) holder).mTvType.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder
                        builder = new AlertDialog.Builder(
                        mContext);
                builder.setTitle("编辑");
                builder.setMessage("是否删除这天记录?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(CalandarBean.class, "time = ?", mCalandarBeans.get(position).getTime()+"");
                        mCalandarBeans.remove(mCalandarBeans.get(position));
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectCallBack.onSelectCallBack(mCalandarBeans.get(position));
            }
        });
    }

    public static long date2TimeStamp(String date_str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
            return Long.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mCalandarBeans.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_type_color)
        TextView mTvTypeColor;
        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.tv_categray)
        TextView mTvCategray;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnSelectCallBack{
        void onSelectCallBack(CalandarBean calandarBean);
    }
}
