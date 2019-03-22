package com.matrix.yukun.matrix.video_module.play;

import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.adapter.HistoryAdapter;
import com.matrix.yukun.matrix.video_module.adapter.LVAdapter;
import com.matrix.yukun.matrix.video_module.entity.HistoryInfo;
import com.matrix.yukun.matrix.video_module.entity.SortModel;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.video_module.utils.Cn2Spell;
import com.matrix.yukun.matrix.video_module.utils.ISideBarSelectCallBack;
import com.matrix.yukun.matrix.video_module.utils.PinyinComparator;
import com.matrix.yukun.matrix.video_module.views.SideBar;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HistoryTodayActivity extends BaseActivity {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.rl)
    RelativeLayout mRl;
    @BindView(R2.id.recyclerview)
    ListView mListview;
    @BindView(R2.id.bar)
    SideBar mBar;
    @BindView(R2.id.av_load)
    AVLoadingIndicatorView mAvLload;

    private List<HistoryInfo> mList = new ArrayList<>();
    private String APPKEY = "1c88cd3077c448b199b227a5eccca0ed";
//    private HistoryAdapter mHistoryAdapter;
    private List<SortModel> mSortModule;

    @Override
    public int getLayout() {
        return R.layout.activity_history_today;
    }

    @Override
    public void initView() {
        mAvLload.setIndicator("BallTrianglePathIndicator");
        mAvLload.setIndicatorColor(getResources().getColor(R.color.color_00ff00));
        mAvLload.show();
//        mHistoryAdapter = new HistoryAdapter(this, mList);
        getInfo();
        mBar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                if(mSortModule==null){
                    return;
                }
                for (int i = 0; i < mSortModule.size() ; i++){
                    if(mSortModule.get(i).getSortLetters().equals(selectStr)){
                        mListview.setSelection(i);
                        return;
                    }
                }
            }

            @Override
            public void onSelectEnd() {

            }

            @Override
            public void onSelectStart() {

            }
        });
    }

    private void getInfo() {
        String url = "http://api.avatardata.cn/HistoryToday/LookUp";
        Calendar CD = Calendar.getInstance();
        int MM = CD.get(Calendar.MONTH) + 1;
        int DD = CD.get(Calendar.DATE);
        NetworkUtils.networkGet(url)
                .addParams("key", APPKEY)
                .addParams("yue", MM + "")
                .addParams("ri", DD + "")
                .addParams("type", 1 + "")
                .addParams("rows", 50 + "")
                .addParams("page", 1 + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(HistoryTodayActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    mAvLload.hide();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("result");
                    if (data != null&&data.length()>0) {
                        Gson gson = new Gson();
                        List<HistoryInfo> jokeList = gson.fromJson(data.toString(), new TypeToken<List<HistoryInfo>>() {
                        }.getType());
                        mList.addAll(jokeList);
                        mSortModule = getSortModule();
                        LVAdapter lvAdapter = new LVAdapter(mSortModule, HistoryTodayActivity.this);
                        mListview.setAdapter(lvAdapter);
                    }else {
                        Toast.makeText(HistoryTodayActivity.this, "历史今日无大事发生", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<SortModel> getSortModule() {
        List<SortModel> filterDateList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            String pinYinFirstLetter = Cn2Spell.getPinYinFirstLetter(mList.get(i).getTitle());
            SortModel sortModel = new SortModel(mList.get(i).getTitle(), pinYinFirstLetter.toUpperCase().charAt(0) + "", mList.get(i).getYear(), mList.get(i).getMonth(), mList.get(i).getDay());
            filterDateList.add(sortModel);
        }
        Collections.sort(filterDateList, new PinyinComparator());
        return filterDateList;
    }

    @OnClick(R2.id.iv_back)
    public void onClick() {
        finish();
    }
}
