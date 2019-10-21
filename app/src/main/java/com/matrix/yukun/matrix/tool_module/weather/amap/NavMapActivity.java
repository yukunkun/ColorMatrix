package com.matrix.yukun.matrix.tool_module.weather.amap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;

public class NavMapActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, NavMapActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayout() {
        return R.layout.activity_nav_map;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDate() {

    }

    @Override
    public void initListener() {

    }
}
