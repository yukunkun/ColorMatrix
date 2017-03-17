package com.matrix.yukun.matrix.setting_module;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yukun on 17-3-10.
 */
public class SetAdapter extends BaseAdapter {
    Context context;
    private int viewTyp1=0;
    private int viewTyp2=2;

    private final List<String> arrayList;

    public SetAdapter(Context context) {
        this.context = context;
        String[] strings=context.getResources().getStringArray(R.array.setting);
        arrayList= Arrays.asList(strings);
    }

    @Override
    public int getCount() {
        return arrayList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position==viewTyp1){
            convertView= LayoutInflater.from(context).inflate(R.layout.setting_1,null);
        }else /*if((position)==viewTyp2)*/{
            convertView= LayoutInflater.from(context).inflate(R.layout.setting_2,null);
            if(position==8){
                ((ImageView)convertView.findViewById(R.id.setting_image)).setVisibility(View.GONE);
                ((TextView)convertView.findViewById(R.id.textview_banben)).setText(getVersion());
            }
            ((TextView)convertView.findViewById(R.id.setting_con)).setText(arrayList.get(position-1));
            if(position==1||position==2||position==3){
                ((TextView)convertView.findViewById(R.id.setting_con)).setTextColor(context.getResources().getColor(R.color.color_44fc2c));
            }else {
                ((TextView)convertView.findViewById(R.id.setting_con)).setTextColor(context.getResources().getColor(R.color.color_whit));
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return viewTyp1;
        }else {
            return viewTyp2;
        }
    }
    private String getVersion() {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
}
