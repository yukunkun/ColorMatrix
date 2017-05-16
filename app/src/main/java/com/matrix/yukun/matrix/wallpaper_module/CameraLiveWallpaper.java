package com.matrix.yukun.matrix.wallpaper_module;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.matrix.yukun.matrix.MyApp;

import java.io.IOException;

public class CameraLiveWallpaper extends WallpaperService {
    // 实现WallpaperService必须实现的抽象方法  
    public Engine onCreateEngine() {
        // 返回自定义的CameraEngine
        return new CameraEngine();
    }


    class CameraEngine extends Engine /*implements Camera.PreviewCallback*/ {
        private Camera camera;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            startPreview();
            // 设置处理触摸事件  
            setTouchEventsEnabled(true);

        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            // 时间处理:点击拍照,长按拍照
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        /**
         * 开始预览
         */
        public void startPreview() {
            try{
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                Camera.Parameters params = camera.getParameters();
                params.setPictureFormat(ImageFormat.JPEG);
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(getSurfaceHolder());
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        camera.addCallbackBuffer(data);
                    }
                });
                camera.startPreview();

            }catch (Exception e){
                //开始
                MyApp.showToast("请在手机权限设置打开相机权限");
                e.printStackTrace();
            }
        }

        /**
         * 停止预览
         */
        public void stopPreview() {
            if (camera != null) {
                try {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    // camera.lock();
                    camera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                camera = null;
            }
        }

//        @Override
//        public void onPreviewFrame(byte[] bytes, Camera camera) {
//            camera.addCallbackBuffer(bytes);
//        }
    }
}  