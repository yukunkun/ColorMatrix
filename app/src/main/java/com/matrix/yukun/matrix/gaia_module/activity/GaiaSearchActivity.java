package com.matrix.yukun.matrix.gaia_module.activity;

import android.content.Context;
import android.content.Intent;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseActivity;

public class GaiaSearchActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent=new Intent(context,GaiaSearchActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_gaia_search;
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
