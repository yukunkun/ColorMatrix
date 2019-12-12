package com.matrix.yukun.matrix.tool_module.gif.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.tool_module.gif.adapter.RVGallaryAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GallaryActivity extends BaseActivity {


    private String mFilepath;
    private ImageView mIvBack;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private List<String> mList=new ArrayList<>();
    private RVGallaryAdapter mRvGallaryAdapter;
    private TextView mMIvComp;

    public static void start(Context context, String filePath){
        Intent intent=new Intent(context,GallaryActivity.class);
        intent.putExtra("filepath",filePath);
        ((Activity)context).startActivityForResult(intent,1);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_gallary;
    }

    @Override
    public void initView() {
        mFilepath = getIntent().getStringExtra("filepath");
        mIvBack = findViewById(R.id.iv_back);
        mMIvComp = findViewById(R.id.tv_comp);
        mRecyclerView = findViewById(R.id.recyclerview);
        mGridLayoutManager = new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRvGallaryAdapter = new RVGallaryAdapter(this,mList);
        mRecyclerView.setAdapter(mRvGallaryAdapter);
        mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(5));
    }

    @Override
    public void initDate() {
        File file=new File(mFilepath);
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            mList.add(listFiles[i].getPath());
        }
        mRvGallaryAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(2, intent);
                finish();
            }
        });

        mMIvComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(2, intent);
                finish();
            }
        });
    }
}
