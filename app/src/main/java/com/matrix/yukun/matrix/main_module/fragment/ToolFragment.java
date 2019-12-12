package com.matrix.yukun.matrix.main_module.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.ToolsRVAdapter;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.mine_module.activity.SettingActivity;
import com.matrix.yukun.matrix.mine_module.activity.ShareActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/10/28.
 */

public class ToolFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mIvSetting;
    private RecyclerView mRvList;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Integer> mListImage=new ArrayList<>();
    private ToolsRVAdapter mToolsRVAdapter;
    private List<String> mListName=new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvShare;

    public static ToolFragment getInstance(){
        ToolFragment toolFragment=new ToolFragment();
        return toolFragment;
    }
    @Override
    public int getLayout() {
        return R.layout.activity_tools;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mIvSetting = (ImageView) inflate.findViewById(R.id.iv_setting);
        mRvList = (RecyclerView)inflate. findViewById(R.id.rv_list);
        mIvShare = inflate.findViewById(R.id.iv_share);
        mSwipeRefreshLayout = inflate.findViewById(R.id.sr);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRvList.setLayoutManager(mLinearLayoutManager);
        mToolsRVAdapter = new ToolsRVAdapter(getContext(),mListImage,mListName);
        mRvList.setAdapter(mToolsRVAdapter);
        if(SPUtils.getInstance().getBoolean("isbrief")){
            mIvSetting.setVisibility(View.VISIBLE);
        }
        initData();
        initListener();
    }

    public void initListener() {
        mIvShare.setOnClickListener(this);
        mIvSetting.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initData() {
        String tool = SPUtils.getInstance().getString("tool");
        if(TextUtils.isEmpty(tool)){
            //initial list
            mListName.addAll(getToolNameList());
            mListImage.addAll(initImageList());
        //SP 获取
        }else {
            String[] split = tool.split(",");
            mListName.addAll(Arrays.asList(split));
            for (int i = 0; i < mListName.size(); i++) {
                addImageList(mListName.get(i));
            }
        }
    }

    private void addImageList(String value) {
        List<String> list = getToolNameList();
        List<Integer> integerList = initImageList();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(value)){
                mListImage.add(integerList.get(i));
            }
        }
    }

    @NonNull
    private List<String> getToolNameList() {
        return Arrays.asList(getResources().getStringArray(R.array.tools_name));
    }

    private List<Integer> initImageList() {
        List<Integer> integers=new ArrayList<>();
        integers.add(R.mipmap.icon_tool_map);
        integers.add(R.mipmap.icon_tool_video);
        integers.add(R.mipmap.icon_tool_danmu);
        integers.add(R.mipmap.icon_tool_erweima);
        integers.add(R.mipmap.icon_tool_weather);
        integers.add(R.mipmap.icon_tool_calandar);
        integers.add(R.mipmap.icon_tool_gif);
        integers.add(R.mipmap.icon_tool_note);
        integers.add(R.mipmap.icon_tool_net);
        integers.add(R.mipmap.icon_tool_desk);
        return integers;
    }

    private void getWindowManager() {
        getActivity().getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        long guide_time = SPUtils.getInstance().getLong("guide_time");
                        long currentTimeMillis = System.currentTimeMillis();
                        if(currentTimeMillis-guide_time>/*2*24*60*60*1000*/10000){
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int mId = v.getId();
        if(mId == R.id.iv_share){
            Intent intent=new Intent(getContext(), ShareActivity.class);
            startActivity(intent);
        }
        if(mId == R.id.iv_setting){
            Intent intent=new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        }
    }

    class ItemDragCallBack extends ItemTouchHelper.Callback {
        //通过返回值来设置是否处理某次拖曳或者滑动事件
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //注意：和拖曳的区别就是在这里
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

//            dragFlags 是拖拽标志，
//            swipeFlags 是滑动标志，
//            swipeFlags 都设置为0，暂时不考虑滑动相关操作。
        }

        //当长按并进入拖曳状态时，拖曳的过程中不断的回调此方法
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //拖动的 item 的下标
            int fromPosition = viewHolder.getAdapterPosition();
            //目标 item 的下标，目标 item 就是当拖曳过程中，不断和拖动的 item 做位置交换的条目。
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mListImage, i, i + 1);
                    Collections.swap(mListName, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mListImage, i, i - 1);
                    Collections.swap(mListName, i, i -  1);
                }
            }
            mToolsRVAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        //滑动删除的回调
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int adapterPosition = viewHolder.getAdapterPosition();
//            mJokeAdapter.notifyItemRemoved(adapterPosition);
//            jokeInfoList.remove(adapterPosition);

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //给被拖曳的 item 设置一个深颜色背景
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color_73b4b3b3));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //给已经完成拖曳的 item 恢复开始的背景。
            mToolsRVAdapter.updateData(mListName);
            //这里我们设置的颜色尽量和你 item 在 xml 中设置的颜色保持一致
            viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color_34b4b3b3));
            //清除SP
            SPUtils.getInstance().clearKey("tool");
            //添加数据存贮SP
            String saveName="";
            for (int i = 0; i < mListName.size(); i++) {
                saveName=saveName+mListName.get(i)+",";
            }
            SPUtils.getInstance().saveString("tool", saveName);
        }
    }

}

