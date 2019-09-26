package com.matrix.yukun.matrix.tool_module.weather.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;

public class SearchCityActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchCityActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_search_city;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }
}
