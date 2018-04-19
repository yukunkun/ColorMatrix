package com.ykk.pluglin_video.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.utils.CameraSizeUtils;
import com.ykk.pluglin_video.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by yukun on 17-9-29.
 */

public class CamerPresent implements CameraControler.presenr{
    CameraControler.view mView;
    SurfaceView mSurfaceview;
    private Camera mCamera;
    boolean isBackCamera=true;
    private SurfaceHolder mHolder;
    private Camera.Parameters mParams;
    private List<Camera.Size> mPreviewSizes;
    private float mRatio;
    private MediaRecorder mediaRecorder;
    private File tempFile;
    private final int maxDurationInMs = 20000;
    private final long maxFileSizeInBytes = 500000;
    private final int videoFramesPerSecond = 20;

    public CamerPresent(CameraControler.view view) {
        mView = view;
    }

    @Override
    public void init(SurfaceView mSurfaceview) {
        this.mSurfaceview=mSurfaceview;
        mHolder = mSurfaceview.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamer((Context) mView);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //保持屏幕的高亮
        mSurfaceview.setKeepScreenOn(true);
    }

    @Override
    public void openCamer(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mRatio = width / height;
//        final float ratio = (float) mSurfaceview.getWidth() / mSurfaceview.getHeight();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        //线程安全问题。
       cameraInit();
    }

