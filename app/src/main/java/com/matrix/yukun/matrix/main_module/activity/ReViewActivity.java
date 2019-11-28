package com.matrix.yukun.matrix.main_module.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.main_module.entity.TouTiaoBean;
import com.matrix.yukun.matrix.main_module.entity.UserInfo;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.qrcode.QRCodeActivity;
import com.matrix.yukun.matrix.tool_module.qrcode.QRImageCropActivity;
import com.matrix.yukun.matrix.util.BitmapUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class ReViewActivity extends BaseActivity implements View.OnClickListener {

//    https://uploadbeta.com/api/pictures/random/?key=推女郎
//    https://uploadbeta.com/api/pictures/random/?key=二次元
//    https://uploadbeta.com/api/pictures/random/?key=动漫

    private String mCount;
    private String mPassword;
    private TextView mTvCount;
    private RelativeLayout mRlPhoto;
    private EditText mEtSig;
    private EditText mEtName;
    private ToggleButton mASwitch;
    private TextView mTvGender;
    private TextView mTvCompl;
    private String headerPath;
    private String url="https://bing.ioliu.cn/v1/rand?type=json";
    private int REQUEST_CODE_FROM_GALLERY=1001;
    private CircleImageView mCircleImageView;
    private ImageView mMIvBack;

    @Override
    public int getLayout() {
        return R.layout.activity_re_view;
    }

    @Override
    public void initView() {
        mCount = getIntent().getStringExtra("name");
        mPassword = getIntent().getStringExtra("password");
        mTvCount = findViewById(R.id.tv_count);
        mMIvBack = findViewById(R.id.iv_back);
        mRlPhoto = findViewById(R.id.rl_header);
        mCircleImageView = findViewById(R.id.cv_header);
        mEtSig = findViewById(R.id.et_set_sig);
        mEtName = findViewById(R.id.et_set_name);
        mASwitch = findViewById(R.id.sw_gender);
        mTvGender = findViewById(R.id.tv_gender);
        mTvCompl = findViewById(R.id.tv_complete);
    }

    @Override
    public void initDate() {
        mTvCount.setText("账号："+mCount+"\n"+"密码："+mPassword);
    }

    @Override
    public void initListener() {
        mTvCompl.setOnClickListener(this);
        mRlPhoto.setOnClickListener(this);
        mMIvBack.setOnClickListener(this);
        mASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mTvGender.setText("性别：男");
                }else {
                    mTvGender.setText("性别：女");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_complete:
                if(!TextUtils.isEmpty(mEtName.getText().toString().trim())
                        &&!TextUtils.isEmpty(mEtSig.getText().toString().trim())
                        &&!TextUtils.isEmpty(headerPath)){
                    register((mEtName.getText().toString()).trim(),(mEtSig.getText().toString()).trim(),headerPath,(mTvGender.getText().toString()).substring(3,(mTvGender.getText().toString()).length()));
                }else {
                    ToastUtils.showToast("请完善信息");
                }
                break;
            case R.id.rl_header:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                this.startActivityForResult(intent, REQUEST_CODE_FROM_GALLERY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_CODE_FROM_GALLERY && data.getData()!=null) {
                Uri dataUri = data.getData();
                String dataPath = getDataColumn(this, dataUri, null, null);
                String path = BitmapUtil.compressImage(dataPath);
                headerPath=path;
                QRImageCropActivity.start(this,headerPath,false);
            }
        }
        if (resultCode == QRImageCropActivity.RESULT) {
            if (requestCode == QRImageCropActivity.CROPIMAGES){
                if (data != null) {
                    String crop_photo_path = data.getStringExtra("path");
                    headerPath=crop_photo_path;
                    Glide.with(this).load(crop_photo_path).into(mCircleImageView);
                }
            }
        }
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]); path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } return path;
    }

    private void register(String name, String sig, String headerPath, String gender) {

        BmobFile bmobFile = new BmobFile(new File(headerPath));

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                ToastUtils.showToast(bmobFile.getFileUrl());
                UserInfoBMob userInfoBMob=new UserInfoBMob();
                userInfoBMob.setName(name);
                userInfoBMob.setGender(gender);
                userInfoBMob.setSignature(sig);
                userInfoBMob.setCreateTime(String.valueOf(System.currentTimeMillis()));
                userInfoBMob.setAvator(bmobFile.getFileUrl());
                userInfoBMob.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        LogUtil.i("========",s+" "+e.toString());
                        ToastUtils.showToast("success");
                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }

            @Override
            public void onProgress(Integer value) {
                LogUtil.i("======","onProgress"+value);
            }

            @Override
            public void onStart() {
                LogUtil.i("======","onStart");
            }

            @Override
            public void doneError(int code, String msg) {
                LogUtil.i("======",msg);
            }

            @Override
            public void onFinish() {
                LogUtil.i("======","onFinish");
            }
        });

//        OkHttpUtils.post().url(url)
//                .addParams("key", AppConstant.Appkey)
//                .addParams("phone",mCount)
//                .addParams("name",name)
//                .addParams("passwd",mPassword)
//                .addParams("text",sig)
//                .addParams("other",other1)
//                .addFile("image",new File(headerPath).getName(),new File(headerPath))
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    String code = jsonObject.optString("code");
//                    if(code.equals("200")){
//                        JSONObject data = jsonObject.optJSONObject("data");
//                        Gson gson=new Gson();
//                        UserInfo userInfo = gson.fromJson(data.toString(), UserInfo.class);
//                        MyApp.setUserInfo(userInfo);
//                        //存数据库
//                        userInfo.save();
//                        EventBus.getDefault().post(new EventUpdateHeader());
//                        ToastUtils.showToast("注册成功");
//                        finish();
//                    }else {
//                        ToastUtils.showToast(jsonObject.optString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        Intent intent=new Intent(ReViewActivity.this,PlayMainActivity.class);
        startActivity(intent);
        super.onDestroy();

    }
}
