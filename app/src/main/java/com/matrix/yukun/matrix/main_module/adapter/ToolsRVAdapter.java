package com.matrix.yukun.matrix.main_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.selfview.NoScrollListView;
import com.matrix.yukun.matrix.util.ToolItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yukun on 17-11-20.
 */

public class ToolsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Integer> imageList;
    List<String > mListName;
    private ToolsItemAdapter mToolsItemAdapter;

    public ToolsRVAdapter(Context context, List<Integer> imageList,List<String> mListName) {
        this.context = context;
        this.imageList = imageList;
        this.mListName= mListName;
    }

    public void updateData(List<String>  listName){
        this.mListName=listName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =null;
        if(viewType == 0){
             view = LayoutInflater.from(context).inflate(R.layout.tool_grid_layout, null);
            return new MHolderTool(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.tool_list_layout, null);
            return new MHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MHolderTool){
            GridLayoutManager gridLayoutManager=new GridLayoutManager(context,4);
            ((MHolderTool) holder).mRecyclerView.setLayoutManager(gridLayoutManager);
            mToolsItemAdapter = new ToolsItemAdapter(context, imageList, mListName);
            ((MHolderTool) holder).mRecyclerView.setAdapter(mToolsItemAdapter);
            ((MHolderTool) holder).mRecyclerView.addItemDecoration(new ToolItemDecoration(8));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragCallBack());
            itemTouchHelper.attachToRecyclerView(((MHolderTool) holder).mRecyclerView);
        }
        if(holder instanceof MHolder){
            ((MHolder) holder).listview.setAdapter(new LVToolAdapter(context));
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else {
            return 1;
        }
    }

    class MHolderTool extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerview)
        RecyclerView mRecyclerView;
        public MHolderTool(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.listview)
        NoScrollListView listview;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            //拖动的 item 的下标a
            int fromPosition = viewHolder.getAdapterPosition();
            //目标 item 的下标，目标 item 就是当拖曳过程中，不断和拖动的 item 做位置交换的条目。
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(imageList, i, i + 1);
                    Collections.swap(mListName, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(imageList, i, i - 1);
                    Collections.swap(mListName, i, i -  1);
                }
            }
            mToolsItemAdapter.notifyItemMoved(fromPosition, toPosition);
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
                viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_73b4b3b3));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //给已经完成拖曳的 item 恢复开始的背景。
//            this.updateData(mListName);
            //这里我们设置的颜色尽量和你 item 在 xml 中设置的颜色保持一致
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_34b4b3b3));
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
