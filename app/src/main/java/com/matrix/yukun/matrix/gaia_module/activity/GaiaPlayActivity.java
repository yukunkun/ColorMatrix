package com.matrix.yukun.matrix.gaia_module.activity;


import android.content.Context;
import android.content.Intent;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.gaiaplayer.GaiaJzvdStd;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import butterknife.BindView;
import cn.jzvd.Jzvd;

public class GaiaPlayActivity extends BaseActivity {

    @BindView(R.id.gaia_player)
    GaiaJzvdStd gaiaPlayer;
    String url="http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
    private long mId;
    private int mType;

    public static void start(Context context,long id,int type){
        Intent intent=new Intent(context,GaiaPlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        intent.putExtra("type",type);  // 0 作品 1 素材
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gaia_play;
    }

    @Override
    public void initView() {
        mId = getIntent().getLongExtra("id",0);
        mType = getIntent().getIntExtra("type",0);
        gaiaPlayer.setUp(url,url, Jzvd.SCREEN_NORMAL);
        gaiaPlayer.startVideo();
    }

    @Override
    public void initDate() {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gaiaPlayer.reset();
    }
}
