package com.matrix.yukun.matrix.camera_module;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.util.FileUtil;

import org.greenrobot.eventbus.EventBus;




public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageViewCamera;
    private ImageView imageViewBack;
    private ImageView imageViewOk;
    private ImageView imageViewCancler;
    private String fileName="";
    private String path=null;
    private RelativeLayout layout;
    private boolean tag=true;
    private BitmapFactory.Options options = new BitmapFactory.Options();


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
        if("null".equals(path)){
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
            Bundle extras = data.getExtras();
            Bitmap photo=null;
            Log.i("===","path+fileNam"+data.getData()+"--"+data.getExtras());
            if(extras!=null){
                photo = (Bitmap) extras.get("data");
                imageViewCamera.setImageBitmap(photo);// 将图片显示在ImageView里
            }
           /* if (photo!=null&&!photo.equals("null")) {
//                Log.i("===photo",data.getData()+"=");
                imageViewCamera.setImageBitmap(photo);// 将图片显示在ImageView里
            }else*/
            if(data.getData()!=null){
                Uri uri = data.getData();
                if((data.getData()+"").startsWith("file")){
                    Bitmap bitmapCopy= BitmapFactory.decodeFile((data.getData()+"").substring(8,(data.getData()+"").length()),options).copy(Bitmap.Config.ARGB_4444,true);
                    imageViewCamera.setImageBitmap(bitmapCopy);// 将图片显示在ImageView里
                    path=(data.getData()+"").substring(8,(data.getData()+"").length());
                    int nameCount = path.lastIndexOf("/");
                    fileName=path.substring(nameCount+1,path.length());
                    Log.i("===",path+"+"+fileName);
//                        FileUtil.deleteFile((data.getData()+"").substring(8,(data.getData()+"").length()));
                    return;
                }else {
                    String imagePath = null;
                    Cursor cursor = getContentResolver().query(uri, null, null, null,null);
                    if (cursor != null && cursor.moveToFirst()) {
                        imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }
                    Bitmap bitmapCopy=BitmapFactory.decodeFile(imagePath,options).copy(Bitmap.Config.ARGB_4444,true);
                    imageViewCamera.setImageBitmap(bitmapCopy);// 将图片显示在ImageView里
                    path=imagePath;
                    int nameCount = path.lastIndexOf("/");
                    fileName=path.substring(nameCount+1,path.length());
                    Log.i("===1",path+"+"+fileName);
                }
            }else {
                if(data.getData()==null||getContentResolver().query(data.getData(), null, null, null,null)==null){
                    imageViewCamera.setImageResource(R.mipmap.beijing_1);
                    Toast.makeText(CameraActivity.this, "获取图片失败，请从相册选择！", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        }
    }
}
