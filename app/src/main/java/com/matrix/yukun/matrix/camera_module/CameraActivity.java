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


public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageViewCamera;
    private ImageView imageViewBack;
    private ImageView imageViewOk;
    private ImageView imageViewCancler;
    private String fileName="";
    private RelativeLayout layout;
    private boolean tag=true;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private String path1=null;


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
        tag=false;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            String out_file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+AppConstants.PATH+"/";
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileName=System.currentTimeMillis() + ".jpg";
            path1 = out_file_path + fileName;
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path1)));
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(getImageByCamera, 1);
        }
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
        if (requestCode == 1) {
            Bitmap bitmapCopy=BitmapFactory.decodeFile(path1,options).copy(Bitmap.Config.ARGB_4444,true);
            imageViewCamera.setImageBitmap(bitmapCopy);// 将图片显示在ImageView里

        }

    }
}
