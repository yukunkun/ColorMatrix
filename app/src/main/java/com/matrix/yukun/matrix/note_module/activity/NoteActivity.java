package com.matrix.yukun.matrix.note_module.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.note_module.adapter.RVNoteAdapter;
import com.matrix.yukun.matrix.note_module.bean.NoteBean;
import com.matrix.yukun.matrix.util.Base64Encode;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private RecyclerView mRvList;
    private SmartRefreshLayout mSmartLayout;
    private FloatingActionButton mFab;
    private List<NoteBean> mNoteBeans = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private RVNoteAdapter mRvNoteAdapter;
    private TextView mTvRemind;

    @Override
    public int getLayout() {
        return R.layout.activity_note;
    }

    @Override
    public void initView() {
        FileUtil.createFile(AppConstant.NOTEPATH);
        mIvBack = findViewById(R.id.iv_back);
        mSmartLayout = findViewById(R.id.smartrefresh);
        mRvList = findViewById(R.id.recyclerview);
        mTvRemind = findViewById(R.id.tv_remind);
        mFab = findViewById(R.id.fab);
        mSmartLayout.autoRefresh();
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mSmartLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmartLayout.finishRefresh();
            }
        });

        mSmartLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmartLayout.finishLoadMore(20);
            }
        });
    }

    @Override
    public void initDate() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLinearLayoutManager);
        mRvNoteAdapter = new RVNoteAdapter(this, mNoteBeans);
        mRvList.addItemDecoration(new SpacesDoubleDecoration(0,0,0,10));
        mRvList.setAdapter(mRvNoteAdapter);
        readFile();
    }

    private void readFile() {
        File file=new File(AppConstant.NOTEPATH);
        File[] listFiles = file.listFiles();
        mNoteBeans.clear();
        for (int i = 0; i < listFiles.length; i++) {
            NoteBean noteBean=new NoteBean();
            noteBean.setFilePath(listFiles[i].getAbsolutePath());
            String decrypt = Base64Encode.setDecrypt(FileUtil.read(listFiles[i].getAbsolutePath()));
            String[] split = decrypt.split("\n\n");
            noteBean.setTitle(split[0]);
            noteBean.setContent(split[1]);
            noteBean.setName(listFiles[i].getName().substring(0,listFiles[i].getName().length()-4));
            mNoteBeans.add(noteBean);
        }
        Collections.reverse(mNoteBeans);
        if(mNoteBeans.size()>0){
            mTvRemind.setVisibility(View.GONE);
            mRvNoteAdapter.notifyDataSetChanged();
        }else {
            mTvRemind.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        readFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fab:
                NoteEditActivity.start(this);
                break;
           /* case R.id.iv_back:
                finish();
                break;*/
        }
    }
}
