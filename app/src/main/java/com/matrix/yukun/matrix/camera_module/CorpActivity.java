package com.matrix.yukun.matrix.camera_module;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.edmodo.cropper.CropImageView;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.EventByte;

import org.greenrobot.eventbus.EventBus;

public class CorpActivity extends BaseActivity {

    private CropImageView cropImageView;
    private SeekBar seekBar;
    private String imagePath;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private ImageView imageViewSure;
    private ImageView imageViewCancler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corp);
        imagePath = getIntent().getStringExtra("imagepath");
        init();
        cropImageView();
        setListener();
    }

    private void init() {
        cropImageView = (CropImageView) findViewById(R.id.cropimageview);
        seekBar = (SeekBar) findViewById(R.id.cropseekbar);
        seekBar.setMax(250);
        seekBar.setProgress(125);
        imageViewSure = (ImageView) findViewById(R.id.cameraok);
        imageViewCancler = (ImageView) findViewById(R.id.cameracancler);
    }

    //裁剪图片
    private void cropImageView() {
        if(imagePath==null||imagePath.length()<=0){
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options).copy(Bitmap.Config.ARGB_8888,true);
        cropImageView.setImageBitmap(bitmap);
        cropImageView.setAspectRatio(125, 50);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setGuidelines(1);
    }

    private void setListener() {
        imageViewSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap=cropImageView.getCroppedImage();
                EventBus.getDefault().post(new EventByte(bitmap));
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        imageViewCancler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(b){
                    cropImageView.setAspectRatio(progress+1, 50);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void CropBack(View view) {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
