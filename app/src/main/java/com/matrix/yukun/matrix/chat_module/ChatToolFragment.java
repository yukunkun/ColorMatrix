package com.matrix.yukun.matrix.chat_module;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.mvp.ChatBaseActivity;
import com.matrix.yukun.matrix.video_module.BaseFragment;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class ChatToolFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mLlPhoto;
    private LinearLayout mLlCamera;
    private static Context mContext;


    public static ChatToolFragment getInstance(Context context){
        mContext=context;
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
        mLlPhoto.setOnClickListener(this);
        mLlCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.ll_photo){
            ((ChatBaseActivity)mContext).openPhoto();
        }else if(id==R.id.ll_camera){
            ((ChatBaseActivity)mContext).openCamera();
        }
    }
}
