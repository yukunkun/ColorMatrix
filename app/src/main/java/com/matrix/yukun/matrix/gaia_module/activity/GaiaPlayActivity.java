package com.matrix.yukun.matrix.gaia_module.activity;


import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.bean.MaterialDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoType;
import com.matrix.yukun.matrix.gaia_module.fragment.DetailFragment;
import com.matrix.yukun.matrix.gaia_module.gaiaplayer.GaiaJzvdStd;
import com.matrix.yukun.matrix.gaia_module.net.VideoUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.jzvd.Jzvd;

public class GaiaPlayActivity extends BaseActivity {

    @BindView(R.id.gaia_player)
    GaiaJzvdStd gaiaPlayer;
    String url="http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
    private long mId;
    private int mType;
    private VideoDetailInfo mDetailInfo;
    private MaterialDetailInfo mMaterialInfo;

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
        if(mType== VideoType.WORK.getType()){
            VideoUtils.getWorkVideo((int) mId, new VideoUtils.GetVideoListener() {
                @Override
                public void getVideo(String data) {
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        mDetailInfo = new Gson().fromJson(jsonObject.optJSONObject("o").toString(), VideoDetailInfo.class);
                        initVideoView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void getError(String error) {
                    LogUtil.i(error);
                }
            });
        }else {
            VideoUtils.getMaterialVideo((int) mId, new VideoUtils.GetVideoListener() {
                @Override
                public void getVideo(String data) {
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        mMaterialInfo = new Gson().fromJson(jsonObject.optJSONObject("o").toString(), MaterialDetailInfo.class);
                        initMaterialVide();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void getError(String error) {
                    LogUtil.i(error);
                }
            });
        }

    }

    private void initVideoView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl,DetailFragment.instance(mId,mDetailInfo)).commit();
    }

    private void initMaterialVide() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl,DetailFragment.instance(mId,mMaterialInfo)).commit();
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
