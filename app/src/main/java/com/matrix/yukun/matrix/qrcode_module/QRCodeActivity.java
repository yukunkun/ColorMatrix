package com.matrix.yukun.matrix.qrcode_module;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.qrcode_module.Util.DecodeUtils;
import com.matrix.yukun.matrix.qrcode_module.camera.CameraManager;
import com.matrix.yukun.matrix.qrcode_module.cropper.RGBLuminanceSource;
import com.matrix.yukun.matrix.qrcode_module.decoding.CaptureActivityHandler;
import com.matrix.yukun.matrix.qrcode_module.decoding.InactivityTimer;
import com.matrix.yukun.matrix.qrcode_module.view.ViewfinderView;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Initial the camera
 */
public class QRCodeActivity extends Activity implements Callback {
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private TextView               myQrCodeTv;
    private boolean                hasSurface;
    private Vector<BarcodeFormat>  decodeFormats;
    private String                 characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer            mediaPlayer;
    private boolean                playBeep;
    private String                 photo_path;
    private String                 crop_photo_path;
    private static final float BEEP_VOLUME  = 0.10f;
    private static final int   REQUEST_CODE = 100;
    public static final  int   CROPIMAGES   = 4;
    private boolean    vibrate;

    private static ScanFinishListener mScanFinishListener;//扫描完成监听
    private ScanOption mScanOption;//用于设置扫描框样式
    private TextView mTvPhoto;
    private ImageView mIvBack;
    private SurfaceHolder mSurfaceHolder;
    private TextView mTvContent;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        mSurfaceHolder = surfaceView.getHolder();

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        myQrCodeTv = (TextView) findViewById(R.id.tv_my_qr_code);
        mTvContent = findViewById(R.id.tv_qr_content);
        mIvBack = findViewById(R.id.iv_back);
        mTvPhoto = findViewById(R.id.tv_photo);
        mScanFinishListener=new ScanFinishedListener(this);
        mScanOption = new ScanOption();
        mScanOption.lineDrawable = R.mipmap.ic_scan_line;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        setScanOptions();
        initListener();
    }

    private void initListener() {
        mTvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                }
                else {
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myQrCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QRCodeActivity.this,MyQRCodeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setScanOptions() {
        if (mScanOption != null) {
            viewfinderView.setCornerColor(mScanOption.cornerColor);
            viewfinderView.setMaskColor(mScanOption.maskColor);
            viewfinderView.setPointColor(mScanOption.pointColor);
            viewfinderView.setLineColor(mScanOption.lineColor);
            viewfinderView.setLineDrawable(mScanOption.lineDrawable);
        }
    }


    /**
     * 获取传来的参数
     */

    @Override
    protected void onResume() {
        super.onResume();
        if (hasSurface) {
            initCamera(mSurfaceHolder);
        }
        else {
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        mTvContent.setText("扫面结果："+resultString);
        if (mScanFinishListener != null) {
            mScanFinishListener.onScanFinish(resultString, this);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取选中图片的路径
                    try {
                        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                        if (cursor.moveToFirst()) {
                            photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                        startCrop(photo_path);
                    } catch (Exception e) {
                        ToastUtils.showToast("未识别的二维码");
                    }
                    break;
            }
        }
        else if (resultCode == 20) {
            switch (requestCode) {
                case CROPIMAGES:
                    try {
                        if (data != null) {
                            crop_photo_path = data.getStringExtra("path");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Result result = scanningImage(crop_photo_path);
                                        if (result != null) {
                                            final String recode = recode(result.toString());
                                            Intent data = new Intent();
                                            data.putExtra("result", recode);
                                            //UI线程
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mTvContent.setText("扫面结果："+recode.toString());
                                                }
                                            });
                                            if (mScanFinishListener != null) {
                                                mScanFinishListener.onScanFinish(recode, QRCodeActivity.this);
                                            }
                                        }
//                                        else {
//                                            String result2 = scanningImage2(crop_photo_path);
//                                            if (result2 != null) {
//                                                String recode = recode(result2.toString());
//                                                Intent data = new Intent();
//                                                data.putExtra("result", recode);
//                                                if (mScanFinishListener != null) {
//                                                    mScanFinishListener.onScanFinish(recode, QRCodeActivity.this);
//                                                }
//                                                setResult(300, data);
//                                                finish();
//                                            } else {
//                                                Looper.prepare();
//                                                Toast.makeText(getApplicationContext(), "未发现图中二维码", Toast.LENGTH_SHORT).show();
//                                                Looper.loop();
//                                            }
//                                        }
                                        else {
                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "未发现图中二维码", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    } catch (Exception e) {
//                        ToastUtils.showToast("未识别的二维码");
                    }
                    break;
            }
        }
    }

    public void startCrop(String path) {
        Intent intent = new Intent();
        intent.putExtra("path", path);
        intent.putExtra("flag", false);
        intent.setClass(this, QRImageCropActivity.class);
        startActivityForResult(intent, CROPIMAGES);
    }

    Bitmap scanBitmap;

    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        options.inJustDecodeBounds = false; // 获取新大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String scanningImage2(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        String resultZbar = new DecodeUtils(DecodeUtils.DECODE_DATA_MODE_ALL).decodeWithZbar(scanBitmap);
        return resultZbar;
    }

    private String recode(String str) {
        String formart = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            }
            else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }
}
