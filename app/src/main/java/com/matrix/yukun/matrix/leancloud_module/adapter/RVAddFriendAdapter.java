package com.matrix.yukun.matrix.leancloud_module.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.leancloud_module.entity.FriendsBMob;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.GsonUtil;
import cn.bmob.v3.listener.FindListener;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVAddFriendAdapter extends BaseQuickAdapter<UserInfoBMob,BaseViewHolder> {

    Context mContext;
    public RVAddFriendAdapter(Context context,int layoutResId, @Nullable List<UserInfoBMob>  data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBMob item) {
        helper.setText(R.id.tv_nickname,item.getName());
        GlideUtil.loadCircleImage(item.getAvator(),helper.getView(R.id.iv_avatar));
        TextView tvAddView = helper.getView(R.id.tv_add);
        isGoodFriend(item,tvAddView);

        if(item.getId().equals(MyApp.getUserInfo().getId())){
            tvAddView.setClickable(false);
            tvAddView.setText("已添加");
            tvAddView.setTextColor(mContext.getResources().getColor(R.color.color_back_more));
            tvAddView.setBackgroundResource(R.drawable.shape_collect_bg_checked);
        }else {
            tvAddView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvAddView.setClickable(false);
                    tvAddView.setText("已申请");
                    tvAddView.setTextColor(mContext.getResources().getColor(R.color.color_back_more));
                    tvAddView.setBackgroundResource(R.drawable.shape_collect_bg_checked);
                    LeanCloudInit.getInstance().sendSystemAdd(item.getId(),UserInfoBMob.toJson(MyApp.getUserInfo()));
                }
            });
        }

    }

    private void isGoodFriend(UserInfoBMob item, TextView tvAddView) {

        BmobQuery<FriendsBMob> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", MyApp.getUserInfo().getId());
        //执行查询方法
        query.findObjects(new FindListener<FriendsBMob>() {
            @Override
            public void done(List<FriendsBMob> list, BmobException e) {
                if(e==null){
                    for (int i = 0; i < list.size(); i++) {
                        ArrayList<String> friendList = list.get(i).getFriendList();
                        for (int j = 0; j < friendList.size(); j++) {
                            String userinfo = friendList.get(i);
                            UserInfoBMob userInfoBMob = (UserInfoBMob) GsonUtil.toObject(userinfo, UserInfoBMob.class);
                            if(item.getId().equals(userInfoBMob.getId())){
                                tvAddView.setClickable(false);
                                tvAddView.setText("已添加");
                                tvAddView.setTextColor(mContext.getResources().getColor(R.color.color_back_more));
                                tvAddView.setBackgroundResource(R.drawable.shape_collect_bg_checked);
                                break;
                            }
                        }
                    }
                }else {
                    ToastUtils.showToast("获取好友失败："+e.toString());
                }
            }
        });

    }
}
