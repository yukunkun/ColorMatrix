package com.matrix.yukun.matrix.main_module.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.ImageSearchActivity;
import com.matrix.yukun.matrix.main_module.adapter.JokeAdapter;
import com.matrix.yukun.matrix.main_module.entity.EventShowSecond;
import com.matrix.yukun.matrix.main_module.entity.JokeInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class JokeFragment extends BaseFragment {
    String url_old = "http://v.juhe.cn/joke/randJoke.php";
    String url="http://v.juhe.cn/joke/content/text.php";
    int page = 1;
    private TwoLevelHeader mHeader;
    private SmartRefreshLayout mSmartRefreshLayout;
    private RelativeLayout mIvRoot;
    private CardView mCardView;
    private SmartRefreshLayout mRefreshGame;
    private ImageView mIvSecondBack;
    private RecyclerView mRvJoke;
    private RelativeLayout mLayoutRemind;
    List<JokeInfo> jokeInfoList=new ArrayList<>();
    private JokeAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    boolean isVertical=true;
    private View mFloor;

    public static JokeFragment getInstance() {
        JokeFragment recFragment = new JokeFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_image;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mSmartRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mFloor = inflate.findViewById(R.id.secondfloor);
        mHeader = inflate.findViewById(R.id.header);
        mIvRoot = inflate.findViewById(R.id.secondfloor_content);
        mCardView = inflate.findViewById(R.id.card_view);
        mRefreshGame = inflate.findViewById(R.id.smartrefresh);
        mIvSecondBack = inflate.findViewById(R.id.iv_second_back);
        mRvJoke = inflate.findViewById(R.id.recyclerview);
        mLayoutRemind = inflate.findViewById(R.id.rl_remind);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new JokeAdapter(getContext(),jokeInfoList);
        mRefreshGame.setPrimaryColorsId(R.color.color_2299ee, R.color.color_whit);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragCallBack());
        itemTouchHelper.attachToRecyclerView(mRvJoke);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getViewpagerPos(EventShowSecond showSecond){
        if(showSecond.selectPosition!=4){
            mHeader.finishTwoLevel();
            mIvRoot.animate().alpha(0).setDuration(1000);
        }
    }


    private void setListener() {
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                    page++;
                    getInfo();
                }
                mSmartRefreshLayout.finishLoadMore(20);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getInfo();
                refreshLayout.finishRefresh(20);
            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mFloor.setTranslationY(Math.min(offset - mFloor.getHeight(), mSmartRefreshLayout.getLayout().getHeight() - mFloor.getHeight()));
            }
        });

        mHeader.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                mIvRoot.animate().alpha(1).setDuration(2000);
                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSearchActivity.start(getContext());
                mHeader.finishTwoLevel();
                mIvRoot.animate().alpha(0).setDuration(1000);
            }
        });
        mIvSecondBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeader.finishTwoLevel();
                mIvRoot.animate().alpha(0).setDuration(1000);
            }
        });
    }

    private void getInfo() {
        NetworkUtils.networkGet(url)
                .addParams("key", NetworkUtils.APPKEY)
//                .addParams("time", mTime + "")
//                .addParams("sort", "asc")
                .addParams("page", page + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.optJSONObject("result");
                    if(data!=null){
                        mLayoutRemind.setVisibility(View.GONE);
                        JSONArray jsonArray = data.optJSONArray("data");
                        Gson gson = new Gson();
                        List<JokeInfo> jokeList = gson.fromJson(jsonArray.toString(), new TypeToken<List<JokeInfo>>() {
                        }.getType());
                        jokeInfoList.addAll(jokeList);
                        mJokeAdapter.notifyDataSetChanged();
                        mSmartRefreshLayout.finishLoadMore();
                        mSmartRefreshLayout.finishRefresh();
                    }else {
                        String reason = jsonObject.optString("reason");
                        ToastUtils.showToast(reason);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
                    Collections.swap(jokeInfoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(jokeInfoList, i, i - 1);
                }
            }
            mJokeAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        //滑动删除的回调
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int adapterPosition = viewHolder.getAdapterPosition();
            mJokeAdapter.notifyItemRemoved(adapterPosition);
            jokeInfoList.remove(adapterPosition);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //给被拖曳的 item 设置一个深颜色背景
                viewHolder.itemView.setBackgroundColor(Color.GRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //给已经完成拖曳的 item 恢复开始的背景。
            //这里我们设置的颜色尽量和你 item 在 xml 中设置的颜色保持一致
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
