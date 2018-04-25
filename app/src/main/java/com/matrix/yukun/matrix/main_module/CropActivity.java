package com.matrix.yukun.matrix.main_module;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.selfview.CropImageView;
import com.matrix.yukun.matrix.util.BitmapUtil;
import com.matrix.yukun.matrix.util.ImageUtils;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.ykk.pluglin_video.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropActivity extends BaseActivity {

    private CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        String path=getIntent().getStringExtra("path");
        cropImageView = findViewById(R.id.cropimageview);
        Bitmap bitmap= ImageUtils.getSmallBitmap(path);//图片处理,压缩大小
        cropImageView.setDrawable(BitmapUtil.bigBitmap(bitmap,2,2),
                 bitmap.getWidth()-10,
                bitmap.getHeight()-10);
        findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap cropImage = cropImageView.getCropImage();
                saveBitmap(cropImage);
            }
        });
    }

    private void saveBitmap(Bitmap bitmap) {
        File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()+"/yukun");//设置保存路径
        String name=System.currentTimeMillis()+".png";
        File avaterFile = new File(PHOTO_DIR, name);//设置文件名称

        if(avaterFile.exists()){
            avaterFile.delete();
        }
        try {
            avaterFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(avaterFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.i("--",bitmap.getByteCount()+"");
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(avaterFile.exists()){
            EventBus.getDefault().post(new EventDetail(avaterFile.getPath(),name));
            finish();
        }else {
            ToastUtils.showToast("裁剪失败");
            finish();
        }
    }
}
