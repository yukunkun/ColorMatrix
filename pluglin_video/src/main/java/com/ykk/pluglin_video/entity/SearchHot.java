package com.ykk.pluglin_video.entity;

import java.util.List;

/**
 * Created by yukun on 18-1-5.
 */

public class SearchHot {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : [{"id":6,"name":"面试","link":null,"visible":1,"order":1},{"id":9,"name":"Studio3","link":null,"visible":1,"order":1},{"id":5,"name":"动画","link":null,"visible":1,"order":2},{"id":1,"name":"自定义View","link":null,"visible":1,"order":3},{"id":2,"name":"性能优化 速度","link":null,"visible":1,"order":4},{"id":3,"name":"gradle","link":null,"visible":1,"order":5},{"id":4,"name":"Camera 相机","link":null,"visible":1,"order":6},{"id":7,"name":"代码混淆 安全","link":null,"visible":1,"order":7},{"id":8,"name":"逆向 加固","link":null,"visible":1,"order":8}]
     */

    private int errorCode;
    private Object errorMsg;
    /**
     * id : 6
     * name : 面试
     * link : null
     * visible : 1
     * order : 1
     */

    private List<DataBean> data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String name;
        private Object link;
        private int visible;
        private int order;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getLink() {
            return link;
        }

        public void setLink(Object link) {
            this.link = link;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }
}
