package com.matrix.yukun.matrix.main_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.CameraSizeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaceActivity extends AppCompatActivity {


    @BindView(R.id.tv_forget)
    TextView mTvForget;
    @BindView(R.id.svan_camera)
    SurfaceView mSvCamera;
    @BindView(R.id.bt_reset)
    Button mBtReset;
    @BindView(R.id.waterloading)
    RelativeLayout mWaterloading;
    private AlertDialog mAlertDialog;
    private Camera mCamera;
    private Camera.Size mBestPreviewSize;
    private FaceRequest mFace;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        takePhoto();
                    }
                }).start();
                mWaterloading.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        ButterKnife.bind(this);
        initSurface();
    }

    private void initSurface() {
        mSvCamera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                closeCamer();
            }
        });
        mSvCamera.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void closeCamer() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    private void initCamera() {
        final float ratio = (float) mSvCamera.getWidth() / mSvCamera.getHeight();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        Camera.Parameters params = mCamera.getParameters();
        params.setPictureFormat(ImageFormat.JPEG);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setDisplayOrientation(90);

        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        Camera.Size mBestPictureSize = null;

        // 设置pictureSize
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        if (mBestPictureSize == null) {
            mBestPictureSize = CameraSizeUtils.findBestPictureSize(pictureSizes, params.getPictureSize(), ratio);
        }
        params.setPictureSize(mBestPictureSize.width, mBestPictureSize.height);

        // 设置previewSize
        if (mBestPreviewSize == null) {
            mBestPreviewSize = CameraSizeUtils.findBestPreviewSize(previewSizes, params.getPreviewSize(),
                    mBestPictureSize, ratio);
        }
        params.setPreviewSize(mBestPreviewSize.width, mBestPreviewSize.height);
        //设置surface的值
        ViewGroup.LayoutParams param = mSvCamera.getLayoutParams();
        param.height = mSvCamera.getWidth() * mBestPreviewSize.width / mBestPreviewSize.height;
        mSvCamera.setLayoutParams(param);

        //设置值回去
//        mCamera.setParameters(params);
        //回调
        mCamera.setPreviewCallback(new Camera.PreviewCallback() { //获取相机的data

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {  //这里一直在执行

            }
        });

        try {
            mCamera.setPreviewDisplay(mSvCamera.getHolder());
            mCamera.startPreview();

            //自动拍照
            mHandler.sendEmptyMessageDelayed(1, 1500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takePhoto() {
        if (mCamera == null) {
            return;
        }
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //上传
                onTakePhoto(data);
            }
        });
    }

    private void onTakePhoto(byte[] data) {
        byte[] bytes = CameraSizeUtils.compressBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), 100);

        String mAuthId = isFace();
        mFace = new FaceRequest(this);
        // 设置业务类型为验证
        mFace.setParameter(SpeechConstant.WFR_SST, "verify");
        // 设置auth_id
        mFace.setParameter(SpeechConstant.AUTH_ID, mAuthId);
        // imgData 为图片的二进制数据
        mFace.sendRequest(bytes, new RequestListener() {
            @Override
            public void onEvent(int i, Bundle bundle) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                String ret = new String(bytes);
                try {
                    mWaterloading.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(ret);
                    Double rst = jsonObject.optDouble("score");
                    MyApp.showToast("相似度: " + rst + "%");
                    Intent intent = new Intent(FaceActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //成功后存储数据
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (speechError != null) {
                    mWaterloading.setVisibility(View.GONE);
                    MyApp.showToast("验证错误:" + speechError.toString() + " 请正对镜头重试");
                }
            }
        });
    }

    private String isFace() {
        SharedPreferences preferences = getSharedPreferences("mAuthId", Context.MODE_PRIVATE);
        String result = preferences.getString("mAuthId", "a");
        return result;
    }

    @OnClick({R.id.tv_forget, R.id.bt_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                resetSecret();
                break;
            case R.id.bt_reset:
                if (null != mCamera) {
                    mCamera.stopPreview();
                    mCamera.startPreview();
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                }
                break;
        }
    }

    public void FaceBack(View view) {
        finish();
    }


    private void resetSecret() {
        mAlertDialog.dismiss();
        new AlertDialog.Builder(this).setTitle("是否重置密码")
                .setMessage("重置密码你的记录将会清零")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApp.showToast("已重置手势密码,请重新进入APP");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamer();
    }
}
