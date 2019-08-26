package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.VideoCommentAdapter;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static me.everything.android.ui.overscroll.OverScrollDecoratorHelper.ORIENTATION_VERTICAL;

/**
 * author: kun .
 * date:   On 2019/3/13
 */
public class VideoDetailComment extends BaseFragment {

    private ArrayList<EyesInfo.DataBean.TagsBean> mAvator;
    private RecyclerView mRecyclerView;
    private RelativeLayout mLayout;
    private LinearLayoutManager mLinearLayoutManager;

    public static VideoDetailComment getInstance(ArrayList<EyesInfo.DataBean.TagsBean> avator){
        VideoDetailComment videoDetailFragment=new VideoDetailComment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("avator",avator);
        videoDetailFragment.setArguments(bundle);
        return videoDetailFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.video_detail_comment_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mAvator = (ArrayList<EyesInfo.DataBean.TagsBean>) getArguments().getSerializable("avator");
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = inflate.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mLayout = inflate.findViewById(R.id.rl_remind);
        if(mAvator.size()==0){
            mLayout.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setAdapter(new VideoCommentAdapter(getContext(),mAvator));
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView,ORIENTATION_VERTICAL);
    }
}
