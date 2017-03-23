package com.matrix.yukun.matrix.camera_module;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.util.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageViewCamera;
    private ImageView imageViewBack;
    private ImageView imageViewOk;
    private ImageView imageViewCancler;
    private String fileName="";
    private RelativeLayout layout;
    private boolean tag=true;
    private String path1="";
    private String mFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
        setListener();
        if(tag=true){
            openCamera();
        }
    }

    private void init() {
        imageViewCamera = (ImageView) findViewById(R.id.cameraimageview);
        imageViewBack = (ImageView) findViewById(R.id.cameraback);
        imageViewOk = (ImageView) findViewById(R.id.cameraok);
        imageViewCancler = (ImageView) findViewById(R.id.cameracancler);
        layout = (RelativeLayout) findViewById(R.id.deal);
    }

    private void openCamera() {
        tag = false;
        //设置自定义存储路径
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +AppConstants.PATH;
        //存储文件夹操作
        File outFilePath = new File(mFilePath);
        if (!outFilePath.exists()) {
            outFilePath.mkdirs();
        }
        //设置自定义照片的名字
        String fileNames = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mFilePath = mFilePath + "/" + fileNames + ".jpg";
        path1 = mFilePath;
        fileName=fileName + ".jpg";
        File outFile = new File(mFilePath);
        Uri uri = Uri.fromFile(outFile);
        //拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);

    }

    private void setListener() {
        imageViewBack.setOnClickListener(this);
        imageViewOk.setOnClickListener(this);
        imageViewCancler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cameraback:
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
                break;
            case R.id.cameracancler:
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
                break;
            case R.id.cameraok:
                layout.setVisibility(View.VISIBLE);
                backSend();
                break;
        }
    }

    private void backSend() {
        if("null".equals(path1)){
            imageViewCamera.setImageResource(R.mipmap.beijing_1);
            Toast.makeText(CameraActivity.this, "获取图片失败，请从相册选择！", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    EventBus.getDefault().post(new EventDetail(path1,fileName));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        imageViewCamera.setImageResource(R.mipmap.beijing_1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (requestCode == 1&&options!=null) {
            Bitmap bitmapCopy=BitmapFactory.decodeFile(path1,options).copy(Bitmap.Config.ARGB_4444,true);
            imageViewCamera.setImageBitmap(bitmapCopy);// 将图片显示在ImageView里
        }

    }
}
