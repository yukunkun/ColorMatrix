package com.matrix.yukun.matrix;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
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

import com.matrix.yukun.matrix.adapter.RecAdapter;
import com.matrix.yukun.matrix.anims.MyEvaluator;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.bean.EventPos;
import com.matrix.yukun.matrix.camera_module.CameraActivity;
import com.matrix.yukun.matrix.image_module.activity.ListDetailActivity;
import com.matrix.yukun.matrix.image_module.activity.PhotoListActivity;
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.leshi_module.LeShiListActivity;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.selfview.squareprogressbar.SquareProgressBar;
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
    private ImageView imageShare;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecAdapter recAdapter;
    private boolean check=false;
    private LinearLayout layout;
    private ImageView imageViewMore;
    private ImageView imageViewPhoto;
    private String path;
    private String photoName;
    private SquareProgressBar imageViewTest;
    private PopupWindow mPopupWindow;
    private SeekBar seekBarBaoHe;
    private SeekBar seekBarLight;
    private Bitmap mTempBmp;
    private Bitmap mOriginBmp;
    private int pos;
    private Bitmap bitmapOri;
    private Bitmap seekBitmap;
    private RelativeLayout textViewTiShi;
    private int rotate=0;
    private ImageView textRotate;
    private Bitmap bitmapRoate;
    private ImageView imageViewRoate;
    private int roate=0;
    private LinearLayout layoutMore;
    private TextView textViewMatrix;
    private TextView textViewShare;
    private ValueAnimator animator;
    private ArrayList<String> lists=new ArrayList<>();
    private MyRelativeLayout layoutContain;
    private Random random = new Random();
    private boolean flag=true;
    private int count=0;
    private boolean isShow=false;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private Handler handler=new Handler();
    private int []ranColor ={Color.RED,Color.BLUE,Color.DKGRAY,Color.GREEN,Color.LTGRAY,
            R.color.color_44fc2c, R.color.color_44fc2c, R.color.color_b450fc, R.color.color_fc2c5d, R.color.color_fc2cd2,
            R.color.color_57f733, R.color.color_f733d6, R.color.colorPrimaryDark, R.color.color_3575ff};
    private TextView textViewRoate;
    private ImageView imageViewCamera;
    private ImageView imageViewCrop;
    private RelativeLayout reaContain;
    private Bitmap bitmap;
    private TextView textViewTag;
    private File destDirs;
    private long newName;
    private TextView textViewMov;
    private TextView textViewWea;
    private TextView textViewMovRec;
    private TextView textViewSetting;
    private ImageView imageViewBack;
    private boolean mIsMenuOpen=false;
    private int radias=220; //动画半径

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
        imageViewTest = (SquareProgressBar)findViewById(R.id.squareProgressBar);
        imageViewTest.setImage(R.mipmap.beijing_1);
//        imageViewTest.setImage(R.drawable.yuanjing);

        imageViewTest.setWidth(1);
        setColor();

        imageLoad = (ImageView) findViewById(R.id.loadimage);
        imageShare = (ImageView) findViewById(R.id.shareimage);
        layout = (LinearLayout) findViewById(R.id.linfuntion);
        imageViewMore = (ImageView) findViewById(R.id.imagemore);
        imageViewPhoto = (ImageView) findViewById(R.id.imagephoto);
        imageViewRoate = (ImageView) findViewById(R.id.imagerotate);
        imageViewCamera = (ImageView) findViewById(R.id.imagecameras);
        imageViewCrop = (ImageView) findViewById(R.id.imagecrop);
        layoutMore = (LinearLayout) findViewById(R.id.morefunction);
        textViewMatrix = (TextView) findViewById(R.id.loadmaterial);
        textViewShare = (TextView) findViewById(R.id.textviewshare);
        textViewRoate = (TextView) findViewById(R.id.tishi);
        textViewTag = (TextView) findViewById(R.id.tishitag);
        textViewMov = (TextView)findViewById(R.id.textmovie);
        textViewWea = (TextView)findViewById(R.id.textweather);
        imageViewBack = (ImageView) findViewById(R.id.back);
        textViewMovRec = (TextView)findViewById(R.id.textmovierec);
        textViewSetting = (TextView)findViewById(R.id.textseting);
        layoutContain = (MyRelativeLayout) findViewById(R.id.rea_contain);
        textViewTiShi = (RelativeLayout) findViewById(R.id.texttishi);
        reaContain = (RelativeLayout) findViewById(R.id.contain);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        setConLayout();
        startNoticefication();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView)findViewById(R.id.scrollview));
    }

    private void startNoticefication() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Noticefication.start(getApplicationContext());
