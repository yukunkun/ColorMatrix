package com.matrix.yukun.matrix.leshi_module.present;

import android.view.View;

import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;

import java.util.List;

/**
 * Created by yukun on 17-3-16.
 */
public interface LeShiListImple {
    void showMessage(String msg);
    void getInfo(List<ListBean> list);
    void dismissDialogs();
    void setListener();
    void getViews();
}
