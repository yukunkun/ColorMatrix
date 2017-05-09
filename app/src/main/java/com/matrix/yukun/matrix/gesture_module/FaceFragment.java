package com.matrix.yukun.matrix.gesture_module;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.CameraSizeUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yukun on 17-5-8.
 */
public class FaceFragment extends Fragment {
    @BindView(R.id.sv_camera)
    SurfaceView mSvCamera;
    @BindView(R.id.bt_cancle)
    Button mBtCancle;
    @BindView(R.id.bt_sure)
    Button mBtSure;
    @BindView(R.id.cancel_face)
    TextView mCancelFace;
    private Camera mCamera;
    private Camera.Size mBestPreviewSize;
    private FaceRequest mFace;
    private String mAuthId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_face, null);
        ButterKnife.bind(this, inflate);
        initSurface();
        return inflate;
    }

    private void initSurface() {
        mSvCamera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamer();
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

    private void openCamer() {
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
        mCamera.setParameters(params);
        //回调
        mCamera.setPreviewCallback(new Camera.PreviewCallback() { //获取相机的data

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {  //这里一直在执行

            }
        });

        try {
            mCamera.setPreviewDisplay(mSvCamera.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.bt_cancle, R.id.bt_sure,R.id.cancel_face})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancle:
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.startPreview();
                }
//                EventBus.getDefault().post(new OnEventFinish(1));
                break;
            case R.id.bt_sure:
                takePhoto();
                break;
            case R.id.cancel_face:
                setSharePrefress("mAuthId","a");
                MyApp.showToast("取消成功");
                EventBus.getDefault().post(new OnEventFinish(1));
                break;
        }
    }

    private void takePhoto() {
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

        long longtime = System.currentTimeMillis();//获取当前时间
        mAuthId = "a" + longtime;

        mFace = new FaceRequest(getContext());
        // 设置业务类型为注册
        mFace.setParameter(SpeechConstant.WFR_SST, "reg");
        // 设置auth_id
        mFace.setParameter(SpeechConstant.AUTH_ID, mAuthId);
        // imgData 为图片的二进制数据，listener 为处理注册结果的回调对象
        mFace.sendRequest(data, new RequestListener() {
            @Override
            public void onEvent(int i, Bundle bundle) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                String ret = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(ret);
                    String rst = jsonObject.optString("rst");
                    MyApp.showToast("验证成功 " + rst);
                    //成功后存储数据
                    setSharePrefress("mAuthId", mAuthId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (speechError != null) {
                    MyApp.showToast("识别错误:" + speechError.toString() + " 请重试");
                }
            }
        });
    }

    private void setSharePrefress(String tag, String str) {
        SharedPreferences sp = getActivity().getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag, str);
        editor.commit();
    }

}
