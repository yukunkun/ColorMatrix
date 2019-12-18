package com.matrix.yukun.matrix.leancloud_module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.emoji.CubeEmoticonEditText;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.leancloud_module.InputPanelManager;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeanBaseActivity extends BaseActivity implements InputListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.rv_chatview)
    CubeRecyclerView rvChatview;
    @BindView(R.id.sr_refresh)
    CubeSwipeRefreshLayout srRefresh;
    @BindView(R.id.ll_root)
    LinearLayout mLayoutRoot;

    private ContactInfo mData;
    private InputPanelManager mInputPanelManager;

    @Override
    public int getLayout() {
        return R.layout.activity_lean_base;
    }

    @Override
    public void initView() {
        mData = (ContactInfo) getIntent().getSerializableExtra("data");
        mInputPanelManager = new InputPanelManager(this,mLayoutRoot,this);
    }


    @OnClick({R.id.iv_back, R.id.iv_emoji, R.id.image_editor, R.id.cb_origin, R.id.btn_send_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_emoji:
                break;
            case R.id.image_editor:
                break;
            case R.id.cb_origin:
                break;
            case R.id.btn_send_photo:
                break;
        }
    }

    @Override
    public void initListener() {
        rvChatview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    rvChatview.post(new Runnable() {
                        @Override
                        public void run() {
//                            if (mChatAdapter.getItemCount() > 0) {
//                                mRvChatview.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
//                            }
                        }
                    });
                }
            }
        });
        //点击
        this.rvChatview.setEventListener(new CubeRecyclerView.OnEventListener() {
            @Override
            public void onStartTouch() {
                mInputPanelManager.dismissLayout();
            }
        });
    }

    @Override
    public void onSendMessageClick(String msg) {

    }

    @Override
    public void onPictureClick(List<Photo> picPath) {

    }

    @Override
    public void onBottomMove(int position) {

    }
}
