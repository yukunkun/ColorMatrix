package com.matrix.yukun.matrix.tool_module.cropmap;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imageeditor.ImageEditorActivity;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.main_module.views.PhotoChooseDialog;
import com.matrix.yukun.matrix.selfview.SelfImageView;
import com.matrix.yukun.matrix.selfview.view.MyRelativeLayout;
import com.matrix.yukun.matrix.tool_module.cropmap.adapter.RVPhotoAdapter;
import com.matrix.yukun.matrix.tool_module.garrary.activity.PhotoListActivity;
import com.matrix.yukun.matrix.tool_module.garrary.bean.EventDetail;
import com.matrix.yukun.matrix.tool_module.weather.WeatherActivity;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.matrix.yukun.matrix.util.BitmapUtil;
import com.matrix.yukun.matrix.util.DeskMapUtil;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.matrix.yukun.matrix.util.anims.MyEvaluator;
import com.miracle.view.imageeditor.bean.EditorResult;
import com.miracle.view.imageeditor.bean.EditorSetup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ImageCropActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvDownload;
    private RVPhotoAdapter mRVPhotoAdapter;
    private ImageView mIvEditImage;
    private String path;
    private String photoName;
    private SelfImageView mIvImage;
    private PopupWindow mPopupWindow;
    private SeekBar seekBarBaoHe;
    private SeekBar seekBarLight;
    private Bitmap mBitCompress;
    private Bitmap mBitOrigin;
    private Bitmap mBitPath;
    private Bitmap mBitSeek;
    private int rotate=0;
    private ImageView textRotate;
    private Bitmap mBitRotate;
    private int roate=0;
    private ValueAnimator animator;
    private MyRelativeLayout layoutContain;
    private boolean flag=true;
    private boolean isShow=false;
    private Handler handler=new Handler();
    private Bitmap bitmap;
    private TextView mTvMovie;
    private TextView mTvWeather;
    private TextView mTvMovieRec;
    private TextView mTvSetting;
    private ImageView mIvBack;
    private boolean mIsMenuOpen=false;
    private int radias=180; //动画半径
    private ImageView mIvRotate;
    private final int ACTION_REQUEST_EDITOR=1;
    private RecyclerView mRecyclerView;
    private ImageView mIvShare;
    private LinearLayout mLlToolChoose;
    private ImageView mIvCamera;
    private ImageView mIvPhoto;
    private TextView mTvTool;
    private LinearLayout mLlBottomTool;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mIvImage = findViewById(R.id.iv_image);
        mIvDownload =  findViewById(R.id.loadimage);
        mIvEditImage =  findViewById(R.id.imagemore);
        mTvMovie = findViewById(R.id.textmovie);
        mTvWeather = findViewById(R.id.textweather);
        mIvBack =  findViewById(R.id.back);
        mTvMovieRec = findViewById(R.id.textmovierec);
        mTvSetting = findViewById(R.id.textseting);
        layoutContain=findViewById(R.id.my_relat);
        mIvRotate = findViewById(R.id.iv_rotate);
        mRecyclerView = findViewById(R.id.recycler);
        mIvShare = findViewById(R.id.iv_share);
        mTvTool = findViewById(R.id.tv_tool_choose);
        mLlToolChoose = findViewById(R.id.ll_tool_choose);
        mLlBottomTool = findViewById(R.id.ll_tool_bottom);
        mIvCamera = findViewById(R.id.iv_camera);
        mIvPhoto = findViewById(R.id.iv_photo);
        PhotoChooseDialog photoChooseDialog=PhotoChooseDialog.getInstance();
        photoChooseDialog.show(getSupportFragmentManager(),"");
        setAdapter();
        getPermission();
    }

    private void setAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        String[] stringArray = getResources().getStringArray(R.array.photo);
        mRVPhotoAdapter = new RVPhotoAdapter(this, Arrays.asList(stringArray));
        mRecyclerView.setAdapter(mRVPhotoAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        mRVPhotoAdapter.setOnItemClickListener(new RVPhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                mRVPhotoAdapter.setSelectPosition(position);
                setBitmapColor(position);
            }
        });
    }
    /**
     * 打开相册
     */
    public void choosePhoto(){
        Intent intent=new Intent(ImageCropActivity.this, PhotoListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 打开相机
     */
    public void openCamera(){
        Intent intent=new Intent(this, CameraActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void setBitmapColor(int pos) {
        if(mBitPath==null){
            Toast.makeText(ImageCropActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
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
            ToastUtils.showToast("选择为空");
            return;
        }
        EditorSetup setup = new EditorSetup(path, path, AppConstant.IMAGEPATH+"/"+photoName, true);
        Intent intent = ImageEditorActivity.Companion.intent(ImageCropActivity.this, setup);
        ImageCropActivity.this.startActivityForResult(intent, ACTION_REQUEST_EDITOR);
    }

    private void detailImage(String path) {
        if(mBitOrigin!=null){
            mBitOrigin.recycle();
        }
        Bitmap bitmap=null;
        if(new File(path).length()/1024/1024>3){
            bitmap= ImageUtils.getSmallBitmap(path);//图片处理,压缩大小
        }else {
            bitmap=BitmapFactory.decodeFile(path);
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
        mIvImage.setImageBitmap(bitmap);
        mBitSeek=bitmap;//进度的Bitmap
        mBitRotate=bitmap;//旋转的Bitmap
        mBitCompress= BitmapUtil.mTempBit(bitmap);
    }
    //旋转
    private void handleColorRoate(int progress){
        if(mBitRotate!=null){
            Bitmap bitmap = BitmapUtil.rotateBitmap(mBitRotate, progress);
            mIvImage.setImageBitmap(null);
            mIvImage.setImageBitmap(bitmap);
            mBitCompress= BitmapUtil.mTempBit(bitmap);
            mBitSeek=bitmap;//进度的Bitmap
        }
    }

    @Override
    public void initListener() {
        mIvEditImage.setOnClickListener(this);
        mIvEditImage.setOnClickListener(this);
        mIvDownload.setOnClickListener(this);
        mIvRotate.setOnClickListener(this);
        mTvMovie.setOnClickListener(this);
        mTvWeather.setOnClickListener(this);
        mTvMovieRec.setOnClickListener(this);
        mTvSetting.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvPhoto.setOnClickListener(this);
        mIvCamera.setOnClickListener(this);
        mTvTool.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imagemore:
                if(!isShow){
                    showMore();
                }else {
                    mPopupWindow.dismiss();
                }
                isShow=!isShow;
                break;
            case R.id.textmovie:
                //电影推荐
//                closeMenu();
//                Intent intentWeaRec =new Intent(ImageCropActivity.this, MovieActivity.class);
//                startActivity(intentWeaRec);
                break;
            case R.id.textweather:
                //天气提醒
                closeMenu();
                Intent intentWea=new Intent(ImageCropActivity.this, WeatherActivity.class);
                startActivity(intentWea);
                break;
            case R.id.textmovierec:
               //空
                break;
            case R.id.textseting:
                //设置
                closeMenu();
//                Intent intentSet=new Intent(ImageCropActivity.this, SettingActivity.class);
//                startActivity(intentSet);
//                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.loadimage:
                flag=false;
                //load
                if(path==null){
                    if(photoName==null||photoName.length()==0){
                        Toast.makeText(ImageCropActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                bitmap = ImageUtils.createViewBitmap(layoutContain, layoutContain.getWidth(), layoutContain.getHeight());
                Toast.makeText(ImageCropActivity.this, "正在下载...", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.loadImage(bitmap, photoName);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ImageCropActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                                flag=true;
                            }
                        });
                    }
                }).start();
                break;
            case R.id.iv_rotate:
                handleColorRoate(roate);
                roate++;
                break;
            case R.id.iv_share:
                if(photoName!=null){
                    bitmap = ImageUtils.createViewBitmap(layoutContain, layoutContain.getWidth(), layoutContain.getHeight());
                    FileUtil.loadImage(bitmap, photoName);
                    File file = new File(AppConstant.IMAGEPATH+"/"+photoName);//这里share.jpg是sd卡根目录下的一个图片文件
                    Uri imageUri = Uri.fromFile(file);
                    Intent shareIntent = new Intent();shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "分享图片"));
                }
                break;
//            case R.id.back:
//                Intent iVideo=new Intent(this, SettingActivity.class);
//                startActivity(iVideo);
//                overridePendingTransition(R.anim.right_in,R.anim.left_out);
//                break;
            case R.id.iv_camera:
                openCamera();
                break;
            case R.id.iv_photo:
                choosePhoto();
                break;
            case R.id.tv_tool_choose:
                if(mTvTool.getText().toString().equals("重选")){
                    mRecyclerView.setVisibility(View.GONE);
                    mLlToolChoose.setVisibility(View.VISIBLE);
                    mTvTool.setText("预览");
                }else if(mTvTool.getText().toString().equals("预览")){
                    mLlBottomTool.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mLlToolChoose.setVisibility(View.GONE);
                    mTvTool.setText("滤镜");
                }else if(mTvTool.getText().toString().equals("滤镜")){
                    mLlBottomTool.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLlToolChoose.setVisibility(View.GONE);
                    mTvTool.setText("重选");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_CANCELED){
           if(requestCode== ACTION_REQUEST_EDITOR){
               if(!TextUtils.isEmpty(path)){
                   detailImage(path);
               }
            }
        }
        if(resultCode==Activity.RESULT_OK){
            if(requestCode== ACTION_REQUEST_EDITOR){
                if(data!=null){
                    EditorResult editorResult = (EditorResult) data.getSerializableExtra(Activity.RESULT_OK + "");
                    detailImage(editorResult.getEditor2SavedPath());
                }
            }
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
        View view = View.inflate(ImageCropActivity.this, R.layout.popuwindow,null);
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
            mPopupWindow.showAsDropDown(mIvEditImage,-width,0);
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
                        mIvImage.setImageBitmap(bitmap);
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
                        mIvImage.setImageBitmap(bitmap);
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
                FileUtil.showToast(ImageCropActivity.this,rotate);
            }
        });
    }
    
    public void Back(View view) {
        if (!mIsMenuOpen) {
            mIsMenuOpen = true;
            if(mTvSetting.getVisibility()==View.GONE){
                mTvSetting.setVisibility(View.VISIBLE);
//                mTvWeather.setVisibility(View.VISIBLE);
                mTvMovie.setVisibility(View.VISIBLE);
//                mTvMovieRec.setVisibility(View.VISIBLE);
            }
            openMenu();
        } else {
            closeMenu();
        }
    }

    private void openMenu() {
        AnimUtils.doAnimateOpen(mTvSetting, 0, 4, radias,500);
//        AnimUtils.doAnimateOpen(mTvWeather, 1, 4, radias,420);
        AnimUtils.doAnimateOpen(mTvMovie, 1, 4, radias,340);
//        AnimUtils.doAnimateOpen(mTvMovieRec, 3, 4, radias,260);
        AnimUtils.setSettingDown(this,mIvBack);
    }
    private void closeMenu() {
        mIsMenuOpen = false;
        AnimUtils.doAnimateClose(mTvSetting, 0, 4, radias,400);
//        AnimUtils.doAnimateClose(mTvWeather, 1, 4, radias,400);
        AnimUtils.doAnimateClose(mTvMovie, 1, 4, radias,400);
//        AnimUtils.doAnimateClose(mTvMovieRec, 3, 4, radias,400);
        AnimUtils.setSettingUp(this,mIvBack);
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(ImageCropActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImageCropActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
}
