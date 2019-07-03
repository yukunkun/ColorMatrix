package com.matrix.yukun.matrix.video_module.play;

import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.video_module.adapter.ToolsRVAdapter;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ToolsActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvBack;
    private RecyclerView mRvList;
    private GridLayoutManager mGridLayoutManager;
    private List<Integer> mListImage=new ArrayList<>();
    private ToolsRVAdapter mToolsRVAdapter;
    private List<String> mListName;
    private TextView mTvTitle;

    @Override
    public int getLayout() {
        return R.layout.activity_tools;
    }

    @Override
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mTvTitle = findViewById(R.id.tv_title);
        initData();
        mGridLayoutManager = new GridLayoutManager(this,2);
        mRvList.setLayoutManager(mGridLayoutManager);
        mToolsRVAdapter = new ToolsRVAdapter(this,mListImage,mListName);
        mRvList.setAdapter(mToolsRVAdapter);
        //移动
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragCallBack());
        itemTouchHelper.attachToRecyclerView(mRvList);

        getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    private void initData() {
        mListName= Arrays.asList(getResources().getStringArray(R.array.tools_name));
        mListImage.add(R.mipmap.icon_tool_map);
        mListImage.add(R.mipmap.icon_tool_video);
        mListImage.add(R.mipmap.icon_tool_danmu);
        mListImage.add(R.mipmap.icon_tool_erweima);
        mListImage.add(R.mipmap.icon_tool_movie);
        mListImage.add(R.mipmap.icon_tool_weather);
    }

    @Override
    public void initListener(){
        mIvBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.iv_back){
            finish();
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
            //这里我们设置的颜色尽量和你 item 在 xml 中设置的颜色保持一致
            viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color_34b4b3b3));

        }
    }

}
