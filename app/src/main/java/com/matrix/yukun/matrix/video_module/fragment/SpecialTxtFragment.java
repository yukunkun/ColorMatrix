package com.matrix.yukun.matrix.video_module.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.entity.EventCategrayPos;
import com.matrix.yukun.matrix.video_module.entity.TextInfo;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.video_module.play.TextDetailActivity;
import com.matrix.yukun.matrix.video_module.utils.MyType;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.video_module.views.AMzItemLayout;
import com.matrix.yukun.matrix.video_module.views.RetailMeNotLayout;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.R;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by yukun on 17-12-5.
 */

public class SpecialTxtFragment extends BaseFragment {
    @BindView(R2.id.l_retail_me_not)
    RetailMeNotLayout mLRetailMeNot;
    String url = "http://v3.wufazhuce.com:8000/api/onelist/idlist/?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android 或 http://v3.wufazhuce.com:8000/api/onelist/idlist/?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android";
    private JSONArray mData;
    int page = 0;
    List<TextInfo> jokeInfoList=new ArrayList<>();
    private RetailMeNotAdapter mAdapter;
    private List<String> urlList=new ArrayList<>();
    private int mWidth;
    private List<Integer> mList=new ArrayList<>();
    private ImageView mImageView;

    public static SpecialTxtFragment getInstance() {
        SpecialTxtFragment recFragment = new SpecialTxtFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.special_list_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        getIconInfo();
        RelativeLayout tMoreInfo = new RelativeLayout(getContext());
        tMoreInfo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mWidth = ScreenUtils.instance().getWidth(getContext());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.spec_layout_footrer,null);
        tMoreInfo.addView(view);
        mImageView = (ImageView) view.findViewById(R.id.iv_icon);
        FloatingActionButton actionButton= (FloatingActionButton) view.findViewById(R.id.fl_btn);
        final TextView button= (TextView) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                startAnimation(mImageView);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random=new Random();
                int anInt = random.nextInt(5);
                mImageView.setImageResource(mList.get(anInt));
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventCategrayPos(6));
            }
        });
        mLRetailMeNot.addBottomContent(tMoreInfo);
        mAdapter = new RetailMeNotAdapter(getContext());
        mLRetailMeNot.setAdapter(mAdapter);
        getInfo();
    }

    private void getIconInfo() {
        mList.add(R.mipmap.icon_chris);
        mList.add(R.mipmap.icon_small_bird);
        mList.add(R.mipmap.icon_tree);
        mList.add(R.mipmap.icon_snow);
        mList.add(R.mipmap.icon_snowman);
    }

    public void startAnimation(final ImageView view){
        int mHeight=220;
        Random random=new Random();
        int anInt = random.nextInt(5);
        view.setImageResource(mList.get(anInt));
        PointF pointFFirst = new PointF(mWidth/4-view.getWidth(),-mHeight);
        PointF pointFSecond = new PointF(mWidth/4*3-view.getWidth(),mHeight*2);
        PointF pointFStart = new PointF(0,mHeight);
        PointF pointFEnd = new PointF(mWidth-view.getWidth(),mHeight);
        ValueAnimator animator = ValueAnimator.ofObject(new MyType(pointFFirst, pointFSecond), pointFStart, pointFEnd);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF value = (PointF) animation.getAnimatedValue();
                view.setX(value.x);
                view.setY(value.y);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimation(mImageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.play(animator);
        set.start();
    }

    private void getInfo() {
        NetworkUtils.networkGet(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    mData = jsonObject.optJSONArray("data");
                    if(mData !=null){
                        for (int i = 0; i < mData.length(); i++) {
                           String url = "http://v3.wufazhuce.com:8000/api/onelist/ "+ mData.optInt(i) + "/0?cchannel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android";
                            urlList.add(url);
                        }
                        //获取每一个信息
                        getAllMsg();
                        Log.i("urlList", urlList.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAllMsg() {
        String urlDate ;
        if(urlList.size()>0){
            urlDate=urlList.get(page);
        final String finalUrlDate = urlDate;
        NetworkUtils.networkGet(urlDate)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
                Log.i("err",e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.optJSONObject("data");
                    JSONArray content_list = data.optJSONArray("content_list");
                    Gson gson = new Gson();
                    List<TextInfo> jokeList = gson.fromJson(content_list.toString(), new TypeToken<List<TextInfo>>() {
                    }.getType());
                    jokeInfoList.addAll(jokeList);
                    page++;
                    Log.i("url", finalUrlDate);
                    if(page>=urlList.size()/2){
                        if(mAdapter!=null){
                            mAdapter.notifyDataSetChanged();
                        }
                        return;
                    }else {
                        getAllMsg();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        }
    }


    private class RetailMeNotAdapter extends RetailMeNotLayout.Adapter {

        Context context;
        public RetailMeNotAdapter(Context context){
            this.context=context;
        }
        @Override
        public View getView(final int position, ViewGroup parent, int expandedHeight, int normalHeight) {
            AMzItemLayout item = new AMzItemLayout(parent.getContext());
            item.setData(position, jokeInfoList.get(position), expandedHeight, normalHeight);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TextDetailActivity.class);
                    intent.putExtra("url",jokeInfoList.get(position).getShare_url());
                    intent.putExtra("title",jokeInfoList.get(position).getTitle());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                }
            });
            return item;
        }

        @Override
        public int getCount() {
            return jokeInfoList.size();
        }
    }
}
