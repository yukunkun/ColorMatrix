package com.ykk.pluglin_video.video;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ykk.pluglin_video.CenterDialog;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.ViewUtils;
import com.ykk.pluglin_video.utils.FileUtils;
import com.ykk.pluglin_video.utils.ScreenUtils;
import com.ykk.pluglin_video.utils.TagLayout;
import com.ykk.pluglin_video.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements CameraControler.view {
    @BindView(R2.id.surfaceview)
    SurfaceView mSurfaceview;
    @BindView(R2.id.text)
    TextView mText;
    @BindView(R2.id.camera)
    Button mCameraStar;
    @BindView(R2.id.btn_record_wav)
    Button mBtnRecordWav;
    @BindView(R2.id.btn_record_amr)
    Button mBtnRecordAmr;
    @BindView(R2.id.btn_stop)
    Button mBtnStop;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R2.id.ll_setting)
    LinearLayout mLlSetting;
    @BindView(R2.id.iv_play)
    ImageView mIvPlay;
    @BindView(R2.id.rl_line)
    RelativeLayout mRlLine;
    @BindView(R2.id.iv_back_setting)
    ImageView mIvBackSetting;
    @BindView(R2.id.switchBtn)
    Switch mSwitchBtn;
    @BindView(R2.id.iv_face)
    ImageView mIvFace;
    @BindView(R2.id.iv_bline)
    ImageView mIvBline;
    @BindView(R2.id.iv_takphoto)
    ImageView mIvTakphoto;
    @BindView(R2.id.switchScreen)
    Switch mSwitchScreen;
    @BindView(R2.id.switchColor)
    Switch mSwitchColor;
    @BindView(R2.id.iv_add_setting)
    ImageView mIvAddSetting;
    @BindView(R2.id.taglayout)
    TagLayout mTaglayout;
    @BindView(R2.id.switchWrite)
    Switch mSwitchWrite;
    @BindView(R2.id.taglayoutScreen)
    TagLayout mTaglayoutScreen;
    @BindView(R2.id.rl_camera_setting)
    RelativeLayout mRlCameraSetting;
    @BindView(R2.id.rl_bottom_setting)
    RelativeLayout mRlBottomSetting;
    @BindView(R2.id.tv_voice)
    TextView mTvVoice;
    @BindView(R2.id.tv_no_voice)
    RadioButton mTvNoVoice;
    @BindView(R2.id.tv_voice_1)
    RadioButton mTvVoice1;
    @BindView(R2.id.tv_voice_2)
    RadioButton mTvVoice2;
    @BindView(R2.id.rg_voice)
    RadioGroup mRgVoice;
    @BindView(R2.id.tv_sp)
    TextView mTvSp;
    @BindView(R2.id.tv_all)
    RadioButton mTvAll;
    @BindView(R2.id.tv_43)
    RadioButton mTv43;
    @BindView(R2.id.tv_169)
    RadioButton mTv169;
    @BindView(R2.id.rg_screen)
    RadioGroup mRgScreen;
    @BindView(R2.id.tv_light)
    TextView mTvLight;
    @BindView(R2.id.sb_light)
    SeekBar mSbLight;
    @BindView(R2.id.ll_screen_setting)
    LinearLayout mLlScreenSetting;
    @BindView(R2.id.tv_top)
    TextView mTvTop;
    @BindView(R2.id.tv_bottom)
    TextView mTvBottom;
    @BindView(R2.id.tv_left)
    TextView mTvLeft;
    @BindView(R2.id.tv_right)
    TextView mTvRight;
    @BindView(R2.id.sp_record)
    Spinner mSpRecord;
    @BindView(R2.id.iv_file)
    ImageView mIvFile;
    @BindView(R2.id.tv_video)
    TextView mTvVideo;
    private Timer timer = new Timer();
    int cnt = 0;
    private int recordQuality = 1;

    private boolean mState; //录制状态
    private CamerPresent mCamerPresent;
    private boolean isMedia;
    private boolean isShowSetting;
    private List<TextView> mTextViewListWrite = new ArrayList<>();
    private List<TextView> mTextViewListScreen = new ArrayList<>();
    private List<String> mListColor = new ArrayList<>();
    private WindowManager.LayoutParams mLp;

    @Override
    public int getLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mCamerPresent = new CamerPresent(this);
        mCamerPresent.init(mSurfaceview);
        setListener();
        mLp = this.getWindow().getAttributes();
        mSbLight.setMax(255);
        //屏幕亮度
        try {
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            mSbLight.setProgress(anInt);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        ((RadioButton) mRgScreen.getChildAt(0)).setChecked(true);
        ((RadioButton) mRgVoice.getChildAt(0)).setChecked(true);
        mSpRecord.setSelection(2);
    }

    public void initLayout(final List<String> mWidthList) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewListWrite.clear();
                mTaglayout.removeAllViews();
                for (int i = 0; i < mWidthList.size(); i++) {
                    final TextView tv = ViewUtils.getTextView(MainActivity.this);
                    tv.setText(mWidthList.get(i));
                    mTaglayout.addView(tv);
                    mTextViewListWrite.add(tv);
                    //监听
                    final int finalI = i;
                    if (i == 0) {
                        mTextViewListWrite.get(0).setTextColor(getResources().getColor(R.color.color_ff0008));
                        mTextViewListWrite.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg_check));
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < mTextViewListWrite.size(); j++) {
                                if (j == finalI) {
                                    mTextViewListWrite.get(j).setTextColor(getResources().getColor(R.color.color_ff0008));
                                    mTextViewListWrite.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg_check));
                                } else {
                                    mTextViewListWrite.get(j).setTextColor(getResources().getColor(R.color.color_dadada));
                                    mTextViewListWrite.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg));
                                }
                            }
                            mCamerPresent.setSupportedWhiteBalance(tv.getText().toString());
                        }
                    });
                }
            }
        });
    }

    public void initScreenLayout(final List<String> mScreenList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewListScreen.clear();
                mTaglayoutScreen.removeAllViews();
                for (int i = 0; i < mScreenList.size(); i++) {
                    final TextView tv = ViewUtils.getTextView(MainActivity.this);
                    tv.setText(mScreenList.get(i));
                    mTaglayoutScreen.addView(tv);
                    mTextViewListScreen.add(tv);
                    //监听
                    final int finalI = i;
                    if (i == 0) {
                        mTextViewListScreen.get(0).setTextColor(getResources().getColor(R.color.color_ffe100));
                        mTextViewListScreen.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_screen_bg_check));
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < mTextViewListScreen.size(); j++) {
                                if (j == finalI) {
                                    mTextViewListScreen.get(j).setTextColor(getResources().getColor(R.color.color_ffe100));
                                    mTextViewListScreen.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_screen_bg_check));
                                } else {
                                    mTextViewListScreen.get(j).setTextColor(getResources().getColor(R.color.color_dadada));
                                    mTextViewListScreen.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg));
                                }
                            }
                            mCamerPresent.setSupportedSceneModes(tv.getText().toString());
                        }
                    });
                }
            }
        });
    }

    //获取到数据
    public void initCameraList(List<String> listColor) {
        mListColor.clear();
        mListColor.addAll(listColor);
    }

    private void setListener() {
        mSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRlLine.setVisibility(View.VISIBLE);
                } else {
                    mRlLine.setVisibility(View.GONE);
                }
            }
        });
        mSwitchWrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTaglayout.setVisibility(View.VISIBLE);
                } else {
                    mTaglayout.setVisibility(View.GONE);
                }
            }
        });
        mSwitchScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTaglayoutScreen.setVisibility(View.VISIBLE);
                } else {
                    mTaglayoutScreen.setVisibility(View.GONE);
                }
            }
        });
        mSwitchColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CenterDialog instance = CenterDialog.getInstance(mListColor, MainActivity.this);
                    instance.show(getSupportFragmentManager(), "");
                    mLlSetting.setVisibility(View.GONE);
                    mSwitchColor.setChecked(false);
                }
            }
        });
        mSbLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mLp.screenBrightness = Float.valueOf(progress) * (1f / 255f);
                    getWindow().setAttributes(mLp);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //屏幕比例
        mRgScreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < mRgScreen.getChildCount(); i++) {
                    if (((RadioButton) mRgScreen.getChildAt(i)).isChecked()) {
                        ((RadioButton) mRgScreen.getChildAt(i)).setChecked(true);
                        setScreen(i);
                    }
                }
            }
        });
        //音效
        mRgVoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < mRgVoice.getChildCount(); i++) {
                    if (((RadioButton) mRgVoice.getChildAt(i)).isChecked()) {
                        ((RadioButton) mRgVoice.getChildAt(i)).setChecked(true);
                        mCamerPresent.setVoiceDev(i);
                    }
                }
            }
        });
        //spinner
        mSpRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView v1 = (TextView) view;
                v1.setTextColor(Color.WHITE); //可以随意设置自己要的颜色值
                v1.setTextSize(12);
                recordQuality = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setScreen(int pos) {
        int width = ScreenUtils.instance().getWidth(this);
        int height = ScreenUtils.instance().getHeight(this);
        switch (pos) {
            //默认
            case 0:
                mTvTop.setVisibility(View.GONE);
                mTvBottom.setVisibility(View.GONE);
                mTvLeft.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
                break;
            //4：3
            case 1:
                mTvTop.setVisibility(View.GONE);
                mTvBottom.setVisibility(View.GONE);
                mTvLeft.setVisibility(View.VISIBLE);
                mTvRight.setVisibility(View.VISIBLE);
                //设置值
                ViewGroup.LayoutParams params = mTvLeft.getLayoutParams();
                params.width = (width - height * 4 / 3) / 2;
                mTvLeft.setLayoutParams(params);
                ViewGroup.LayoutParams paramR = mTvRight.getLayoutParams();
                paramR.width = (width - height * 4 / 3) / 2;
                mTvRight.setLayoutParams(paramR);
                break;
            //16：9
            case 2:
                mTvLeft.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
                mTvTop.setVisibility(View.VISIBLE);
                mTvBottom.setVisibility(View.VISIBLE);
                //设置值
                ViewGroup.LayoutParams paramT = mTvTop.getLayoutParams();
                paramT.height = (height - width * 1 / 2) / 2;
                mTvTop.setLayoutParams(paramT);
                ViewGroup.LayoutParams paramB = mTvBottom.getLayoutParams();
                paramB.height = (height - width * 1 / 2) / 2;
                mTvBottom.setLayoutParams(paramB);
                break;

        }

    }

    /**
     * 滤镜
     *
     * @param value
     */
    public void setCameraColor(String value) {
        mCamerPresent.setSupportedColorEffects(value);
    }

    @OnClick({R2.id.iv_add_setting,R2.id.iv_file, R2.id.iv_takphoto, R2.id.iv_play, R2.id.iv_face, R2.id.iv_bline, R2.id.camera,
            R2.id.iv_back, R2.id.btn_record_wav, R2.id.iv_setting, R2.id.iv_back_setting, R2.id.btn_record_amr, R2.id.btn_stop})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.camera) {
        } else if (i == R.id.btn_record_wav) {
        } else if (i == R.id.btn_record_amr) {
        } else if (i == R.id.btn_stop) {
        } else if (i == R.id.iv_back) {
            finish();

        } else if (i == R.id.iv_setting) {
            mLlSetting.setVisibility(View.VISIBLE);
            rotateyAnimSet(mLlSetting);

        } else if (i == R.id.iv_back_setting) {
            mLlSetting.setVisibility(View.GONE);

        } else if (i == R.id.iv_face) {//摄像头在子线程切换
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mCamerPresent.changeCamera();
                }
            }).start();

        } else if (i == R.id.iv_bline) {
            mCamerPresent.openBline();

        } else if (i == R.id.iv_play) {
            if (!isMedia) {
                mIvPlay.setImageResource(R.mipmap.ic_pause);
                startTimer();
                cnt = 0;
                //录制开始
                mState = mCamerPresent.startRecording(recordQuality);
            } else {
                mIvPlay.setImageResource(R.mipmap.ic_play);
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                boolean isStop = mCamerPresent.stopRecording();
                if (isStop) {
                    Toast.makeText(this, "视频保存在" + FileUtils.PHOTO_PATH, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "视频录制失败", Toast.LENGTH_SHORT).show();
                }
                //录制完成
                mState = false;

            }
            isMedia = !isMedia;

        } else if (i == R.id.iv_takphoto) {//拍照
            mCamerPresent.getPhoto();

        } else if (i == R.id.iv_add_setting) {//setting
            if (isShowSetting) {
                mIvAddSetting.setImageResource(R.mipmap.ic_add_setting);
                mRlCameraSetting.setVisibility(View.GONE);
                mLlScreenSetting.setVisibility(View.GONE);
            } else {
                mIvAddSetting.setImageResource(R.mipmap.ic_remove_setting);
                mRlCameraSetting.setVisibility(View.VISIBLE);
                mLlScreenSetting.setVisibility(View.VISIBLE);
                rotateyAnimRun(mLlScreenSetting);
                rotateyAnimRun(mRlCameraSetting);
            }
            isShowSetting = !isShowSetting;

        } else if (i == R.id.iv_file) {
            Intent intent = new Intent(this, VideoFileActivity.class);
            startActivity(intent);

        }
    }


    public void startTimer() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mText.setText(FileUtils.getStringTime(cnt++));
                    }
                });
            }
        };

        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 位移动画
     *
     * @param view
     */
    public void rotateyAnimRun(View view) {
        ObjectAnimator//
                .ofFloat(view, "alpha", 0F, 1F)//
                .setDuration(400)//
                .start();
        ObjectAnimator//
                .ofFloat(view, "translationX", -360F, 0F)//
                .setDuration(500)//
                .start();
//        ObjectAnimator//
//                .ofFloat(view, "translationY", -360F, 0F)//
//                .setDuration(500)//
//                .start();
    }

    /**
     * 位移动画
     *
     * @param view
     */
    public void rotateyAnimSet(View view) {
        ObjectAnimator//
                .ofFloat(view, "alpha", 0F, 1F)//
                .setDuration(400)//
                .start();
        ObjectAnimator//
                .ofFloat(view, "translationX", 360F, 0F)//
                .setDuration(500)//
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamerPresent.closeCamer();
        mCamerPresent.stopRecording();
    }
}
