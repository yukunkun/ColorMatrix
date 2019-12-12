package com.matrix.yukun.matrix.main_module.dialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.download_module.service.DownLoadUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.barrage.dialog.BaseBottomDialog;
import com.matrix.yukun.matrix.tool_module.btmovie.Constant;
import com.matrix.yukun.matrix.util.AdvUtil;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

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
public class ShareDialog  extends BaseBottomDialog implements View.OnClickListener, UnifiedBannerADListener {

    private static ShareDialog shareDialog;
    private String mTitle;
    private String mShareUrl;
    private ImageView mIvQQ;
    private ImageView mIvZone;
    private ImageView mIvMore;
    private String mImageUrl;
    private String mImagePath;
    private ImageView mIvWeixin;
    private ImageView mIvFriend;
    private RelativeLayout mRlBanner;
    public static final String PACKAGE_WECHAT = "com.tencent.mm";//微信
    public static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";//qq

    public static ShareDialog getInstance(String title, String shareUrl, String imageUrl){
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

    public static ShareDialog getImageInstance(String imagepath, String shareUrl){

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
        mIvWeixin = inflate.findViewById(R.id.iv_share_weixin);
        mIvFriend = inflate.findViewById(R.id.iv_share_friend);
        mRlBanner=inflate.findViewById(R.id.rl);
    }

    @Override
    protected void initData() {
        UnifiedBannerView banner = AdvUtil.getBanner((Activity) getContext(), mRlBanner,  Constant.APPID, Constant.BANNER_ADID,this);
        banner.loadAD();
    }

    @Override
    protected void initListener() {
        mIvQQ.setOnClickListener(this);
        mIvZone.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
        mIvWeixin.setOnClickListener(this);
        mIvFriend.setOnClickListener(this);
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
            case R.id.iv_share_weixin:
                if(!TextUtils.isEmpty(mImagePath)){
                    shareLocalImageToWeiXin();
                }else {
                    shareToWeiXin();
                }
                break;
            case R.id.iv_share_friend:
                if(!TextUtils.isEmpty(mImagePath)){
                    shareLocalImageToWeiXinFriend();
                }else {
                    shareToWeiXinFriend();
                }
                break;
            case R.id.iv_share_more:
                shareToMore();
                break;
        }
        getDialog().dismiss();
    }

    private void shareToWeiXinFriend() {
        if (checkApkExist(getContext(), PACKAGE_WECHAT)) {
            if(mShareUrl.endsWith(".jpg")||mShareUrl.endsWith(".png")){
                shareImageToWeiXinFriend();
            }else {
                Intent intent = new Intent();
                ComponentName cop = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
                intent.setComponent(cop);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.app_name)+'\n'+mTitle+'\n'+mShareUrl);
                intent.putExtra(getContext().getString(R.string.app_name), "shareTextToWechatFriend");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        } else {
            ToastUtils.showToast("请先安装微信客户端");
        }
    }

    private void shareImageToWeiXinFriend() {
        if (checkApkExist(getContext(), PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            FileInfo fileInfo=new FileInfo();
            fileInfo.setUrl(mImageUrl);
            fileInfo.setFileName("yk_share_"+System.currentTimeMillis()+".png");
            fileInfo.setFilePath(AppConstant.IMAGEPATH);
            DownLoadUtils.getInstance().download(fileInfo, new DownLoadUtils.OnDownloadListener() {
                @Override
                public void onStartDownload() {

                }

                @Override
                public void onDownloading(long processed, long total) {

                }

                @Override
                public void onDownloadPause(long processed, long total) {

                }

                @Override
                public void onDownloadCancel(File file) {

                }

                @Override
                public void onDownloadSuccess(File file) {
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.putExtra("Kdescription", "sharePictureToTimeLine");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.myApp.startActivity(intent);
                    }else {
                        ToastUtils.showToast("分享失败");
                    }
                }
                @Override
                public void onDownloadFailed(Exception e) {
                    ToastUtils.showToast("分享失败");
                }
            });
        } else {
            ToastUtils.showToast("请先安装微信客户端");
        }

    }

    private void shareLocalImageToWeiXinFriend() {
        if (checkApkExist(getContext(), PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            File file=new File(mImagePath);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file.getAbsoluteFile());
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra("Kdescription", "sharePictureToTimeLine");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }else {
                ToastUtils.showToast("分享失败");
            }

        } else {
            ToastUtils.showToast("请先安装微信客户端");
        }

    }

    private void shareLocalImageToWeiXin() {
        if (checkApkExist(getContext(), PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            File file=new File(mImagePath);
            if (file.exists()) {
                if (file.isFile() && file.exists()) {
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(Intent.createChooser(intent, "sharePictureToWechatFriend"));
            }else {
                ToastUtils.showToast("分享失败");
            }
        } else {
            ToastUtils.showToast("请先安装微信客户端");
        }

    }

    private void shareImageToWeiXin() {
        if (checkApkExist(getContext(), PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            FileInfo fileInfo=new FileInfo();
            fileInfo.setUrl(mImageUrl);
            fileInfo.setFileName("yk_share_"+System.currentTimeMillis()+".png");
            fileInfo.setFilePath(AppConstant.IMAGEPATH);
            DownLoadUtils.getInstance().download(fileInfo, new DownLoadUtils.OnDownloadListener() {
                @Override
                public void onStartDownload() {

                }

                @Override
                public void onDownloading(long processed, long total) {

                }

                @Override
                public void onDownloadPause(long processed, long total) {

                }

                @Override
                public void onDownloadCancel(File file) {

                }

                @Override
                public void onDownloadSuccess(File file) {
                    if (file.exists()) {
                        if (file.isFile() && file.exists()) {
                            Uri uri = Uri.fromFile(file);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.myApp.startActivity(Intent.createChooser(intent, "sharePictureToWechatFriend"));
                    }else {
                        ToastUtils.showToast("分享失败");
                    }
                }

                @Override
                public void onDownloadFailed(Exception e) {
                    ToastUtils.showToast("分享失败");
                }
            });
        } else {
            ToastUtils.showToast("请先安装微信客户端");
        }

    }

    private void shareToWeiXin() {
        if (checkApkExist(getContext(), PACKAGE_WECHAT)) {
            if(mShareUrl.endsWith(".jpg")||mShareUrl.endsWith(".png")){
                shareImageToWeiXin();
            }else {
                Intent intent = new Intent();
                ComponentName cop = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
                intent.setComponent(cop);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.app_name)+'\n'+mTitle+'\n'+mShareUrl);
                intent.putExtra(getContext().getString(R.string.app_name), "shareTextToWechatFriend");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        } else {
            ToastUtils.showToast("请先安装微信客户端");
        }
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


    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onNoAD(AdError adError) {

    }

    @Override
    public void onADReceive() {

    }

    @Override
    public void onADExposure() {

    }

    @Override
    public void onADClosed() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADLeftApplication() {

    }

    @Override
    public void onADOpenOverlay() {

    }

    @Override
    public void onADCloseOverlay() {

    }
}
