package com.ykk.pluglin_video.entity;

import java.util.List;

/**
 * Created by yukun on 18-1-5.
 */

public class CommonUrl {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : [{"id":17,"name":"国内大牛博客集合","link":"http://www.wanandroid.com/article/list/0?cid=176","visible":1,"order":1,"icon":null},{"id":16,"name":"国外大牛博客集合","link":"https://github.com/android-cn/android-dev-com","visible":1,"order":2,"icon":null},{"id":1,"name":"鸿洋的博客","link":"http://blog.csdn.net/lmj623565791","visible":1,"order":3,"icon":null},{"id":2,"name":"郭霖的博客","link":"http://blog.csdn.net/guolin_blog","visible":1,"order":3,"icon":null},{"id":4,"name":"今天最火的开源项目","link":"https://github.com/trending/java","visible":1,"order":3,"icon":null},{"id":15,"name":"stackoverflow","link":"https://stackoverflow.com/","visible":1,"order":4,"icon":null},{"id":3,"name":"鸿洋公众号文章聚合","link":"http://www.wanandroid.com/blog/show/6","visible":1,"order":5,"icon":null},{"id":5,"name":"干货集中营","link":"http://gank.io/","visible":1,"order":5,"icon":null},{"id":6,"name":"掘金","link":"https://juejin.im/timeline/android","visible":1,"order":6,"icon":null},{"id":7,"name":"开发者头条","link":"https://toutiao.io/","visible":1,"order":7,"icon":null},{"id":8,"name":"segmentfault","link":"https://segmentfault.com/t/android","visible":1,"order":8,"icon":null},{"id":9,"name":"Android开源项目解析","link":"http://p.codekk.com/","visible":1,"order":9,"icon":null},{"id":10,"name":"androiddevtools","link":"http://www.androiddevtools.cn/","visible":1,"order":10,"icon":null},{"id":11,"name":"Android开发热门专题","link":"http://www.wanandroid.com/article/list/0?cid=185","visible":1,"order":11,"icon":null},{"id":12,"name":"Android面试相关","link":"http://www.wanandroid.com/article/list/0?cid=73","visible":1,"order":12,"icon":null},{"id":14,"name":"极客导航","link":"http://jikedaohang.com/","visible":1,"order":13,"icon":null},{"id":21,"name":"谷歌开发者中文博客","link":"https://developers.googleblog.cn/","visible":1,"order":13,"icon":null},{"id":13,"name":"Git在线可视化学习","link":"https://learngitbranching.js.org/","visible":1,"order":14,"icon":null},{"id":22,"name":"在线查看Android源码","link":"https://www.androidos.net.cn/sourcecode","visible":1,"order":14,"icon":null},{"id":19,"name":"md在线编辑器","link":"http://md.aclickall.com/","visible":1,"order":15,"icon":null},{"id":20,"name":"无版权素材网站","link":"https://unsplash.com/","visible":1,"order":16,"icon":null},{"id":18,"name":"互联网相关统计","link":"http://tongji.baidu.com/data/browser","visible":1,"order":999,"icon":null}]
     */

    private int errorCode;
    private Object errorMsg;
    /**
     * id : 17
     * name : 国内大牛博客集合
     * link : http://www.wanandroid.com/article/list/0?cid=176
     * visible : 1
     * order : 1
     * icon : null
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
        private String link;
        private int visible;
        private int order;
        private Object icon;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
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

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }
    }
}
