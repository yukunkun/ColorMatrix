package com.matrix.yukun.matrix.weather_module.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;

import java.util.List;


/**
 * Created by yukun on 17-3-7.
 */
public class RecyclerTomorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<WeaTomorrow.HeWeather6Bean.DailyForecastBean> forecastBeen;

    public RecyclerTomorrowAdapter(Context context, List<WeaTomorrow.HeWeather6Bean.DailyForecastBean> forecastBeen) {
        this.forecastBeen = forecastBeen;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.recycler_day,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WeaTomorrow.HeWeather6Bean.DailyForecastBean castBean = forecastBeen.get(position);
        ((MyHolder)holder).textViewTime.setText(castBean.getDate().substring(5,castBean.getDate().length()));
        ((MyHolder)holder).textViewTianqi.setText(castBean.getCond_txt_d());
        ((MyHolder)holder).textViewWendu.setText(castBean.getTmp_min()+"℃~"+castBean.getTmp_max()+"℃");
        ((MyHolder)holder).textViewShidu.setText("湿度:"+castBean.getHum()+ "%");
        ((MyHolder)holder).textViewJiangshui.setText("降水:"+castBean.getPop()+"mm");
        ((MyHolder)holder).textViewQiya.setText("气压:"+castBean.getPres());
        ((MyHolder)holder).textViewFengxiang.setText(castBean.getWind_dir()+":"+castBean.getWind_sc());
        ((MyHolder)holder).textViewRichu.setText("日出:"+castBean.getSr()+"h");
        ((MyHolder)holder).textViewNengjiandu.setText("能见度:"+castBean.getVis());
    }

    @Override
    public int getItemCount() {
        return forecastBeen.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView textViewTime,textViewTianqi,textViewWendu,textViewShidu,textViewFengxiang
                ,textViewJiangshui,textViewQiya,textViewRichu,textViewNengjiandu;
        public MyHolder(View itemView) {
            super(itemView);
            textViewTime= (TextView) itemView.findViewById(R.id.today_time);
            textViewTianqi= (TextView) itemView.findViewById(R.id.today_tianqi);
            textViewWendu= (TextView) itemView.findViewById(R.id.today_wendu);
            textViewShidu= (TextView) itemView.findViewById(R.id.today_shidu);
            textViewFengxiang= (TextView) itemView.findViewById(R.id.today_fengxiang);
            textViewJiangshui= (TextView) itemView.findViewById(R.id.today_jiangshui);
            textViewQiya=(TextView) itemView.findViewById(R.id.today_qiya);
            textViewRichu= (TextView) itemView.findViewById(R.id.today_richu);
            textViewNengjiandu= (TextView) itemView.findViewById(R.id.today_nengjiandu);
        }
    }
}
