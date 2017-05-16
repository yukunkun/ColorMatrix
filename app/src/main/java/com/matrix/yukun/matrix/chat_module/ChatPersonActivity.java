package com.matrix.yukun.matrix.chat_module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.image_module.activity.PhotoListActivity;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatPersonActivity extends AppCompatActivity {

    @BindView(R.id.tva_suren)
    TextView mTvaSuren;
    @BindView(R.id.ci_head)
    CircleImageView mCiHead;
    @BindView(R.id.et_name)
    EditText mEtName;
    private boolean isLoad=false;
    private String name;

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_person);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tva_suren, R.id.ci_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tva_suren:
                setHead();
                break;
            case R.id.ci_head:
                Intent intent=new Intent(this, PhotoListActivity.class);
                intent.putExtra("whitch",1);
                startActivity(intent);
                break;
        }
    }

    private void setHead() {
        if(isLoad==false){
            MyApp.showToast("上传中...");
            return;
        }
        name = mEtName.getText().toString();
        if(name !=null&&name.length()>0){
            setSharePrefress("name",name);
        }
        MyApp.showToast("设置成功");
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void getImagePath(OnEventImage eventImage){
        String headPath = eventImage.headPath;
        if(headPath==null){
            MyApp.showToast("设置失败");
            return;
        }
        String s="/storage/emulated/0/DirectSelling/pic/user_head.jpg";
        Glide.with(this).load(/*headPath.trim()*/s).placeholder(R.mipmap.icon_ai).into(mCiHead);
        Log.i("-----path",eventImage.headPath+"");
        setSharePrefress("head",headPath);
        isLoad=true;
    }
    public void ChatPersonBack(View view) {
        finish();
    }
    private void setSharePrefress(String tag, String str) {
        SharedPreferences sp = getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag, str);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
