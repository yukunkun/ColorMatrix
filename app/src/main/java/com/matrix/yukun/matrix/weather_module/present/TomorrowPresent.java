package com.matrix.yukun.matrix.weather_module.present;

import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;
import com.matrix.yukun.matrix.weather_module.fragment.TomorrowWeathFrag;

/**
 * Created by yukun on 17-3-6.
 */
public class TomorrowPresent implements BasePresentImpl {
    TomorrowWeathFrag tomorrowWeathFrag;

    public TomorrowPresent(TomorrowWeathFrag tomorrowWeathFrag) {
        this.tomorrowWeathFrag = tomorrowWeathFrag;
    }

    @Override
    public void onsubscriber() {

    }

    @Override
    public void unsubscriber() {

    }
}
