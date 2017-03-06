package com.matrix.yukun.matrix.movie_module.present;


import android.view.View;

import com.matrix.yukun.matrix.movie_module.bean.Subjects;

import java.util.List;

/**
 * Created by yukun on 17-2-20.
 */
public interface PresentImpl {
    void showMessage(String msg);
    void getInfo(List<Subjects> list);
    void dismissDialogs();
    void setListener();
    void getViews(View view);

}
