package com.matrix.yukun.matrix.tool_module.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;

import java.io.File;
import java.util.HashMap;

public class MyQRCodeActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvQrCode;
    int width=300;      //图片的宽度
    int height=300;     //图片的高度
    private static final int IMAGE_HALFWIDTH = 0;//宽度值，影响中间图片大小
    private ImageView mIvBack;
    HashMap hints=new HashMap();
    private Button mBtDownload;
    private Button mBtShare;
    private TextView mTvProduct;
    private EditText mEditText;
    private Bitmap mBitmap;
    private Handler mHandler=new Handler();
    private Bitmap mViewQRBitmap;

    @Override
    public int getLayout() {
        return R.layout.activity_my_qrcode;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initView() {
        mIvQrCode = findViewById(R.id.iv_qr_code);
        mIvBack = findViewById(R.id.iv_back);
        mBtShare = findViewById(R.id.bt_share);
        mBtDownload = findViewById(R.id.bt_download);
        mTvProduct = findViewById(R.id.tv_product);
        mEditText = findViewById(R.id.et_content);
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //指定字符编码为“utf-8”
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);  //指定二维码的纠错等级为中级
        hints.put(EncodeHintType.MARGIN, 1);    //设置图片的边距
    }

    private void createQrCode(String content) {
        try {
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.tool_icon );
            mBitmap = BitMatrixToBitmap(bitMatrix, bmp);
            mIvQrCode.setImageBitmap(mBitmap);
            ToastUtils.showToast("生成专属二维码成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mBtShare.setOnClickListener(this);
        mBtDownload.setOnClickListener(this);
        mTvProduct.setOnClickListener(this);
        mIvQrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_qr_code:
                mViewQRBitmap= ImageUtils.createViewBitmap(mIvQrCode, mIvQrCode.getWidth(), mIvQrCode.getHeight());
                ImageDialog instance = ImageDialog.getInstance(mViewQRBitmap);
                instance.show(getFragmentManager(),"");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_product:
                if(!TextUtils.isEmpty(mEditText.getText().toString().trim())){
                    createQrCode(mEditText.getText().toString().trim());
                }else {
                    ToastUtils.showToast("您的链接为空呦");
                }
                break;
            case R.id.bt_share:
                String imageDisplayname=System.currentTimeMillis()+".png";
                mViewQRBitmap = ImageUtils.createViewBitmap(mIvQrCode, mIvQrCode.getWidth(), mIvQrCode.getHeight());
                FileUtil.loadImage(mViewQRBitmap,imageDisplayname);
                shareImage(mViewQRBitmap,imageDisplayname);
                break;
            case R.id.bt_download:
                String imageName=System.currentTimeMillis()+".png";
                mViewQRBitmap = ImageUtils.createViewBitmap(mIvQrCode, mIvQrCode.getWidth(), mIvQrCode.getHeight());
                FileUtil.loadImage(mViewQRBitmap,imageName);
                ToastUtils.showToast("下载成功");
                break;
        }
    }

    private void shareImage(final Bitmap bitmap, final String photoName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File destDir= FileUtil.createFile();
                FileUtil.loadImage(bitmap,photoName);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = FileUtil.shareMsg(destDir+"/"+photoName);
                        startActivity(Intent.createChooser(intent, "分享图片"));
                    }
                });
            }
        }).start();

    }

    private Bitmap BitMatrixToBitmap(BitMatrix matrix, Bitmap mBitmap) {
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                    //该位置用于存放图片信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }



    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;

    }

}
