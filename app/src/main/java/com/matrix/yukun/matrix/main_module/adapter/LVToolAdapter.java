package com.matrix.yukun.matrix.main_module.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.HistoryTodayActivity;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.btmovie.SpecialActivity;
import com.matrix.yukun.matrix.tool_module.dictionary.DictionaryActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yukun on 17-12-19.
 */

public class LVToolAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mListName;
    private List<Integer> mListImage = new ArrayList<>();
    private Map<String, Class> mClassMap = new HashMap<>();

    public LVToolAdapter(Context context) {
        mContext = context;
        mListName = Arrays.asList(context.getResources().getStringArray(R.array.tool_life));

//        mListImage.add(R.mipmap.icon_tool_maps); //515151
        mListImage.add(R.mipmap.icon_tool_history);
        mListImage.add(R.mipmap.icon_tool_zidian);
        mListImage.add(R.mipmap.icon_tool_movie);
//        mListImage.add(R.mipmap.icon_tool_phone);

//        mClassMap.put("周边查询", AMapActivity.class);
        mClassMap.put("历史今日", HistoryTodayActivity.class);
        mClassMap.put("新华字典", DictionaryActivity.class);
        mClassMap.put("BT电影", SpecialActivity.class);
//        mClassMap.put("电话查询", ContactActivity.class);

    }

    @Override
    public int getCount() {
        return mListName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.tool_list_item_layout, null);
        ImageView imageView = view.findViewById(R.id.iv_image);
        final TextView textView = view.findViewById(R.id.tv_tool_name);
        Glide.with(mContext).load(mListImage.get(position)).into(imageView);
        textView.setText(mListName.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView.getText().toString();
                Class aClass = mClassMap.get(text);
                if (aClass != null) {
                    Intent intent = new Intent(mContext, aClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    ToastUtils.showToast("developing...");
                }
            }
        });
        return view;
    }

}