    private void cameraInit() {
        if(mCamera==null){
            Toast.makeText((Context) mView, "请在设置界面给予照相机权限", Toast.LENGTH_SHORT).show();
            return;
        }
        mParams = mCamera.getParameters();
        //前置的话，设置焦点要报错
        if(isBackCamera){
            mParams.setPictureFormat(ImageFormat.JPEG);
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.cancelAutoFocus();
        }
        mCamera.setDisplayOrientation(0);

        mPreviewSizes = mParams.getSupportedPreviewSizes();
        Camera.Size mBestPictureSize = null;

        // 设置pictureSize
        List<Camera.Size> pictureSizes = mParams.getSupportedPictureSizes();
        if (mBestPictureSize == null) {
            mBestPictureSize = CameraSizeUtils.findBestPictureSize(pictureSizes, mParams.getPictureSize(), mRatio);
        }
        mParams.setPictureSize(mBestPictureSize.width, mBestPictureSize.height);

        Camera.Size optionSize = CameraSizeUtils.getOptimalPreviewSize(mPreviewSizes, mSurfaceview.getWidth(), mSurfaceview.getHeight());//获取一个最为适配的camera.size
        mParams.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
        //获取值
        getSupportedWhiteBalance();
        getSupportedColorEffects();
        getSupportedSceneModes();
        //设置值回去
        mCamera.setParameters(mParams);
        //回调
        mCamera.setPreviewCallback(new Camera.PreviewCallback() { //获取相机的data

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {  //这里一直在执行

            }
        });

        try {
            mCamera.setPreviewDisplay(mSurfaceview.getHolder());
            mCamera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// [none, mz_lively, mz_moonlight, mz_black_and_white, mz_blue, mz_pink, mz_film, mz_natural,
// mz_dawn, mz_sakura, mz_dreamy, mz_bright, maroon, black_and_white, film, oxidize, leaf, violet, light_yellow,
// azure, viridity, wood, pale_green, fading, corn_field, olive_yellow, toy, pale_blue, clay_bank, sunny, moon,
// dew, aqua, vine, sky, peach, sunny_2rd, jade, wood_2rd, orange, ash]

    // sceneModes: [auto, portrait, landscape, night, night-portrait, theatre, beach, snow, sunset,
    // steadyphoto, fireworks, sports, party, candlelight, hdr]
//       whiteBalance: [auto, incandescent, fluorescent, warm-fluorescent, daylight, cloudy-daylight, twilight, shade]
    @Override
    public void closeCamer() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public boolean startRecording(int recordQuality){
        int quality=CamcorderProfile.QUALITY_480P;
        switch (recordQuality){
            case 0:
                quality=CamcorderProfile.QUALITY_1080P;
                break;
            case 1:
                quality=CamcorderProfile.QUALITY_720P;
                break;
            case 2:
                quality=CamcorderProfile.QUALITY_480P;
                break;
            case 3:
                quality=CamcorderProfile.QUALITY_CIF;
                break;
            case 4:
                quality=CamcorderProfile.QUALITY_HIGH;
                break;
        }
        try {
            mCamera.unlock();

            mediaRecorder = new MediaRecorder();

            mediaRecorder.setCamera(mCamera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            mediaRecorder.setMaxDuration(maxDurationInMs);
            tempFile = new File(FileUtils.PHOTO_PATH,FileUtils.getVideoFileName());
            mediaRecorder.setOutputFile(tempFile.getPath());

//            mediaRecorder.setVideoFrameRate(30); // 视频帧频率
//            mediaRecorder.setVideoFrameRate(videoFramesPerSecond);

//            mediaRecorder.setVideoSize(mSurfaceview.getWidth(), mSurfaceview.getHeight());
//            mediaRecorder.setVideoSize(ScreenUtils.instance().getWidth((Context) mView), ScreenUtils.instance().getHeight((Context) mView));

//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

            mediaRecorder.setProfile(CamcorderProfile.get(quality));

            mediaRecorder.setPreviewDisplay(mSurfaceview.getHolder().getSurface());

//            mediaRecorder.setMaxFileSize(maxFileSizeInBytes);

            mediaRecorder.prepare();

            mediaRecorder.start();

            return true;
        } catch (IllegalStateException e) {
//            Log.e(TAG,e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
//            Log.e(TAG,e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean stopRecording() {
        if(mediaRecorder!=null){
            mediaRecorder.stop();
            mCamera.lock();
            return true;
        }
        return false;
    }

    public void getSupportedWhiteBalance(){
        List<String> whiteBalance = mParams.getSupportedWhiteBalance();
//        Log.i("----whiteBalance",whiteBalance.toString()+"");
        mView.initLayout(whiteBalance);
    }

    public void getSupportedColorEffects(){
        List<String> colorEffects = mParams.getSupportedColorEffects();
//        Log.i("----colorEffects",colorEffects.toString());
        mView.initCameraList(colorEffects);
    }

    public void getSupportedSceneModes(){
        List<String> sceneModes = mParams.getSupportedSceneModes();
//        Log.i("----sceneModes",sceneModes.toString());
        mView.initScreenLayout(sceneModes);
    }

    public void setSupportedWhiteBalance(String value){
        mParams.setWhiteBalance(value);
        mCamera.setParameters(mParams);
    }

    public void setSupportedSceneModes(String value){
        mParams.setSceneMode(value);
        mCamera.setParameters(mParams);
    }

    public void setSupportedColorEffects(String value){
        mParams.setWhiteBalance(value);
        mCamera.setParameters(mParams);
    }

    int voiceLable;
    public void setVoiceDev(int dev){
        this.voiceLable=dev;
    }
    /**
     * 拍照
     */
    @Override
    public void getPhoto(){
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if(voiceLable==2){
                }else if(voiceLable==0){
                    MediaPlayer mediaPlayer=MediaPlayer.create((Context) mView, R.raw.camera_1);
                    mediaPlayer.start();
                }else if(voiceLable==1){
                    MediaPlayer mediaPlayer=MediaPlayer.create((Context) mView, R.raw.camera_2);
                    mediaPlayer.start();
                }

                FileOutputStream fos = null;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                String photoFileName = FileUtils.getPhotoFileName();
                try {
                    fos = new FileOutputStream(FileUtils.PHOTO_PATH+"/"+photoFileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.write(data);
                    fos.flush();
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    Toast.makeText((Context) mView, "拍照成功", Toast.LENGTH_SHORT).show();
                    try {
                        if(fos!=null){
                            fos.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    mCamera=null;
                    mParams=null;
                    if(isBackCamera){
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//打开当前选中的摄像头
                    }else {
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//打开当前选中的摄像头
                    }
                    cameraInit();
                }
            }
        });
    }

    @Override
    public void changeCamera() {
            Camera.CameraInfo cameraInfo=new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
//            Camera.getCameraInfo(cameraCount,cameraInfo);
//            for(int i = 0; i < cameraCount; i++ ) {
                //现在是后置，变更为前置
            if (isBackCamera) {//代表摄像头的方位，CAMERA_FACING_FRONT前置  CAMERA_FACING_BACK后置
                    isBackCamera = false;
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mParams=null;
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//打开当前选中的摄像头
                    cameraInit();
                } else {
                    //现在是前置， 变更为后置
//                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                        isBackCamera = true;
                        mCamera.stopPreview();//停掉原来摄像头的预览
                        mCamera.release();//释放资源
                        mCamera = null;//取消原来摄像头
                        mParams=null;
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//打开当前选中的摄像头
                        cameraInit();
//                    }
//                }
         }
    }
    boolean isOpenBline;
    @Override
    public void openBline() {
        if(!isOpenBline){
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParams);
        }else {
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParams);
        }
        isOpenBline=!isOpenBline;

    }
}
