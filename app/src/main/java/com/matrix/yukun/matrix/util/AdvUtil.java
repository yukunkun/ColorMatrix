package com.matrix.yukun.matrix.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;

/**
 * author: kun .
 * date:   On 2019/7/22
 */
public class AdvUtil {
    static UnifiedBannerView bv;
    private static FrameLayout.LayoutParams getUnifiedBannerLayoutParams(Activity activity) {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x,  Math.round(screenSize.x / 6.4F));
    }


    public static UnifiedBannerView getBanner(Activity activity, ViewGroup viewGroup, String appId, String advId, UnifiedBannerADListener adListener) {
        if(bv != null){
            viewGroup.removeView(bv);
            bv.destroy();
        }
        bv = new UnifiedBannerView(activity,appId, advId, adListener);
        viewGroup.addView(bv, getUnifiedBannerLayoutParams(activity));
        return bv;
    }
}
