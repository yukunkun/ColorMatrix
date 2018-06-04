package com.matrix.yukun.matrix.main_module;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.adapter.RecAdapter;
import com.matrix.yukun.matrix.anims.MyEvaluator;
import com.matrix.yukun.matrix.bean.EventMorePos;
import com.matrix.yukun.matrix.bean.EventPos;
import com.matrix.yukun.matrix.camera_module.CameraActivity;
import com.matrix.yukun.matrix.image_module.activity.PhotoListActivity;
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.main_module.filters.IImageFilter;
import com.matrix.yukun.matrix.main_module.filters.Image;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.selfview.VerticalSeekBar;
import com.matrix.yukun.matrix.selfview.WaterLoadView;
import com.matrix.yukun.matrix.selfview.view.MyRelativeLayout;
import com.matrix.yukun.matrix.setting_module.SettingActivity;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.matrix.yukun.matrix.util.BitmapUtil;
import com.matrix.yukun.matrix.util.DeskMapUtil;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.matrix.yukun.matrix.weather_module.WeatherActivity;
import com.tencent.bugly.beta.Beta;
import com.ykk.pluglin_video.play.PlayActivity;
import com.ykk.pluglin_video.play.PlayMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageLoad;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecAdapter recAdapter;
    private boolean check=false;
    private LinearLayout layout;
    private ImageView imageViewMore;
    private String path;
    private String photoName;
    private ImageView imageViewTest;
    private PopupWindow mPopupWindow;
    private SeekBar seekBarBaoHe;
    private SeekBar seekBarLight;
    private Bitmap mBitCompress;
    private Bitmap mBitOrigin;
    private Bitmap mBitPath;
    private Bitmap mBitSeek;
    private int pos;
    private RelativeLayout textViewTiShi;
    private int rotate=0;
    private ImageView textRotate;
    private Bitmap mBitRotate;
    private int roate=0;
    private ValueAnimator animator;
    private MyRelativeLayout layoutContain;
    private boolean flag=true;
    private boolean isShow=false;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private Handler handler=new Handler();
    private TextView textViewRoate;
    private ImageView imageViewCrop;
    private RelativeLayout reaContain;
    private Bitmap bitmap;
    private TextView textViewTag;
    private TextView textViewMov;
    private TextView textViewWea;
    private TextView textViewMovRec;
    private TextView textViewSetting;
    private ImageView imageViewBack;
    private boolean mIsMenuOpen=false;
    private int radias=180; //动画半径
    private RecyclerView mRecyclerFilter;
    private FilterAdapter mFilterAdapter;
    private LinearLayoutManager mLinearLayout;
    private WaterLoadView mWaterLoadView;
    private FloatingActionButton mActionsMenu;
    private TextView tvVideo;
    private LinearLayout llCamera;
    private LinearLayout llPhoto;
    private LinearLayout mLlContain;
    private TextView mTvRotate;
    private VerticalSeekBar mVerticalSeekBar;
    private ImageView mIvRotate;
    private ImageView mIvMorebig;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        init();
        setAdapter();
        setListener();
        judgeTishi();
        getPermission();
        Beta.checkUpgrade();
    }

    private void init() {
        imageViewTest = (ImageView) findViewById(R.id.iv_image);
        imageViewTest.setImageResource(R.mipmap.beijing_1);
        imageLoad = (ImageView) findViewById(R.id.loadimage);
        layout = (LinearLayout) findViewById(R.id.linfuntion);
        imageViewMore = (ImageView) findViewById(R.id.imagemore);
        llCamera = (LinearLayout) findViewById(R.id.ll_camera);
        mWaterLoadView = (WaterLoadView) findViewById(R.id.waterload);
        imageViewCrop = (ImageView) findViewById(R.id.imagecrop);
        llPhoto = (LinearLayout) findViewById(R.id.ll_photo);
        textViewRoate = (TextView) findViewById(R.id.tishi);
        textViewTag = (TextView) findViewById(R.id.tishitag);
        textViewMov = (TextView)findViewById(R.id.textmovie);
        textViewWea = (TextView)findViewById(R.id.textweather);
        imageViewBack = (ImageView) findViewById(R.id.back);
        textViewMovRec = (TextView)findViewById(R.id.textmovierec);
        textViewSetting = (TextView)findViewById(R.id.textseting);
        textViewTiShi = (RelativeLayout) findViewById(R.id.texttishi);
        reaContain = (RelativeLayout) findViewById(R.id.contain);
        layoutContain=(MyRelativeLayout)findViewById(R.id.my_relat);
        tvVideo = findViewById(R.id.tv_video);
        mLlContain = findViewById(R.id.ll_rotato);
        mTvRotate = findViewById(R.id.tv_rotate);
        mIvRotate = findViewById(R.id.iv_rotate);
        mIvMorebig = findViewById(R.id.image_big);
        mVerticalSeekBar = findViewById(R.id.vertical_seek);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mLinearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerFilter = (RecyclerView) findViewById(R.id.recyclerfilter);
        mActionsMenu = (FloatingActionButton) findViewById(R.id.fl_btn);
        recyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerFilter.setLayoutManager(mLinearLayout);
        setConLayout();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView)findViewById(R.id.scrollview));
    }

    //计算高度
    private void setConLayout() {
        int height= ScreenUtils.instance().getHeight(this);
        int width= ScreenUtils.instance().getWidth(this);
        ViewGroup.LayoutParams layoutParams =reaContain.getLayoutParams();
        layoutParams.height= (int) (height*0.55);
        reaContain.setLayoutParams(layoutParams);
        radias=width/4;   //菜单的半径为屏幕高度的1/4
        mVerticalSeekBar.setMaxProgress(20);
        mVerticalSeekBar.setProgress(1);
    }

    private void setAdapter() {
        recAdapter = new RecAdapter(getApplicationContext(),check);
        recyclerView.setAdapter(recAdapter);
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));

        mFilterAdapter = new FilterAdapter(this);
        mRecyclerFilter.setAdapter(mFilterAdapter);
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerFilter, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        mRecyclerFilter.addItemDecoration(new SpacesItemDecoration(20));

    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getPos(EventPos event){
        pos = event.position;
        recAdapter.notifyDataSetChanged();
        setBitmapColor(pos);
    }
    //更多滤镜
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getMorePos(EventMorePos event){
        int posmore = event.pos;
        detailBitmap(posmore);


    }

    private void detailBitmap(int posmore) {
        if(mBitPath==null){
            Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        //得到不同的ColorMatrix,并且返回回来
        IImageFilter filter = (IImageFilter) mFilterAdapter.getItem(posmore);
        new processImageTask(filter,mBitOrigin).execute();
    }


    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
        private IImageFilter filter;
        private Bitmap images;
        public processImageTask(IImageFilter imageFilter,Bitmap images) {
            this.filter = imageFilter;
            this.images=images;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWaterLoadView.setVisibility(View.VISIBLE);
        }

        public Bitmap doInBackground(Void... params) {
            Image img = null;
            try {
//                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.image);
                img = new Image(images);
                if (filter != null) {
                    img = filter.process(img);
                    img.copyPixelsFromBuffer();
                }
                return img.getImage();
            }
            catch(Exception e){
                if (img != null && img.destImage.isRecycled()) {
                    img.destImage.recycle();
                    img.destImage = null;
                    System.gc();
                }
            }
            finally{
                if (img != null && img.image.isRecycled()) {
                    img.image.recycle();
                    img.image = null;
                    System.gc();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null){
                mWaterLoadView.setVisibility(View.GONE);
                super.onPostExecute(result);
                imageViewTest.setImageBitmap(result);
                mBitSeek=result;//进度的Bitmap
                mBitRotate=result;//旋转的Bitmap
                Bitmap bitmap= ImageUtils.compressBitmap(result);//图片处理,压缩大小
                mBitCompress= BitmapUtil.mTempBit(bitmap);
                ViewGroup.LayoutParams layoutParams=imageViewTest.getLayoutParams();
                layoutParams.width=width;
                layoutParams.height=height;
                imageViewTest.setLayoutParams(layoutParams);
            }
//            textView.setVisibility(View.GONE);
        }
    }

    private void setBitmapColor(int pos) {
        if(mBitPath==null){
            Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        //得到不同的ColorMatrix,并且返回回来
        ColorMatrix colorMatrix = BitmapUtil.matrixTrans(pos);
        handleColorRotateBmp(colorMatrix, mBitPath) ;
    }
    //图片回调
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getDetail(EventDetail event){
        path = event.path;
        photoName = event.photoName;
        if(path==null||path.length()==0){
            return;
        }
        imageViewCrop.setVisibility(View.GONE);
        textViewTiShi.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        imageViewMore.setVisibility(View.VISIBLE);
        detailImage();
    }

    private void detailImage() {

        if(mBitOrigin!=null){
            mBitOrigin.recycle();
        }
//        //压缩大小
//        Bitmap bitmapCopy=BitmapFactory.decodeFile(path,options).copy(Bitmap.Config.ARGB_4444,true);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmapCopy.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
////        Log.i("-----M",baos.toByteArray().length / 1024/1024+"");
//        if(baos.toByteArray().length / 1024/1024>15){
//            Toast.makeText(MainActivity.this, "图片过大,请选择较小图片", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(bitmapCopy.getWidth()>2048){
//            //裁剪尺寸
//            Bitmap bitmap = FileUtil.compressImage(bitmapCopy).copy(Bitmap.Config.ARGB_8888,true);
//            int height = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
//            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, height, true);
//            mBitOrigin=scaled;
//        }else {
//            mBitOrigin=bitmapCopy;
//        }

        Bitmap bitmap= ImageUtils.getSmallBitmap(path);//图片处理,压缩大小
        mBitOrigin=bitmap;
        mBitCompress = Bitmap.createBitmap(mBitOrigin.getWidth(), mBitOrigin.getHeight(),
                Bitmap.Config.ARGB_4444);
        mBitPath=Bitmap.createBitmap(mBitOrigin.getWidth(), mBitOrigin.getHeight(),
                Bitmap.Config.ARGB_4444);
        mBitSeek=mBitOrigin;
        mBitRotate=mBitOrigin;
        //得到第一个原图
        ColorMatrix colorMatrix = BitmapUtil.matrixTrans(0);
        handleColorRotateBmp(colorMatrix, mBitPath) ;
    }


    public void getCrop(Bitmap bitmap){
        if(mBitOrigin!=null){
            mBitOrigin.recycle();
        }
        mBitOrigin=bitmap;
        mBitCompress = Bitmap.createBitmap(mBitOrigin.getWidth(), mBitOrigin.getHeight(),
                Bitmap.Config.ARGB_4444);
        mBitPath=Bitmap.createBitmap(mBitOrigin.getWidth(), mBitOrigin.getHeight(),
                Bitmap.Config.ARGB_4444);
        mBitSeek=mBitOrigin;
        mBitRotate=mBitOrigin;
        //得到第一个原图
        ColorMatrix colorMatrix = BitmapUtil.matrixTrans(0);
        handleColorRotateBmp(colorMatrix, mBitPath) ;
    }

    //从新绘制
    private void handleColorRotateBmp(ColorMatrix colorMatrix,Bitmap bitmaps){
        Bitmap bitmap = BitmapUtil.handleColorRotateBmp(colorMatrix, mBitOrigin, bitmaps);
        imageViewTest.setImageBitmap(bitmap);
        mBitSeek=bitmap;//进度的Bitmap
        mBitRotate=bitmap;//旋转的Bitmap
        mBitCompress= BitmapUtil.mTempBit(bitmap);
    }
    //旋转
    private void handleColorRoate(int progress){
        if(mBitRotate==null){
            return;
        }
        Bitmap bitmap = BitmapUtil.rotateBitmap(mBitRotate, progress);
        imageViewTest.setImageBitmap(null);
        imageViewTest.setImageBitmap(bitmap);
        mBitCompress= BitmapUtil.mTempBit(bitmap);
        mBitSeek=bitmap;//进度的Bitmap
        mTvRotate.setText(90+roate%4*90+"°");
    }

    private void setListener() {
        imageViewMore.setOnClickListener(this);
        textViewTiShi.setOnClickListener(this);
        imageViewMore.setOnClickListener(this);
        imageLoad.setOnClickListener(this);
        mIvRotate.setOnClickListener(this);
        textViewRoate.setOnClickListener(this);
        textViewTag.setOnClickListener(this);
        imageViewCrop.setOnClickListener(this);
        textViewMov.setOnClickListener(this);
        textViewWea.setOnClickListener(this);
        textViewMovRec.setOnClickListener(this);
        textViewSetting.setOnClickListener(this);
        mActionsMenu.setOnClickListener(this);
        tvVideo.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llPhoto.setOnClickListener(this);
        mIvMorebig.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imagemore:
                //popuwindow的展示
                if(!isShow){
                    if(mLlContain.getVisibility()==View.VISIBLE){
                        mLlContain.setVisibility(View.GONE);
                    }
                    showMore();
                    isShow=true;
                }else {
                    mPopupWindow.dismiss();
                    isShow=false;
                }
                break;
            case R.id.texttishi:
                Intent intent_1=new Intent(MainActivity.this, PhotoListActivity.class);
                startActivity(intent_1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.ll_photo:
                Intent intent=new Intent(MainActivity.this, PhotoListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            break;
            case R.id.textmovie:
                //电影推荐
                closeMenu();
                Intent intentWeaRec =new Intent(MainActivity.this, MovieActivity.class);
                startActivity(intentWeaRec);
                break;
            case R.id.textweather:
                //天气提醒
                closeMenu();
                Intent intentWea=new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intentWea);
                break;
            case R.id.textmovierec:
               //空
                break;
            case R.id.textseting:
                //设置
                closeMenu();
                Intent intentSet=new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSet);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.loadimage:
                flag=false;
                //load
                if(path==null){
                    if(photoName==null||photoName.length()==0){
                        Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mLlContain.setVisibility(View.GONE);
                bitmap = ImageUtils.createViewBitmap(layoutContain, layoutContain.getWidth(), layoutContain.getHeight());
                Toast.makeText(MainActivity.this, "正在下载...", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.loadImage(bitmap, photoName);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                                flag=true;
                            }
                        });
                    }
                }).start();
                break;
                //变大的contain
            case R.id.image_big:
                if(mPopupWindow!=null&&mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    isShow=!isShow;
                }
                if(mLlContain.getVisibility()==View.VISIBLE){
                    mLlContain.setVisibility(View.GONE);
                }else {
                    mLlContain.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_rotate:
                handleColorRoate(roate);
                roate++;
                break;
            case R.id.tishi:
                saveSharePreferrence();
                textViewRoate.setVisibility(View.GONE);
                textViewTag.setVisibility(View.GONE);
                break;
            case R.id.tishitag:
                saveSharePreferrence();
                textViewRoate.setVisibility(View.GONE);
                textViewTag.setVisibility(View.GONE);
                break;
            case R.id.imagecrop:
                Intent intent2 = new Intent(this,CropActivity.class);
                intent2.putExtra("path",path);
                startActivity(intent2);
//                Intent intent2 = new Intent("com.android.camera.action.CROP");
//                intent2.setDataAndType(Uri.fromFile(new File(path)), "image/*");
//                intent2.putExtra("crop", "true");
//                intent2.putExtra("scale", true);
//                intent2.putExtra("return-data", false);//默认不输出.防止图片失真
//                intent2.putExtra("noFaceDetection", true);
//                startActivityForResult(intent2, 1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.ll_camera:
                startCamera();
                break;
            case R.id.fl_btn:
                //分享
                File destDir= FileUtil.createFile();
                if(photoName==null||photoName.length()==0){
                    Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLlContain.setVisibility(View.GONE);
                final File f = new File(destDir, photoName);
                Toast.makeText(MainActivity.this, "正在下载...", Toast.LENGTH_SHORT).show();
                flag=false;
                bitmap = ImageUtils.createViewBitmap(layoutContain, layoutContain.getWidth(), layoutContain.getHeight());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.loadImage(bitmap,photoName);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                                share(f.getPath());
                                flag=true;
                            }
                        });
                    }
                }).start();
                break;
            case R.id.tv_video:
                Intent iVideo=new Intent(this, PlayMainActivity.class);
                startActivity(iVideo);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
        }
        mVerticalSeekBar.setOnSlideChangeListener(new VerticalSeekBar.SlideChangeListener() {
            @Override
            public void onStart(VerticalSeekBar slideView, int progress) {

            }

            @Override
            public void onProgress(VerticalSeekBar slideView, int progress) {
                if(mBitRotate==null){
                    return;
                }
                Bitmap bitmap = BitmapUtil.bigBitmap(mBitRotate,progress/10f+0.1f,progress/10f+0.1f);

                ViewGroup.LayoutParams layoutParams=imageViewTest.getLayoutParams();
                layoutParams.width=bitmap.getWidth();
                layoutParams.height=bitmap.getHeight();
                imageViewTest.setLayoutParams(layoutParams);
                width=bitmap.getWidth();
                height=bitmap.getHeight();
                imageViewTest.setImageBitmap(null);
                imageViewTest.setImageBitmap(bitmap);
                mBitCompress= BitmapUtil.mTempBit(bitmap);
                mBitSeek=bitmap;//进度的Bitmap
                mBitPath=bitmap;
            }

            @Override
            public void onStop(VerticalSeekBar slideView, int progress) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&data!=null){
            photoName=System.currentTimeMillis() + ".jpg";
            if(data.getData()!=null){
                Uri uri = data.getData();
                if((data.getData()+"").startsWith("file")){
                    Bitmap bitmapCopy=BitmapFactory.decodeFile((data.getData()+"").substring(8,(data.getData()+"").length()),options).copy(Bitmap.Config.ARGB_4444,true);
                    getCrop(bitmapCopy);
                    FileUtil.deleteFile((data.getData()+"").substring(8,(data.getData()+"").length()));
                    return;
                }else {
                    if(data.getData()==null||getContentResolver().query(uri, null, null, null,null)==null){
                        Toast.makeText(MainActivity.this, "获取图片失败，请从相册选择！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String imagePath = null;
                    Cursor cursor = getContentResolver().query(uri, null, null, null,null);
                    if (cursor != null && cursor.moveToFirst()) {
                        imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }
                    Bitmap bitmap= ImageUtils.getSmallBitmap(imagePath);//图片处理,压缩大小                    Bitmap bitmap= ImageUtils.compressBitmap(bitmapCopy);//图片处理,压缩大小
                    getCrop(BitmapUtil.bigBitmap(bitmap,2,2));
                    FileUtil.deleteFile(imagePath);
                }
            }else if(data.getExtras()!=null){

                Bitmap photo = (Bitmap) (data.getExtras().get("data"));
                if(photo!=null){
                    //图片失真太严重
                    getCrop(BitmapUtil.bigBitmap(photo,2,2));
                }else {
                    MyApp.showToast("裁剪失败!");
                }
            }
        }
    }

    //打开相机
    private void startCamera() {
        Intent intent=new Intent(this, CameraActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void saveSharePreferrence() {
        SharedPreferences sp = getSharedPreferences("share", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("tishi",true);
        editor.commit();
    }
    private void judgeTishi() {
        SharedPreferences preferences=getSharedPreferences("share", Context.MODE_PRIVATE);
        boolean auto = preferences.getBoolean("tishi", false);
        if(auto){
            textViewRoate.setVisibility(View.GONE);
            textViewTag.setVisibility(View.GONE);
        }
    }

    private void judgeDesk() {
        SharedPreferences preferences=getSharedPreferences("desk", Context.MODE_PRIVATE);
        boolean auto = preferences.getBoolean("desk", false);
        if(!auto){
            //创建快捷图标
            DeskMapUtil.createShortCut(getApplicationContext());
            //保存图标tag
            SharedPreferences sp = getSharedPreferences("desk", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("desk",true);
            editor.commit();
        }
    }

    private void share(String filePath){
        Intent intentShare = FileUtil.shareMsg(filePath);
        startActivity(Intent.createChooser(intentShare, "分享图片"));
    }
    //share的动画
    private void addAnimation() {
        animator = ValueAnimator.ofInt(0,-600,0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
//                layoutMore.layout(layoutMore.getLeft(),animatedValue,layoutMore.getRight(),layoutMore.getHeight()+animatedValue);
            }
        });

        animator.setDuration(1000);
        animator.setInterpolator(new BounceInterpolator());
        animator.setEvaluator(new MyEvaluator());
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(layoutMore.getVisibility()==View.VISIBLE){
//            layoutMore.setVisibility(View.GONE);
//        }
        return super.onTouchEvent(event);
    }

    private void showMore() {
        View view = View.inflate(MainActivity.this, R.layout.popuwindow,null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        seekBarBaoHe = (SeekBar) view.findViewById(R.id.seekbarbaohe);
        seekBarLight = (SeekBar) view.findViewById(R.id.seekbarlight);
        textRotate = (ImageView) view.findViewById(R.id.rotate);
        seekBarBaoHe.setMax(200);
        seekBarBaoHe.setProgress(100);
        seekBarLight.setMax(20);
        seekBarLight.setProgress(1);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.color_000000_alpha));

        if (mPopupWindow !=null&&!mPopupWindow.isShowing()) {
            int width = ScreenUtils.instance().getWidth(getApplicationContext());
            mPopupWindow.showAsDropDown(imageViewMore,-width,0);
        }
        seekBarListener();
    }

    private void seekBarListener() {
        seekBarBaoHe.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(b){
                    if(mBitPath!=null){
                        Bitmap bitmap = BitmapUtil.handleColorBmp(mBitCompress, mBitSeek, progress, rotate);
                        imageViewTest.setImageBitmap(bitmap);
                        mBitRotate=bitmap;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(b){
                    if(mBitPath!=null){
                        Bitmap bitmap = BitmapUtil.handleColorMatrixBmp(mBitCompress, mBitSeek, progress);
                        imageViewTest.setImageBitmap(bitmap);
                        mBitRotate=bitmap;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //色彩的变化
        textRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rotate==2){
                    rotate=0;
                }else {
                    rotate++;
                }
                //toast
                FileUtil.showToast(MainActivity.this,rotate);
            }
        });
    }
    
    public void Back(View view) {
        if (!mIsMenuOpen) {
            mIsMenuOpen = true;
            if(textViewSetting.getVisibility()==View.GONE){
                textViewSetting.setVisibility(View.VISIBLE);
                textViewWea.setVisibility(View.VISIBLE);
                textViewMov.setVisibility(View.VISIBLE);
                textViewMovRec.setVisibility(View.VISIBLE);
            }
            openMenu();
        } else {
            closeMenu();
        }
    }

    private void openMenu() {
        AnimUtils.doAnimateOpen(textViewSetting, 0, 4, radias,500);
        AnimUtils.doAnimateOpen(textViewWea, 1, 4, radias,420);
        AnimUtils.doAnimateOpen(textViewMov, 2, 4, radias,340);
        AnimUtils.doAnimateOpen(textViewMovRec, 3, 4, radias,260);
        AnimUtils.setSettingDown(this,imageViewBack);
    }
    private void closeMenu() {
        mIsMenuOpen = false;
        AnimUtils.doAnimateClose(textViewSetting, 0, 4, radias,400);
        AnimUtils.doAnimateClose(textViewWea, 1, 4, radias,400);
        AnimUtils.doAnimateClose(textViewMov, 2, 4, radias,400);
        AnimUtils.doAnimateClose(textViewMovRec, 3, 4, radias,400);
        AnimUtils.setSettingUp(this,imageViewBack);
    }

    private void getPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
//            //快捷图标
//            judgeDesk();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //6.0权限访问
                    //快捷图标
//                    judgeDesk();

                } else { //权限被拒绝
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！且可能被强制退出登录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag=false;
        EventBus.getDefault().unregister(this);

    }

    //两次退出
    private static Boolean isQuit = false;
    private Timer timer = new Timer();
    @Override
    public void onBackPressed() {
        if(mIsMenuOpen){
            mIsMenuOpen=false;
            closeMenu();
        }else {
            if (isQuit == false) {
                isQuit = true;
                MyApp.showToast("再按一次退出*_*");
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
//            System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }
}
