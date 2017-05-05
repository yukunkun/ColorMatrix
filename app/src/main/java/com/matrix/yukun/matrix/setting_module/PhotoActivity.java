package com.matrix.yukun.matrix.setting_module;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;

import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity {

    private PhotoView mPhotoView;
    private TextView mTextView;
    private String mPhotoName;
    private String mPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mPhotoName = getIntent().getStringExtra("photoname");
        mPhotoPath = getIntent().getStringExtra("photopath");
        init();
        setInfo();
    }

    private void init() {
        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        mTextView = (TextView) findViewById(R.id.photo_name);
    }

    private void setInfo() {
        mTextView.setText(mPhotoName);
        Glide.with(this).load(mPhotoPath).into(mPhotoView);
    }

    public void PhotoBack(View view) {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
