package com.matrix.yukun.matrix.leancloud_module.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVAddFriendAdapter;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVAddGroupAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.SearchGroupInfo;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchFriendActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_member)
    RecyclerView rvMember;
    private int mType;  //0 好友 1 群
    private LinearLayoutManager mLinearLayoutManager;
    private RVAddGroupAdapter mRvAddGroupAdapter;
    private RVAddFriendAdapter mRvAddFriendAdapter;
    private List<UserInfoBMob> mUserInfoBMobs = new ArrayList<>();
    private List<SearchGroupInfo> mGroupInfos = new ArrayList<>();

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, SearchFriendActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_search_friend;
    }

    @Override
    public void initView() {
        mType = getIntent().getIntExtra("type", 0);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvMember.setLayoutManager(mLinearLayoutManager);
        if (mType == 1) {
            etSearch.setHint("输入群名称");
            mRvAddGroupAdapter = new RVAddGroupAdapter(R.layout.search_group_item, mGroupInfos);
            rvMember.setAdapter(mRvAddGroupAdapter);
        } else {
            mRvAddFriendAdapter = new RVAddFriendAdapter(this,R.layout.search_friend_item, mUserInfoBMobs);
            rvMember.setAdapter(mRvAddFriendAdapter);
        }
    }

    @Override
    public void initListener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    if(!TextUtils.isEmpty(etSearch.getText().toString().trim())){
                        // search friend
                        if(mType==0){
                            searchFriend(etSearch.getText().toString().trim(), new FindListener<UserInfoBMob>() {
                                @Override
                                public void done(List<UserInfoBMob> list, BmobException e) {
                                    LogUtil.i(list.toString());
                                    if(!list.isEmpty()){
                                        mUserInfoBMobs.clear();
                                        mUserInfoBMobs.addAll(list);
                                        mRvAddFriendAdapter.notifyDataSetChanged();
                                    }else {
                                        Snackbar.make(ll,"搜索结果为空",1500).show();
//                                        ToastUtils.showToast("搜索结果为空");
                                    }
                                }
                            });
                        }
                        //search group
                        else {

                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void initDate() {
        if (mType == 0) {

        }
    }

    public void searchFriend(String str,FindListener<UserInfoBMob> userInfoBMobFindListener) {
        BmobQuery<UserInfoBMob> query = new BmobQuery<UserInfoBMob>();
        //查询playerName叫“比目”的数据
        if(DataUtils.isNumericzidai(str)){
            query.addWhereEqualTo("account", str);
        }else {
            query.addWhereEqualTo("name", str);
        }
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(userInfoBMobFindListener);
    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                break;
        }
    }
}
