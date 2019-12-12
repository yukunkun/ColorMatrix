package com.matrix.yukun.matrix.tool_module.gif.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.gif.adapter.RVCropAdapter;
import com.matrix.yukun.matrix.tool_module.gif.bean.ImageBean;
import com.matrix.yukun.matrix.tool_module.gif.dialog.LoadDialog;
import com.matrix.yukun.matrix.tool_module.qrcode.cropper.BitmapUtil;
import com.matrix.yukun.matrix.tool_module.qrcode.cropper.CropImageView;
import com.matrix.yukun.matrix.tool_module.qrcode.cropper.FileDownloadUtil;
import com.matrix.yukun.matrix.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class CropGifImageActivity extends BaseActivity implements View.OnClickListener {

    ArrayList<String> mGruopMap;
    private RecyclerView mRecyclerView;
    private List<ImageBean> mImageBeans=new ArrayList<>();
    private RVCropAdapter mRvCropAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private CropImageView mCropImageView;
    int screen_width;
    int screen_height;
    int mCurrentPos;
    private TextView mTvComp;
    private ImageView mIvBack;
    private Long mFilePath;
    int mAspectRatioX=1;
    int mAspectRatioY=1;
    private PopupWindow mWindow;
    private String mFirstImage;
    private LinearLayout mAnchor;
    private LoadDialog mLoadDialog;
    private boolean isAnyRatio;
    private int mWidth;
    private int mHeight;


    public static void start(Context context, ArrayList<String> mGruopMap){
        Intent intent=new Intent(context,CropGifImageActivity.class);
        intent.putStringArrayListExtra("photo",mGruopMap);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_crop_gif_image;
    }

    @Override
    public void initView() {
        mGruopMap=getIntent().getStringArrayListExtra("photo");
        mFilePath = System.currentTimeMillis();
        mRecyclerView = findViewById(R.id.recyclerview);
        mCropImageView = findViewById(R.id.CropImageView);
        mIvBack = findViewById(R.id.iv_back);
        mAnchor = findViewById(R.id.ll);
        mTvComp = findViewById(R.id.tv_complete);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mRvCropAdapter = new RVCropAdapter(this,mImageBeans);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRvCropAdapter);
        mLoadDialog = LoadDialog.getInstance();
        mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(4));
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, LinearLayoutManager.VERTICAL);
    }

    @Override
    public void initDate() {
        for (String s: mGruopMap) {
            mImageBeans.add(new ImageBean(s,false));
        }
        mRvCropAdapter.notifyDataSetChanged();
        mFirstImage = mGruopMap.get(0);
        cropImage(mFirstImage);
        //view可见后
        mAnchor.post(new Runnable(){
            @Override
            public void run(){
                // 构造和展现弹出窗口
                setCropRatio();
            }
        });
    }

    private void cropImage(String pre_path){
        Bitmap bitmap = null;
        if (pre_path != null) {
            bitmap = BitmapUtil.getBitmapFromSDCard(pre_path);
        } else {
            bitmap = BitmapUtil.temp;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;

        if (bitmap != null) {
            bitmap = resizeSurfaceWithScreen(bitmap, screen_width, screen_height);
            int degree = readPictureDegree(pre_path);
            bitmap = rotaingImageView(degree, bitmap);
            mCropImageView.setImageBitmap(bitmap);
        }
        //比例

        mCropImageView.setFixedAspectRatio(!isAnyRatio); //任意大小的裁剪
        mCropImageView.setAspectRatio(mAspectRatioX, mAspectRatioY);
    }

    private void setCropRatio(){
        // 用于PopupWindow的View
        View contentView= LayoutInflater.from(this).inflate(R.layout.pop_ratio_layout, null, false);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        mWindow = new PopupWindow(contentView, ScreenUtils.instance().getWidth(this)*3/4, ScreenUtils.instance().getHeight(this)*1/2, true);
        contentView.findViewById(R.id.tv_1to1).setOnClickListener(this);
        contentView.findViewById(R.id.tv_any).setOnClickListener(this);
        contentView.findViewById(R.id.tv_1to2).setOnClickListener(this);
        contentView.findViewById(R.id.tv_2to1).setOnClickListener(this);
        contentView.findViewById(R.id.tv_4to3).setOnClickListener(this);
        contentView.findViewById(R.id.tv_16to9).setOnClickListener(this);
        // 设置PopupWindow的背景
        mWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_1e000000)));
        // 设置PopupWindow是否能响应外部点击事件
        mWindow.setOutsideTouchable(false);
        // 设置PopupWindow是否能响应点击事件
        mWindow.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        mWindow.showAtLocation(mAnchor, Gravity.CENTER,0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
    }
    private void setStartGIF(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("制作GIF")
                .setCancelable(false)
                .setMessage("是否开始制作GIF")
                .setPositiveButton("开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showToast("制作中，请稍等");
                        mLoadDialog.show(getSupportFragmentManager(),"");
                        GIFImageProActivity.start(CropGifImageActivity.this, AppConstant.GIFPATH+"/"+mFilePath,mCropImageView.getWidth(),mCropImageView.getHeight());
                        mLoadDialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file=new File(AppConstant.GIFPATH+"/"+mFilePath);
                file.delete();
                finish();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_any:
                isAnyRatio=true;
                mAspectRatioX=1;
                mAspectRatioY=1;
                break;
            case R.id.tv_1to1:
                mAspectRatioX=1;
                mAspectRatioY=1;
                break;
            case R.id.tv_1to2:
                mAspectRatioX=1;
                mAspectRatioY=2;
                break;
            case R.id.tv_2to1:
                mAspectRatioX=2;
                mAspectRatioY=1;
                break;
            case R.id.tv_4to3:
                mAspectRatioX=4;
                mAspectRatioY=3;
                break;
            case R.id.tv_16to9:
                mAspectRatioX=16;
                mAspectRatioY=9;
                break;
        }
        cropImage(mFirstImage);
        if(mWindow!=null&&mWindow.isShowing()){
            mWindow.dismiss();
        }
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(AppConstant.GIFPATH+"/"+mFilePath);
                file.delete();
                finish();
            }
        });
        //完成
        mTvComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadDialog.show(getSupportFragmentManager(),"");
                if(mCurrentPos<mGruopMap.size()){
                    mImageBeans.get(mCurrentPos).setChecked(true);
                    mRvCropAdapter.notifyDataSetChanged();
                    saveToSDCard(mCurrentPos);
                    mCurrentPos++;
                    if(mCurrentPos<mGruopMap.size()){
                        cropImage(mGruopMap.get(mCurrentPos));
                        mTvComp.setText("完成("+(mCurrentPos)+")");
                    }else {
                        setStartGIF();
                        mTvComp.setText("制作GIF");
                    }
                    mLoadDialog.dismiss();
                }else {
                    GIFImageProActivity.start(CropGifImageActivity.this, AppConstant.GIFPATH+"/"+mFilePath,mWidth,mHeight);
                    mLoadDialog.dismiss();
                    finish();
                }
            }
        });
    }

    private void saveToSDCard(int currentPos) {
        Bitmap croppedImage = mCropImageView.getCroppedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        croppedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        mWidth = croppedImage.getWidth();
        mHeight = croppedImage.getHeight();
        FileUtil.loadTempImage(croppedImage,mFilePath+"",currentPos+".png");
    }

    private Bitmap resizeSurfaceWithScreen(Bitmap bitmap, int screen_width, int screen_height) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width < screen_width && height < screen_height) {
            float scale_width = screen_width * 1.0f / width;
            float scale_height = screen_height * 1.0f / height;
            float scale = scale_width > scale_height ? scale_height : scale_width; // ???�?�????
            width *= scale;
            height *= scale;
        } else {
            if (width > screen_width) {
                height = height * screen_width / width;
                width = screen_width;
            }
            if (height > screen_height) {
                width = width * screen_height / height;
                height = screen_height;
            }
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        bitmap = BitmapUtil.zoomBitmap(bitmap, width, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        while (baos.toByteArray().length > 1024 * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap_new = BitmapFactory.decodeStream(isBm, null, null);

        return bitmap_new;

    }

    public String saveBitmap(Bitmap bm) {

        Long tolong = System.currentTimeMillis() / 1000;
        File f = new File(FileDownloadUtil.getDefaultLocalDir("/Scan/temp/"), tolong.toString());
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, out);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (baos.toByteArray().length > 1024 * 1024) {
                baos.reset();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public void gotoNextStep() {
        try {
            Bitmap croppedImage = mCropImageView.getCroppedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            croppedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] bitmapByte = baos.toByteArray();

            int width = croppedImage.getWidth();
            int heigth = croppedImage.getHeight();

            Intent data = new Intent();
            data.putExtra("bitmap", bitmapByte);
            data.putExtra("path", saveBitmap(croppedImage));
            data.putExtra("width", width);
            data.putExtra("heigth", heigth);
            setResult(20, data);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
