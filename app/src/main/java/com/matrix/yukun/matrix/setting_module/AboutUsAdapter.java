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

import java.util.Arrays;
import java.util.List;

/**
 * Created by yukun on 17-3-10.
 */
public class AboutUsAdapter extends BaseAdapter {
    Context context;
    private final List<String> arrayList;

    public AboutUsAdapter(Context context) {
        this.context = context;
        String[] strings=context.getResources().getStringArray(R.array.aboutus);
        arrayList= Arrays.asList(strings);
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
        convertView= LayoutInflater.from(context).inflate(R.layout.setting_2,null);
        if(position==6){
            ((ImageView)convertView.findViewById(R.id.setting_image)).setVisibility(View.GONE);
            ((TextView)convertView.findViewById(R.id.textview_banben)).setText(getVersion());
        }
        ((TextView)convertView.findViewById(R.id.setting_con)).setText(arrayList.get(position));
        return convertView;
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
}
