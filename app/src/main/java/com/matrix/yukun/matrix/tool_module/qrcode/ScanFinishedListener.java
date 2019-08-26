package com.matrix.yukun.matrix.tool_module.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.matrix.yukun.matrix.main_module.utils.ToastUtils;

/**
 * 扫描二维码结束后监听的实现类
 *
 * @autor wwl
 * @date 2017-04-25 14:07
 */

public class ScanFinishedListener implements ScanFinishListener {
    private Context mContext;

    public ScanFinishedListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onScanFinish(final String resultStr, final Activity activity) {
//        LogUtil.i("扫到的字符串-->" + resultStr);
        if (resultStr != null) {
//            if (resultStr.matches(AppConstants.Validator.REGEX_SCAN_LOGIN)) {
//                try {
//                    JSONObject json = new JSONObject(resultStr);
//                    String qrKey = json.getString("qrKey");
//                    String plat = json.getString("plat");
//                    OtherPlatLoginActivity.start(mContext, plat, qrKey);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    activity.finish();
//                }
//                activity.finish();
//            }
//            else {
//                if (resultStr.contains("getcube.cn") || resultStr.contains("workinggo.com") || resultStr.contains("125.208.1.67") || resultStr.contains("59.110.45.40") || resultStr.contains("114.112.101.")) {//坐标的链接
//                    if (resultStr.contains("qr/user")) { // 用户
//                        //ScanFriendDetailActivity.startByUrl(activity, resultStr);
//                        ContactRegisterDetailActivity.start(activity, resultStr, QR_Contact);
//                        activity.finish();
//                    }
//                    else if (resultStr.contains("qr/group")) { // 群组
//                        Subscription subscription = GroupRepository.getInstance().getGroupByUrl(resultStr).compose(RxSchedulers.<Group>io_main()).subscribe(new ApiSubscriber<Group>(activity) {
//                            @Override
//                            protected void _onNext(Group group) {
//                                if (activity != null && !activity.isFinishing()) {
//                                    if (group.delGroup == 1) {
//                                        ToastUtil.showToast(activity, "该群已被解散");
//                                    }
//                                    else if (Group.isContainsById(group, SpUtil.getUser().userId)) {
//                                        CubeUI.getInstance().startGroupChat(activity, group.cube, group.groupName, CubeConstant.EXTRA_CHAT_MANAGER_SN);
//                                        activity.finish();
//                                    }
//                                    else {
//                                        ScannedGroupActivity.start(activity, resultStr);
//                                        activity.finish();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            protected void _onError(String message) {
//                                ToastUtil.showToast(activity, "查询群组信息失败，识别的二维码内容为:" + resultStr);
//                            }
//
//                            @Override
//                            protected void _onCompleted() {
//                            }
//                        });
//                    }
//                    // 坐标的抽奖系统
//                    else if (resultStr.contains("https://lucky.workinggo.com")) {
//                        String uriString = "https://lucky.workinggo.com?" + "token=" + SpUtil.getToken() + "&key=" + EncryptUtil.encrypt(String.valueOf(SpUtil.getUserId()), AppConstants.EncryptKey.LUCKY_DRAW_ENCODE_KEY) + "&from=" + "android";
//                        WebViewActivity.start(mContext, "幸运大奖", uriString);
//                        activity.finish();
//                    }
//                    else {
//                        //ToastUtil.showToast(mContext, 0, "扫到的内容为:" + resultStr);
//                        WebViewActivity.start(mContext, "", resultStr);
//                        activity.finish();
//                    }
//                }
//                else {//非坐标的链接
                    if (resultStr.startsWith("http://") || resultStr.startsWith("https://")) {
                        Uri uri = Uri.parse(resultStr);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(intent);
                        activity.finish(); }
                    else {
                        ToastUtils.showToast("扫到的内容为:" + resultStr);
                    }
                }
            }
        }
//        else {
//            ToastUtil.showToast(mContext, 0, "扫到的内容为null");
//        }
//    }
//}
