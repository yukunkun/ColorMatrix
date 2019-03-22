package com.matrix.yukun.matrix.phone_module;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.video_module.BaseCenterDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2018/11/28
 */
public class PhoneDialog extends BaseCenterDialog {

    private TextView mTvNumber;
    private TextView mTvBusiness;
    private TextView mTvPlace;
    private TextView mTvEmail;
    private String url="http://apis.juhe.cn/mobile/get";
    private static String phoneNumber;
    private static String phoneName;
    private TextView mTvName;

    public static PhoneDialog getInstance(String number,String name){
        PhoneDialog phoneDialog=new PhoneDialog();
        phoneNumber=number;
        phoneName=name;
        return phoneDialog;
    }
    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mTvNumber = inflate.findViewById(R.id.tv_phone_number);
        mTvBusiness = inflate.findViewById(R.id.tv_phone_business);
        mTvPlace = inflate.findViewById(R.id.tv_phone_place);
        mTvEmail = inflate.findViewById(R.id.tv_phone_email);
        mTvName = inflate.findViewById(R.id.tv_phone_name);
        mTvName.setText("姓名："+phoneName);
        if(phoneNumber.startsWith("+86")){
            phoneNumber=phoneNumber.substring(3,phoneNumber.length());
        }
        mTvNumber.setText("电话："+phoneNumber);
        iniData();
    }

    private void iniData() {
        OkHttpUtils.get().url(url)
                .addParams("phone",phoneNumber)
                .addParams("key", AppConstant.JUHE_PHONE_APPKEY)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String resultcode = jsonObject.optString("resultcode");
                    if(resultcode.equals("200")){
                        JSONObject result = jsonObject.optJSONObject("result");
                        String company = result.optString("company");
                        String province = result.optString("province");
                        String city = result.optString("city");
                        String zip = result.optString("zip");
                        mTvBusiness.setText("中国"+company);
                        mTvPlace.setText("地址："+province+"•"+city);
                        mTvEmail.setText("邮编："+zip);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.phone_dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            int width = ScreenUtils.instance().getWidth(getContext());
            int height = ScreenUtils.instance().getHeight(getContext());
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount =0.6f;
            //修改gravity
            params.gravity = Gravity.CENTER;
            params.width =width*4/5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }
}
