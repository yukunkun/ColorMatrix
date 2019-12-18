package com.matrix.yukun.matrix.chat_module.fragment.more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.BuildConfig;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.chat_module.mvp.InputPanel;

import java.io.File;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class ChatToolFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mLlPhoto;
    private LinearLayout mLlCamera;
    private LinearLayout mLlShake;
    private LinearLayout mLlFile;
    private static Context mContext;
    private Uri uri;
    public static boolean mIsLeanChat;
    public static File cameraSavePath;//拍照照片路径

    public static ChatToolFragment getInstance(Context context, String mCameraSavePath,boolean isLeanChat){
        mContext=context;
        mIsLeanChat=isLeanChat;
        cameraSavePath=new File(mCameraSavePath);
        ChatToolFragment chatToolFragment=new ChatToolFragment();
        return chatToolFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.tool_chat_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLlPhoto = inflate.findViewById(R.id.ll_photo);
        mLlCamera = inflate.findViewById(R.id.ll_camera);
        mLlShake = inflate.findViewById(R.id.ll_shake);
        mLlFile = inflate.findViewById(R.id.ll_file);
        mLlPhoto.setOnClickListener(this);
        mLlCamera.setOnClickListener(this);
        mLlShake.setOnClickListener(this);
        mLlFile.setOnClickListener(this);
        if(mIsLeanChat){
            mLlCamera.setVisibility(View.GONE);
            mLlFile.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.ll_photo){
           openPhoto();
        }else if(id== R.id.ll_camera){
           openCamera();
        }
        else if(id== R.id.ll_shake){
            //发送shake
            if(mShakeClickListener!=null){
                mShakeClickListener.shakeClickListener();
            }
        }
        else if(id== R.id.ll_file){
            openFile();
        }
    }

    private void openPhoto(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity)mContext).startActivityForResult(intent, InputPanel.ACTION_REQUEST_IMAGE);
    }

    private void openFile(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        ((Activity)mContext).startActivityForResult(intent, InputPanel.ACTION_REQUEST_FILE);
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity)mContext).startActivityForResult(intent, InputPanel.ACTION_REQUEST_CAMERA);
    }

    static ShakeClickListener mShakeClickListener;

    public static void setShakeClickListener(ShakeClickListener shakeClickListener) {
        mShakeClickListener = shakeClickListener;
    }

    public interface ShakeClickListener{
        void shakeClickListener();
    }
}
