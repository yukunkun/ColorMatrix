package com.matrix.yukun.matrix.camera_module;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.matrix.yukun.matrix.image_module.bean.EventDetail;

import org.greenrobot.eventbus.EventBus;




public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageViewCamera;
    private ImageView imageViewBack;
    private ImageView imageViewOk;
    private ImageView imageViewCancler;
    private String fileName;
    private String path;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
        setListener();
        openCamera();
    }

    private void init() {
        imageViewCamera = (ImageView) findViewById(R.id.cameraimageview);
        imageViewBack = (ImageView) findViewById(R.id.cameraback);
        imageViewOk = (ImageView) findViewById(R.id.cameraok);
        imageViewCancler = (ImageView) findViewById(R.id.cameracancler);
        layout = (RelativeLayout) findViewById(R.id.deal);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
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
        if("null".equals(path)||fileName.length()<1){
            Toast.makeText(CameraActivity.this, "获取图片失败，请从相册选择！", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    EventBus.getDefault().post(new EventDetail(path,fileName));
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
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }

            Uri uri = data.getData();
            if(data.getData()==null||getContentResolver().query(uri, null, null, null,null)==null){
                imageViewCamera.setImageResource(R.mipmap.beijing_1);
                Toast.makeText(CameraActivity.this, "获取图片失败，请从相册选择！", Toast.LENGTH_SHORT).show();
                return;
            }
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
            }

            int nameCount = path.lastIndexOf("/");
            fileName=path.substring(nameCount+1,path.length());
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            imageViewCamera.setImageBitmap(bitmap);// 将图片显示在ImageView里
        }
    }
}
