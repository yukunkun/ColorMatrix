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
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeanBaseActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.rv_chatview)
    CubeRecyclerView rvChatview;
    @BindView(R.id.sr_refresh)
    CubeSwipeRefreshLayout srRefresh;
    @BindView(R.id.et_messg)
    CubeEmoticonEditText etMessg;
    @BindView(R.id.send_btn)
    Button sendBtn;
    @BindView(R.id.bt_add)
    Button btAdd;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.iv_emoji)
    ImageView ivEmoji;
    @BindView(R.id.select_img_rv)
    RecyclerView selectImgRv;
    @BindView(R.id.image_album)
    TextView imageAlbum;
    @BindView(R.id.image_editor)
    TextView imageEditor;
    @BindView(R.id.cb_origin)
    CheckBox cbOrigin;
    @BindView(R.id.btn_send_photo)
    Button btnSendPhoto;
    @BindView(R.id.bottom_bar)
    RelativeLayout bottomBar;
    @BindView(R.id.select_pic_view)
    RelativeLayout selectPicView;
    @BindView(R.id.fl_contain)
    FrameLayout flContain;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private ContactInfo mData;

    @Override
    public int getLayout() {
        return R.layout.activity_lean_base;
    }

    @Override
    public void initView() {
        mData = (ContactInfo) getIntent().getSerializableExtra("data");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
}
