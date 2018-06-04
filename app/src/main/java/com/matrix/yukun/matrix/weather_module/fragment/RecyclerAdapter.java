package com.matrix.yukun.matrix.weather_module.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;

import java.util.List;


/**
 * Created by yukun on 17-3-7.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<WeaHours.HeWeather6Bean.HourlyBean> forecastBeen;

    public RecyclerAdapter(Context context,List<WeaHours.HeWeather6Bean.HourlyBean> forecastBeen) {
        this.forecastBeen = forecastBeen;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.recycler_hour,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WeaHours.HeWeather6Bean.HourlyBean castBean = forecastBeen.get(position);
        ((MyHolder)holder).textViewTime.setText(castBean.getTime().substring(11,castBean.getTime().length()));
        ((MyHolder)holder).textViewTianqi.setText(castBean.getCond_txt());
        ((MyHolder)holder).textViewWendu.setText(castBean.getTmp()+"℃");
        ((MyHolder)holder).textViewShidu.setText("湿度:"+castBean.getHum()+ "%");
        ((MyHolder)holder).textViewJiangshui.setText("降水:"+castBean.getPop()+"mm");
        ((MyHolder)holder).textViewQiya.setText("气压:"+castBean.getPres());
        ((MyHolder)holder).textViewFengxiang.setText(castBean.getWind_dir()+":"+castBean.getWind_sc());
    }

    @Override
    public int getItemCount() {
        return forecastBeen.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView textViewTime,textViewTianqi,textViewWendu,textViewShidu,textViewFengxiang
                ,textViewJiangshui,textViewQiya;
        public MyHolder(View itemView) {
            super(itemView);
            textViewTime= (TextView) itemView.findViewById(R.id.today_time);
            textViewTianqi= (TextView) itemView.findViewById(R.id.today_tianqi);
            textViewWendu= (TextView) itemView.findViewById(R.id.today_wendu);
            textViewShidu= (TextView) itemView.findViewById(R.id.today_shidu);
            textViewFengxiang= (TextView) itemView.findViewById(R.id.today_fengxiang);
            textViewJiangshui= (TextView) itemView.findViewById(R.id.today_jiangshui);
            textViewQiya= (TextView) itemView.findViewById(R.id.today_qiya);

        }
    }
}
