package com.ykk.pluglin_video.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.JokeAdapter;
import com.ykk.pluglin_video.entity.JokeInfo;
import com.ykk.pluglin_video.netutils.NetworkUtils;
import com.ykk.pluglin_video.utils.SpacesDoubleDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class JokeFragment extends BaseFragment {
    String url = "http://api.avatardata.cn/Joke/QueryJokeByTime";
    int page = 1;
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<JokeInfo> jokeInfoList=new ArrayList<>();
    private JokeAdapter mJokeAdapter;
    private long mTime;
    private LinearLayoutManager mLayoutManager;
    boolean isVertical=true;
    private GridLayoutManager mGridLayoutManager;
    private SpacesDoubleDecoration mSpacesDoubleDecoration;

    public static JokeFragment getInstance() {
        JokeFragment recFragment = new JokeFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mTime = System.currentTimeMillis() / 1000;
        mLayoutManager = new LinearLayoutManager(getContext());
        mGridLayoutManager=new GridLayoutManager(getContext(),2);
        if(isVertical){
            mRvJoke.setLayoutManager(mLayoutManager);
        }else {
            mRvJoke.setLayoutManager(mGridLayoutManager);
        }
        mJokeAdapter = new JokeAdapter(getContext(),jokeInfoList);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragCallBack());
        itemTouchHelper.attachToRecyclerView(mRvJoke);
    }
    public void getLayoutTag(boolean isTag){
        isVertical=isTag;
        if(isVertical){
            mRvJoke.setLayoutManager(mLayoutManager);
        }else {
            mRvJoke.setLayoutManager(mGridLayoutManager);
            mSpacesDoubleDecoration=new SpacesDoubleDecoration(0,1,1,0);
            mRvJoke.addItemDecoration(mSpacesDoubleDecoration);
        }
        mJokeAdapter.notifyDataSetChanged();
    }


    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jokeInfoList.clear();
                Random random=new Random();
                int nextInt = random.nextInt(30);
                mJokeAdapter.notifyDataSetChanged();
                page=nextInt;
                getInfo();
                mSw.setRefreshing(false);
            }
        });

        mRvJoke.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
//                if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
//                    page++;
//                    getInfo();
//                }

                //竖向
                if(isVertical){
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                        page++;
                        getInfo();
                    }
                }else {
                    //格子布局
                    int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mGridLayoutManager.getItemCount()-1){
                        page++;
                        getInfo();
                    }
                }
            }
        });
    }

    private void getInfo() {
        Random random=new Random();
        int nextInt = random.nextInt(30);
        NetworkUtils.networkGet(url)
                .addParams("key", NetworkUtils.APPKEY)
                .addParams("time", mTime + "")
                .addParams("sort", "desc")
                .addParams("page", nextInt + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("result");
                    if(data!=null){
                        Gson gson = new Gson();
                        List<JokeInfo> jokeList = gson.fromJson(data.toString(), new TypeToken<List<JokeInfo>>() {
                        }.getType());
                        jokeInfoList.addAll(jokeList);
                        mJokeAdapter.notifyDataSetChanged();
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

}