//            }
//        }).start();
    }

    //计算高度
    private void setConLayout() {
        int height= ScreenUtils.instance().getHeight(this);
        int width= ScreenUtils.instance().getWidth(this);
        ViewGroup.LayoutParams layoutParams =reaContain.getLayoutParams();
        layoutParams.height= (int) (height*0.55);
        reaContain.setLayoutParams(layoutParams);
        radias=width/4;//菜单的半径为屏幕高度的1/4
    }

    private void setAdapter() {
        recAdapter = new RecAdapter(getApplicationContext(),check);
        recyclerView.setAdapter(recAdapter);
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getPos(EventPos event){
        pos = event.position;
        recAdapter.notifyDataSetChanged();
        setBitmapColor(pos);
    }

    private void setBitmapColor(int pos) {
        if(bitmapOri==null){
            Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        //得到不同的ColorMatrix,并且返回回来
        ColorMatrix colorMatrix = BitmapUtil.matrixTrans(pos);
        handleColorRotateBmp(colorMatrix, bitmapOri) ;
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getDetail(EventDetail event){
        path = event.path;
        photoName = event.photoName;
        if(path==null||path.length()==0){
            return;
        }

        imageViewCrop.setVisibility(View.VISIBLE);
        imageViewRoate.setVisibility(View.VISIBLE);
        textViewTiShi.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        imageViewMore.setVisibility(View.VISIBLE);
        detailImage();
    }

    private void detailImage() {

        if(mOriginBmp!=null){
            mOriginBmp.recycle();
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
//            mOriginBmp=scaled;
//        }else {
//            mOriginBmp=bitmapCopy;
//        }

        Bitmap bitmap= ImageUtils.getSmallBitmap(path);//图片处理,压缩大小
        mOriginBmp=bitmap;
        mTempBmp = Bitmap.createBitmap(mOriginBmp.getWidth(), mOriginBmp.getHeight(),
                Bitmap.Config.ARGB_4444);
        bitmapOri=Bitmap.createBitmap(mOriginBmp.getWidth(), mOriginBmp.getHeight(),
                Bitmap.Config.ARGB_4444);
        seekBitmap=mOriginBmp;
        bitmapRoate=mOriginBmp;
        //得到第一个原图
        ColorMatrix colorMatrix = BitmapUtil.matrixTrans(0);
        handleColorRotateBmp(colorMatrix, bitmapOri) ;
    }


    public void getCrop(Bitmap bitmap){
        if(mOriginBmp!=null){
            mOriginBmp.recycle();
        }
        mOriginBmp=bitmap;
        mTempBmp = Bitmap.createBitmap(mOriginBmp.getWidth(), mOriginBmp.getHeight(),
                Bitmap.Config.ARGB_4444);
        bitmapOri=Bitmap.createBitmap(mOriginBmp.getWidth(), mOriginBmp.getHeight(),
                Bitmap.Config.ARGB_4444);
        seekBitmap=mOriginBmp;
        bitmapRoate=mOriginBmp;
        //得到第一个原图
        ColorMatrix colorMatrix = BitmapUtil.matrixTrans(0);
        handleColorRotateBmp(colorMatrix, bitmapOri) ;
    }

    //从新绘制
    private void handleColorRotateBmp(ColorMatrix colorMatrix,Bitmap bitmaps){
        Bitmap bitmap = BitmapUtil.handleColorRotateBmp(colorMatrix, mOriginBmp, bitmaps);
        imageViewTest.setImageBitmap(bitmap);
        seekBitmap=bitmap;//进度的Bitmap
        bitmapRoate=bitmap;//旋转的Bitmap
        mTempBmp= BitmapUtil.mTempBit(bitmap);
    }
    //旋转
    private void handleColorRoate(int progress){
        Bitmap bitmap = BitmapUtil.rotateBitmap(bitmapRoate, progress);
        imageViewTest.setImageBitmap(null);
        imageViewTest.setImageBitmap(bitmap);
        mTempBmp= BitmapUtil.mTempBit(bitmap);
        seekBitmap=bitmap;//进度的Bitmap
    }

    private void setListener() {
        imageViewPhoto.setOnClickListener(this);
        imageViewMore.setOnClickListener(this);
        textViewTiShi.setOnClickListener(this);
        imageShare.setOnClickListener(this);
        imageLoad.setOnClickListener(this);
        imageViewRoate.setOnClickListener(this);
        textViewMatrix.setOnClickListener(this);
        textViewShare.setOnClickListener(this);
        textViewRoate.setOnClickListener(this);
        textViewTag.setOnClickListener(this);
        imageViewCamera.setOnClickListener(this);
        imageViewCrop.setOnClickListener(this);
        textViewMov.setOnClickListener(this);
        textViewWea.setOnClickListener(this);
        textViewMovRec.setOnClickListener(this);
        textViewSetting.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imagephoto:
                Intent intent=new Intent(MainActivity.this, PhotoListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            break;
            case R.id.imagemore:
                //popuwindow的展示
                if(!isShow){
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
            case R.id.imagecameras:
                startCamera();
                break;
            case R.id.textmovie:
                //视频推荐
                closeMenu();
                Intent intentMov=new Intent(MainActivity.this, LeShiListActivity.class);
                startActivity(intentMov);
                break;
            case R.id.textweather:
                //天气提醒
                closeMenu();
                Intent intentWea=new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intentWea);
                break;
            case R.id.textmovierec:
                //电影推荐
                closeMenu();
                Intent intentWeaRec =new Intent(MainActivity.this, MovieActivity.class);
                startActivity(intentWeaRec);
                break;
            case R.id.textseting:
                //设置
                closeMenu();
                Intent intentSet=new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSet);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.shareimage:
                //分享
                File destDir= FileUtil.createFile();
                if(photoName==null||photoName.length()==0){
                    Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                final File f = new File(destDir, photoName);
                Toast.makeText(MainActivity.this, "正在下载...", Toast.LENGTH_SHORT).show();
                flag=false;
                imageViewTest.setProgress(0);
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
                                setColor();
                            }
                        });
                    }
                }).start();
                share(f.getPath());
                break;
            case R.id.loadimage:
                layoutMore.setVisibility(View.GONE);
                flag=false;
                imageViewTest.setProgress(0);
                //load
                if(path==null){
                    if(photoName==null||photoName.length()==0){
                        Toast.makeText(MainActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

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
                                setColor();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.imagerotate:
                handleColorRoate(roate);
                roate++;
                break;
            case R.id.loadmaterial:
                File destDi = new File(Environment.getExternalStorageDirectory()+"/yukun");
                if (!destDi.exists()) {
                    destDi.mkdirs();
                }
                lists.clear();
                File[] files=destDi.listFiles();
                for (int i = 0; i < files.length; i++) {
                    lists.add(files[i]+"");
                }
                //打开Matria图库
                Intent intent_map=new Intent(MainActivity.this,ListDetailActivity.class);
                intent_map.putStringArrayListExtra("photo",lists);
                startActivity(intent_map);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.textviewshare:
                //下载分享的动画
                if(layoutMore.getVisibility()==View.GONE){
                    //下载分享的动画
                    layoutMore.setVisibility(View.VISIBLE);
                    addAnimation();
                }else {
                    layoutMore.setVisibility(View.GONE);
                }
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

                Intent intent1 = new Intent("com.android.camera.action.CROP");
                intent1.setDataAndType(Uri.fromFile(new File(path)), "image/*");
                intent1.putExtra("crop", "true");
                newName = System.currentTimeMillis();
                destDirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ AppConstants.PATH+"/"+newName);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destDirs));//
                intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
                intent1.putExtra("scale", true);
                intent1.putExtra("scaleUpIfNeeded", true);
                intent1.putExtra("return-data", false);
                startActivityForResult(intent1, 1);

                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&data!=null){
            Bundle extras = data.getExtras();
            Bitmap photo=null;
            photoName=System.currentTimeMillis() + ".jpg";
            if(extras!=null){
                photo = (Bitmap) extras.get("data");
            }

            if (photo!=null&&!photo.equals("null")) {
                getCrop(photo);
            }else if(data.getData()!=null){
                Uri uri = data.getData();
                if((data.getData()+"").startsWith("file")){
                    Bitmap bitmapCopy=BitmapFactory.decodeFile((data.getData()+"").substring(8,(data.getData()+"").length()),options).copy(Bitmap.Config.ARGB_4444,true);
                    getCrop(bitmapCopy);
                    FileUtil.deleteFile((data.getData()+"").substring(8,(data.getData()+"").length()));
                    return;
                }else {
                    if(data.getData()==null||getContentResolver().query(uri, null, null, null,null)==null){
                        imageViewCamera.setImageResource(R.mipmap.beijing_1);
                        Toast.makeText(MainActivity.this, "获取图片失败，请从相册选择！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String imagePath = null;
                    Cursor cursor = getContentResolver().query(uri, null, null, null,null);
                    if (cursor != null && cursor.moveToFirst()) {
                        imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }
                    Bitmap bitmapCopy=BitmapFactory.decodeFile(imagePath,options).copy(Bitmap.Config.ARGB_4444,true);
                    getCrop(bitmapCopy);
                    FileUtil.deleteFile(imagePath);
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
                layoutMore.layout(layoutMore.getLeft(),animatedValue,layoutMore.getRight(),layoutMore.getHeight()+animatedValue);
            }
        });

        animator.setDuration(1000);
        animator.setInterpolator(new BounceInterpolator());
        animator.setEvaluator(new MyEvaluator());
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(layoutMore.getVisibility()==View.VISIBLE){
            layoutMore.setVisibility(View.GONE);
        }
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
                    if(bitmapOri!=null){
                        Bitmap bitmap = BitmapUtil.handleColorBmp(mTempBmp, seekBitmap, progress, rotate);
                        imageViewTest.setImageBitmap(bitmap);
                        bitmapRoate=bitmap;
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
                    if(bitmapOri!=null){
                        Bitmap bitmap = BitmapUtil.handleColorMatrixBmp(mTempBmp, seekBitmap, progress);
                        imageViewTest.setImageBitmap(bitmap);
                        bitmapRoate=bitmap;
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

    private void setColor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    if(count==100){
                        final int randomcolor =random.nextInt(ranColor.length);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageViewTest.setColorRGB(ranColor[randomcolor]);
                            }
                        });
                        count=0;
                    }else {
                        count++;
                    }
                    try {
                        Thread.sleep(20);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageViewTest.setProgress(count);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
        AnimUtils.doAnimateOpen(textViewSetting, 0, 4, radias);
        AnimUtils.doAnimateOpen(textViewWea, 1, 4, radias);
        AnimUtils.doAnimateOpen(textViewMov, 2, 4, radias);
        AnimUtils.doAnimateOpen(textViewMovRec, 3, 4, radias);
        AnimUtils.setSettingDown(this,imageViewBack);
    }
    private void closeMenu() {
        mIsMenuOpen = false;
        AnimUtils.doAnimateClose(textViewSetting, 0, 4, radias);
        AnimUtils.doAnimateClose(textViewWea, 1, 4, radias);
        AnimUtils.doAnimateClose(textViewMov, 2, 4, radias);
        AnimUtils.doAnimateClose(textViewMovRec, 3, 4, radias);
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
