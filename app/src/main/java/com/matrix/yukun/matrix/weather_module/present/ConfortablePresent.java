package com.matrix.yukun.matrix.weather_module.present;

import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.fragment.ConfortableFragment;

/**
 * Created by yukun on 17-3-7.
 */
public class ConfortablePresent  implements ConfortablePresentImpl {
    ConfortableFragment mView;
    String city;

    public ConfortablePresent(ConfortableFragment mView, String city) {
        this.mView = mView;
        this.city = city;
    }

    @Override
    public void onsubscriber() {

    }

    @Override
    public void unsubscriber() {

    }

    @Override
    public void getInfo() {

    }

    /**
     * Created by yukun on 17-3-7.
     */
    public static interface ConforFragmentImpl {
       void  getInfo(WeaLifePoint weaLifePoint);
    }
}
