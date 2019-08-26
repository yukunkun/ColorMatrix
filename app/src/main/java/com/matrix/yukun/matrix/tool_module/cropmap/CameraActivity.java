package com.matrix.yukun.matrix.tool_module.cropmap;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.garrary.bean.EventDetail;
import com.matrix.yukun.matrix.util.BitmapUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageViewCamera;
    private ImageView mIvBack;
    private ImageView imageViewOk;
    private ImageView imageViewCancler;
    private String fileName="";
    private RelativeLayout layout;
    private boolean tag=true;
    private String path1="";
    private String mFilePath;
    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_camera;
    }

    @Override
    public void initView() {
        if(tag=true){
            useCamera();
        }
        imageViewCamera = (ImageView) findViewById(R.id.cameraimageview);
        mIvBack = (ImageView) findViewById(R.id.cameraback);
        imageViewOk = (ImageView) findViewById(R.id.cameraok);
        imageViewCancler = (ImageView) findViewById(R.id.cameracancler);
        layout = (RelativeLayout) findViewById(R.id.deal);
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        imageViewOk.setOnClickListener(this);
        imageViewCancler.setOnClickListener(this);
    }

    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileNames = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        mFile = new File( AppConstant.IMAGEPATH+"/"+ fileNames + ".jpg");
        mFile.getParentFile().mkdirs();
        path1 = mFile.getAbsolutePath();
        fileName=fileNames + ".jpg";
        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
        Uri uri = FileProvider.getUriForFile(this, "com.xykj.customview.fileprovider", mFile);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cameraback:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.cameracancler:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
        super.onActivityResult(requestCode, resultCode, data);
        imageViewCamera.setImageResource(R.mipmap.beijing_1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (requestCode == 1&&options!=null) {
            String imagePath = BitmapUtil.compressImage(mFile.getAbsolutePath());
            imageViewCamera.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            path1=mFile.getPath();
        }else {
            finish();
        }
    }
}
