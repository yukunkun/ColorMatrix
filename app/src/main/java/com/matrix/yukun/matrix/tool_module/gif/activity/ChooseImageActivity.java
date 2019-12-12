package com.matrix.yukun.matrix.tool_module.gif.activity;

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
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.gif.adapter.RVImageAdapter;
import com.matrix.yukun.matrix.tool_module.gif.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

public class ChooseImageActivity extends BaseActivity {

    ArrayList<String> mGruopMap;
    private RecyclerView mRecyclerView;
    private RVImageAdapter mRvImageAdapter;
    private List<ImageBean> mImageBeans=new ArrayList<>();
    private TextView mComplete;
    private ImageView mIvBack;
    private List<String> mImageChoose;
    private int mType;

    public static void start(Context context, ArrayList<String> mGruopMap,int type){
        Intent intent=new Intent(context,ChooseImageActivity.class);
        intent.putStringArrayListExtra("photo",mGruopMap);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_choose_image;
    }

    @Override
    public void initView() {
        mGruopMap = getIntent().getStringArrayListExtra("photo");
        mType = getIntent().getIntExtra("type",0);
        mRecyclerView = findViewById(R.id.recyclerview);
        mComplete = findViewById(R.id.tv_complete);
        mIvBack = findViewById(R.id.iv_back);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRvImageAdapter = new RVImageAdapter(this,mImageBeans);
        mRecyclerView.setAdapter(mRvImageAdapter);
        if(mType==2){
            mRvImageAdapter.setCount(1);
        }else {
            mRvImageAdapter.setCount(9);
        }
        mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(5));
    }

    @Override
    public void initDate() {
        for (String s: mGruopMap) {
            mImageBeans.add(new ImageBean(s,false));
        }
        mRvImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRvImageAdapter.setOnClickListener(new RVImageAdapter.OnClickListener() {
            @Override
            public void onClickListener(List<String> list) {
                if(list.size()==0){
                    mComplete.setTextColor(getResources().getColor(R.color.color_eaffd9));
                }else {
                    mComplete.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                mComplete.setText("完成("+list.size()+")");
                mImageChoose=list;
            }

            @Override
            public void onClickCheckBoxListener(int pos, boolean isCheck) {
                mImageBeans.get(pos).setChecked(isCheck);
                mRvImageAdapter.update(mImageBeans);
            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageChoose==null||mImageChoose.size()==0){
                    ToastUtils.showToast("请选择文件");
                }else {
                    if(mType==1){
                        CropGifImageActivity.start(ChooseImageActivity.this, (ArrayList<String>) mImageChoose);
                    }else {
                        GIFVideoClipActivity.start(ChooseImageActivity.this,mImageChoose.get(0));
                    }
                }
            }
        });
    }
}
