package com.matrix.yukun.matrix.video_module.play;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyGallaryActivity extends BaseActivity {
    String imagePath= AppConstant.IMAGEPATH;
    String gifPath= AppConstant.GIFLOAD;
    private ImageView mIvBack;
    private GridView mGridView;
    private RelativeLayout mLayout;
    private List<String> mStringList=new ArrayList<>();
    private File mImageFile;
    private File mGifFile;

    public static void start(Context context){
        Intent intent=new Intent(context,MyGallaryActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_my_gallary;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mGridView = findViewById(R.id.grideview);
        mLayout = findViewById(R.id.rl_remind);
        mGridView.setAdapter(new GridAdapter());
        createFile();
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mStringList.get(position).endsWith("gif")){
                    ImageDetailActivity.start(MyGallaryActivity.this,mStringList.get(position),true);
                }else {
                    ImageDetailActivity.start(MyGallaryActivity.this,mStringList.get(position),false);
                }
            }
        });
    }

    @Override
    public void initDate() {
        File[] files = mImageFile.listFiles();
        File[] listFiles = mGifFile.listFiles();
        for (File file:files) {
            if(!file.isDirectory()&&file.getPath().endsWith("gif")||file.getPath().endsWith("png")||file.getPath().endsWith("jpeg")){
                mStringList.add(file.getPath());
            }
        }
        for (int i = 0; i < listFiles.length; i++) {
            if(!listFiles[i].isDirectory()&&i<16){
                mStringList.add(listFiles[i].getPath());
            }
        }
        if(mStringList.size()>0){
            mLayout.setVisibility(View.GONE);
        }
    }

    private void createFile() {
        mImageFile = new File(imagePath);
        mGifFile = new File(gifPath);
        if(!mImageFile.exists()){
            mImageFile.mkdirs();
        }
        if(!mGifFile.exists()){
            mGifFile.mkdirs();
        }
    }

    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mStringList.size();
        }

        @Override
        public Object getItem(int position) {
            return mStringList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = LayoutInflater.from(MyGallaryActivity.this).inflate(R.layout.image_gif_item, null);
            ImageView ivTag=inflate.findViewById(R.id.iv_del);
            ImageView ivImage=inflate.findViewById(R.id.iv_image);
            Glide.with(MyGallaryActivity.this).load(mStringList.get(position)).into(ivImage);
            if(mStringList.get(position).endsWith("gif")){
                ivTag.setVisibility(View.VISIBLE);
            }else {
                ivTag.setVisibility(View.GONE);
            }
            return inflate;
        }
    }
}
