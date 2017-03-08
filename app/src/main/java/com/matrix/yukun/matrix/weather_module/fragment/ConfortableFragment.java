package com.matrix.yukun.matrix.weather_module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.present.ConforableFragImpl;
import com.matrix.yukun.matrix.weather_module.present.ConfortablePresent;

import butterknife.ButterKnife;

/**
 * Created by yukun on 17-3-7.
 */
public class ConfortableFragment extends BaseFrag implements ConforableFragImpl {

    private ConfortablePresent mPresent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String city = getArguments().getString("city");
        mPresent = new ConfortablePresent(this,city);
        this.basePresent= mPresent;
        super.onCreate(savedInstanceState);
    }
    public static ConfortableFragment newInstance(String arg){
        ConfortableFragment fragment = new ConfortableFragment();
        Bundle bundle = new Bundle();
        bundle.putString( "city", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wea_conforable, null);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void dismissDialogs() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getLifeInfo(WeaLifePoint weaLifePoint) {

    }
}
