package com.matrix.yukun.matrix.video_module.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.video_module.adapter.RVCommentAdapter;
import com.matrix.yukun.matrix.video_module.entity.EventCategrayPos;
import com.matrix.yukun.matrix.video_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.video_module.entity.VideoCommentBean;
import com.matrix.yukun.matrix.video_module.play.VideoCommentActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by yukun on 17-12-26.
 */

public class CommentFragmentDialog extends DialogFragment {

    @BindView(R2.id.rl_remind)
    RelativeLayout mRlRemind;
    @BindView(R2.id.iv_close)
    ImageView mIvClose;
    @BindView(R2.id.tv_more_comment)
    TextView mTvComment;
    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerView;
    private static String mVideoId;
    List<VideoCommentBean> mVideoCommentBeans=new ArrayList<>();
    private String url="https://www.apiopen.top/satinCommentApi";
    private RVCommentAdapter mRvCommentAdapter;

    public static CommentFragmentDialog getInstance(String videoId) {
        CommentFragmentDialog recFragment = new CommentFragmentDialog();
        mVideoId=videoId;
        return recFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        View inflate = inflater.inflate(R.layout.comment_bottom_layout, null);
        ButterKnife.bind(this, inflate);
        initView(inflate);
        initData();
        return inflate;
    }

    private void initView(View inflate) {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRvCommentAdapter = new RVCommentAdapter(getContext(),mVideoCommentBeans);
        mRecyclerView.setAdapter(mRvCommentAdapter);
    }
    private void initData() {
        OkHttpUtils.post().url(url)
                .addParams("id",mVideoId)
                .addParams("page","0")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optInt("code")==200){
                        JSONObject data = jsonObject.optJSONObject("data");
                        JSONObject hot = data.getJSONObject("hot");
                        JSONArray list = hot.optJSONArray("list");
                        if(list.length()>0){
                            mRlRemind.setVisibility(View.GONE);
                            Gson gson=new Gson();
                            List<VideoCommentBean> videoComment= gson.fromJson(list.toString(), new TypeToken<List<VideoCommentBean>>(){}.getType());
                            mVideoCommentBeans.addAll(videoComment);
                            mRvCommentAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.showToast("没有评论");
                        }
                    }else {
                        ToastUtils.showToast("获取评论失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            int height = ScreenUtils.instance().getHeight(getContext());
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height =height*3/5;
            window.setAttributes(params);
        }
    }

    @OnClick({R2.id.iv_close,R2.id.tv_more_comment})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_close) {
            getDialog().dismiss();
        }
        if (i == R.id.tv_more_comment) {
            Intent intent=new Intent(getContext(), VideoCommentActivity.class);
            intent.putExtra("videoId",mVideoId);
            getContext().startActivity(intent);
            getDialog().dismiss();
        }
    }
}
