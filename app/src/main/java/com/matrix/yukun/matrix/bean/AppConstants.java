package com.matrix.yukun.matrix.bean;

import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cretin on 15/12/30.
 */
public class AppConstants {
    /**
     * 指定在MyRelativeLayout中滑动的方向
     */
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;
    public static final String PATH = "yukun";
    public static final String YINGYONGBAOPATH = "http://app.qq.com/#id=detail&appid=1105962710\n";
    //和风天气 1163459047@qq.com
    //密码 :123456789ykk
    public static final String HEWEATHER_KEY = "449354b040d04805a3737fbcdb3c1339";

    public static final String APPID="1105962710";
    public static final String BANNER_ADID="3040126180953688";

    public static final String user_unique="cnsmvgmgt8";
    public static final String timestamp=getCurrentTime();
    public static final String format="json";
    public static final String ver="2.0";
    public static final String Secret_Key="ca1d537e27d5c7433741222b52133246";
    public static final String UserId="894559";
    public static final String ChatAppId="12b5b1b14c7e1d25f18902728b9655b6";
    public static ArrayList<Integer> integers=new ArrayList<>();
    public static List<Integer> giftFlows=new ArrayList<>();
    public static String HEWEANowURL="https://free-api.heweather.com/s6/weather/now";
    public static String getCurrentTime(){
        long l = System.currentTimeMillis();
        return l+"";
    }
    //礼物的集合
    public static ArrayList<Integer> getGift(){
        integers.add(R.mipmap.gift_1);
        integers.add(R.mipmap.gift_2);
        integers.add(R.mipmap.gift_3);
        integers.add(R.mipmap.gift_4);
        integers.add(R.mipmap.gift_5);
        integers.add(R.mipmap.gift_6);
        integers.add(R.mipmap.gift_7);
        integers.add(R.mipmap.gift_8);
        integers.add(R.mipmap.gift_9);
        integers.add(R.mipmap.gift_10);
        integers.add(R.mipmap.gift_11);
        integers.add(R.mipmap.chat_gif_666);
        integers.add(R.mipmap.chat_gif_2333);
        integers.add(R.mipmap.chat_gif_no);
        integers.add(R.mipmap.chat_gif_ruanmei);
        integers.add(R.mipmap.chat_gif_wow);
        integers.add(R.drawable.live_icon_christmas_hat2);
        integers.add(R.drawable.live_icon_christmas_ring1);
        integers.add(R.drawable.live_icon_christmas_socks4);
        integers.add(R.drawable.live_icon_spring_fish1);
        return integers;
    }
    //礼物的集合
    public static List<Integer> getFlowGiftMap(){
        giftFlows.clear();
        giftFlows.add(R.drawable.live_icon_christmas_hat1);
        giftFlows.add(R.drawable.live_icon_christmas_hat2);
        giftFlows.add(R.drawable.live_icon_christmas_hat3);
        return giftFlows;
    }
    //礼物的集合
    public static List<Integer> getFlowGiftRing(){
        giftFlows.clear();
        giftFlows.add(R.drawable.live_icon_christmas_ring1);
        giftFlows.add(R.drawable.live_icon_christmas_ring2);
        giftFlows.add(R.drawable.live_icon_christmas_ring3);
        return giftFlows;
    }
    //礼物的集合
    public static List<Integer> getFlowGiftSocks(){
        giftFlows.clear();
        giftFlows.add(R.drawable.live_icon_christmas_socks1);
        giftFlows.add(R.drawable.live_icon_christmas_socks2);
        giftFlows.add(R.drawable.live_icon_christmas_socks3);
        giftFlows.add(R.drawable.live_icon_christmas_socks4);
        return giftFlows;
    }
    //礼物的集合
    public static List<Integer> getFlowGiftFish(){
        giftFlows.clear();
        giftFlows.add(R.drawable.live_icon_spring_fish1);
        giftFlows.add(R.drawable.live_icon_spring_fish2);
        return giftFlows;
    }
}