package com.matrix.yukun.matrix.tool_module.gif.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.garrary.adapter.GroupAdapter;
import com.matrix.yukun.matrix.tool_module.garrary.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ImageActivity extends BaseActivity {

    GridView gridView;
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    private List<ImageBean> list;
    public static final int IMAGE=1;
    public static final int VIDEO=2;
    private ArrayList<ArrayList<String>> lists=new ArrayList<>();
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //关闭进度条
//                    mProgressDialog.dismiss();
                    GroupAdapter adapter = new GroupAdapter(ImageActivity.this, list = subGroupOfImage(mGruopMap), gridView);
                    if(subGroupOfImage(mGruopMap)!=null){
                        gridView.setAdapter(adapter);
                    }
                    OverScrollDecoratorHelper.setUpOverScroll(gridView);
                    break;
            }
        }
    };
    private ImageView mIvBack;
    private int mType;

    public static void start(Context context,int type){
        Intent intent=new Intent(context,ImageActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_image;
    }

    @Override
    public void initView() {
        mType = getIntent().getIntExtra("type", 0);
        mIvBack = findViewById(R.id.iv_back);
        gridView= findViewById(R.id.grideview);
        if(mType==IMAGE){
            getImages();
        }else {
            getVideo();
        }
    }

    @Override
    public void initListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if(mType==IMAGE){
                    ChooseImageActivity.start(ImageActivity.this,lists.get(pos),IMAGE);
                }else {
                    ChooseImageActivity.start(ImageActivity.this,lists.get(pos),VIDEO);
                }
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        //显示进度条

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ImageActivity.this.getContentResolver();
                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

                if(mCursor == null){
                    return;
                }

                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    //根据父路径名将图片放入到mGruopMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(path);
                    }
                }

                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0);
                mCursor.close();
            }
        }).start();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getVideo() {
        //显示进度条

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ImageActivity.this.getContentResolver();

                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Video.Media.MIME_TYPE + "=? or "
                                + MediaStore.Video.Media.MIME_TYPE + "=?",
                        new String[] { "video/mp4"}, MediaStore.Video.Media.DATE_MODIFIED);

                if(mCursor == null){
                    return;
                }

                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Video.Media.DATA));

                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    //根据父路径名将图片放入到mGruopMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(path);
                    }
                }

                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0);
                mCursor.close();
            }
        }).start();
    }

    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param mGruopMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap){
        if(mGruopMap.size() == 0){
            return null;
        }
        List<ImageBean> list = new ArrayList<ImageBean>();

        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            //获取数据到详情的页面
            lists.add((ArrayList<String>) value);
            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片

            list.add(mImageBean);
        }
        return list;
    }
}
