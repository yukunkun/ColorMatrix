package com.matrix.yukun.matrix.video_module.play;
import android.content.Context;
import android.content.Intent;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseActivity;

public class ImageSearchActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent=new Intent(context,ImageSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_image_search;
    }

    @Override
    public void initView() {

    }
}
