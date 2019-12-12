package com.matrix.yukun.matrix.tool_module.map.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.tool_module.map.adapter.RVSearchMapAdapter;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class MapSearchActivity extends BaseActivity implements Inputtips.InputtipsListener {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ic_search)
    ImageView icSearch;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    List<Tip> mTips=new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private RVSearchMapAdapter mRvSearchMapAdapter;
    private String mCityCode;
    public static int request=1;
    public static int result=2;
    public static void start(Context context,View view) {
        Intent intent = new Intent(context, MapSearchActivity.class);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            ((Activity)context).startActivityForResult(intent, 1,ActivityOptions.makeSceneTransitionAnimation((Activity) context,view,"share").toBundle());
        }else {
            ((Activity)context).startActivityForResult(intent,request);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MapSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_map_search;
    }

    @Override
    public void initView() {
        mCityCode= SPUtils.getInstance().getString("CityCode");
        mLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvList.setLayoutManager(mLinearLayoutManager);
        mRvSearchMapAdapter = new RVSearchMapAdapter(R.layout.map_search_item,mTips);
        rvList.setAdapter(mRvSearchMapAdapter);
    }

    @Override
    public void initListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    searchResule(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRvSearchMapAdapter.setOnItemClickListener(new RVSearchMapAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, Tip tip) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelable("tip",tip);
                setResult(result,intent.putExtra("bundle",bundle));
                finish();
            }
        });

//        mRvSearchMapAdapter.setOnItemClickListener(new RVSearchMapAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClickListener(int position, Tip tip) {
//                Intent intent=new Intent();
//                intent.putExtra("tip",tip);
//                setResult(result,intent);
//                finish();
//            }
//        });
    }

    private void searchResule(String text) {
        InputtipsQuery inputquery = new InputtipsQuery(text, mCityCode);
        inputquery.setCityLimit(true);//限制在当前城市
        Inputtips inputTips = new Inputtips(this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void initDate() {

    }


    @OnClick({R.id.iv_back, R.id.ic_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ic_search:
                etSearch.setText("");
                etSearch.clearAnimation();
                break;
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        mTips.clear();
        mTips.addAll(list);
        if(mRvSearchMapAdapter!=null){
            mRvSearchMapAdapter.notifyDataSetChanged();
        }
    }
}
