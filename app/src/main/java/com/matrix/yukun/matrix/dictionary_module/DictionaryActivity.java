package com.matrix.yukun.matrix.dictionary_module;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.util.InputUtil;
import com.matrix.yukun.matrix.util.KeyBoardUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class DictionaryActivity extends BaseActivity {
    String Url = "http://v.juhe.cn/xhzd/query";
    String TAG = getClass().getSimpleName();
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_search)
    ImageView mIvSearch;
    @BindView(R2.id.tv_internet)
    TextView mTvInternet;
    @BindView(R2.id.et_search)
    EditText mEtSearch;
    @BindView(R2.id.tv_word)
    TextView mTvWord;
    @BindView(R2.id.tv_pinyin)
    TextView mTvPinyin;
    @BindView(R2.id.tv_bushou)
    TextView mTvBushou;
    @BindView(R2.id.tv_jijie)
    TextView mTvJijie;
    @BindView(R2.id.tv_xiangjie)
    TextView mTvXiangjie;
    @BindView(R2.id.ll_con)
    LinearLayout  mLayout;
    private DictionaryBean mDictionaryBean;

    @Override
    public int getLayout() {
        return R.layout.activity_dictionary;
    }

    @Override
    public void initView() {

    }

    private void updateView() {
        String jijie="";
        String xiangJie="";
        mTvWord.setText(mDictionaryBean.getZi());
        mTvPinyin.setText("拼音："+mDictionaryBean.getPinyin());
        mTvBushou.setText("部首:"+mDictionaryBean.getBushou()+" ; "+"笔画:"+mDictionaryBean.getBihua());
        for (int i = 0; i < mDictionaryBean.getJijie().size(); i++) {
            jijie=jijie+mDictionaryBean.getJijie().get(i)+"\n";
        }
        mTvJijie.setText(jijie);
        for (int i = 0; i < mDictionaryBean.getXiangjie().size(); i++) {
            xiangJie=xiangJie+mDictionaryBean.getXiangjie().get(i)+"\n";
        }
        mTvXiangjie.setText(xiangJie);
    }

    private void initData(String word) {
        OkHttpUtils.get().url(Url).addParams("key", AppConstant.JUHE_DIR_APPKEY)
                .addParams("word", word)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("查询没有结果");
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
//                    Log.i(TAG,response.toString());
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int error_code = jsonObject.optInt("error_code");
                    if (error_code == 0) {
                        mLayout.setVisibility(View.VISIBLE);
                        JSONObject result = jsonObject.optJSONObject("result");
                        Gson gson = new Gson();
                        mDictionaryBean = gson.fromJson(result.toString(), DictionaryBean.class);
//                        Log.i(TAG,mDictionaryBean.toString());
                        updateView();
                    } else {
                        ToastUtils.showToast(response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    search();
                }
                return false;
            }
        });
    }

    private void search() {
        String keyWord = mEtSearch.getText().toString().trim();
        if(!TextUtils.isEmpty(keyWord)){
            if(keyWord.length()>1){
                ToastUtils.showToast("只能查询一个汉字");
                String word=keyWord.substring(0,1);
                initData(word);
            }else if(keyWord.length()==1){
                initData(keyWord);
            }
        }else {
            ToastUtils.showToast("查询不能空");
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_internet,R.id.iv_search})
    public void onViewClicked(View view) {
        int id = view.getId();
        if(id==R.id.iv_back){
            finish();
        }else if(id==R.id.tv_internet) {
            skipToInternet();
        }else if(id==R.id.iv_search){
            search();
            KeyBoardUtil.closeKeyboard(this );
        }
    }

    private void skipToInternet() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://xh.5156edu.com");
        intent.setData(content_url);
        startActivity(intent);
    }
}
