package com.matrix.yukun.matrix.movie_module.bean;

import java.util.List;

/**
 * Created by yukun on 17-2-15.
 */
public class HttpResult<T> {
    private int count;
    private int start;
    private int total;
    private String title;

    //用来模仿Data
    private List<T> subjects;  //这里的实体类必须和返回值一样的名字,否则报错

    public List<T> getSubjects() {
        return subjects;
    }

    public int getCount() {
        return count;
    }

}
