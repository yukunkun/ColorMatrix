package com.matrix.yukun.matrix.leshilive_module.bean;

import java.util.List;

/**
 * Created by yukun on 17-3-29.
 */
public class ResponseBean<T> {
    private String total;
    private List<T> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
