package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matrix.yukun.matrix.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by yukun on 17-11-17.
 */

public abstract class BaseCardFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(getLayout(),null);
        ButterKnife.bind(this, inflate);
        initView(inflate, savedInstanceState);
        return inflate;
    }

    public abstract int getLayout() ;

    public abstract void initView(View inflate,Bundle savedInstanceState);

    public  abstract void saveCardView();

    public  abstract String getImagePath();
}
