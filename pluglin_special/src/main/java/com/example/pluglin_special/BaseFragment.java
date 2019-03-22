package com.example.pluglin_special;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by yukun on 17-11-17.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(getLayout(),null);
//        ButterKnife.bind(this, inflate);
        initView(inflate, savedInstanceState);
        initListener();
        return inflate;
    }

    public abstract int getLayout() ;

    public abstract void initView(View inflate,Bundle savedInstanceState);
    public abstract void initListener() ;
}
