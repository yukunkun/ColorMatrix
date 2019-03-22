package com.matrix.yukun.matrix.video_module.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.barrage_module.dialog.BaseBottomDialog;

import java.io.File;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * author: kun .
 * date:   On 2018/11/16
 */
public class ShareDialog  extends BaseBottomDialog implements View.OnClickListener {

    private static ShareDialog shareDialog;
    private String mTitle;
    private String mShareUrl;
    private ImageView mIvQQ;
    private ImageView mIvZone;
    private ImageView mIvMore;
    private String mImageUrl;
    private String mImagePath;

    public static ShareDialog getInstance(String title, String shareUrl,String imageUrl){
        if(shareDialog==null){
            shareDialog = new ShareDialog();
        }
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        bundle.putString("imageUrl",imageUrl);
        bundle.putString("shareUrl",shareUrl);
        shareDialog.setArguments(bundle);
        return shareDialog;
    }

    public static ShareDialog getImageInstance(String imagepath,String shareUrl){

        if(shareDialog==null){
            shareDialog = new ShareDialog();
        }
        Bundle bundle=new Bundle();
        bundle.putString("imagepath",imagepath);
        bundle.putString("shareUrl",shareUrl);
        shareDialog.setArguments(bundle);
        return shareDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTitle = arguments.getString("title");
        mShareUrl = arguments.getString("shareUrl");
        mImageUrl = arguments.getString("imageUrl");
        mImagePath = arguments.getString("imagepath");
    }

    @Override
    public int setLayout() {
        return R.layout.share_dialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mIvQQ = inflate.findViewById(R.id.iv_share_qq);
        mIvZone = inflate.findViewById(R.id.iv_share_zone);
        mIvMore = inflate.findViewById(R.id.iv_share_more);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvQQ.setOnClickListener(this);
        mIvZone.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share_qq:
                if(!TextUtils.isEmpty(mImagePath)){
                    shareImageToQQ(QQ.NAME);
                }else {
                    shareToQQ(QQ.NAME);
                }
                break;
            case R.id.iv_share_zone:
                if(!TextUtils.isEmpty(mImagePath)){
                    shareImageToQQ(QZone.NAME);
                }else {
                    shareToQQ(QZone.NAME);
                }
                break;
            case R.id.iv_share_more:
                shareToMore();
                break;
        }
        getDialog().dismiss();
    }

    private void shareToMore() {
        if(!TextUtils.isEmpty(mImagePath)){
            File file = new File(mImagePath);//这里share.jpg是sd卡根目录下的一个图片文件
            Uri imageUri = Uri.fromFile(file);
            Intent shareIntent = new Intent();shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "分享图片"));
        }else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain"); // 纯文本
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享连接");
            intent.putExtra(Intent.EXTRA_TEXT, mShareUrl);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(Intent.createChooser(intent, ""));
        }
    }

    private void shareToQQ(String plat) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl(mImageUrl);
        oks.setUrl(mImageUrl);
        oks.setSiteUrl(mImageUrl);
        oks.setTitleUrl(mShareUrl);
        oks.setTitle("Matrix Photo");
        oks.setText(mTitle);
        oks.setPlatform(plat);
        oks.show(getContext());
    }

    private void shareImageToQQ(String plat) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);//设置分享为图片类型
        sp.setImagePath(mImagePath);
        Platform platform = ShareSDK.getPlatform(plat);// 要分享的平台//QQ.NAME
        platform.share(sp);
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount =0.6f;
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }
}
