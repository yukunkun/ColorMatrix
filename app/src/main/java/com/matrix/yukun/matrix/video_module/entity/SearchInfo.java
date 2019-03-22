package com.matrix.yukun.matrix.video_module.entity;

import java.util.List;

/**
 * Created by yukun on 18-1-5.
 */

public class SearchInfo {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : {"datas":[{"id":1839,"title":"<em class='highlight'>如何让你的ap<\/em>p在后台被干掉后优雅的重新启动","chapterId":228,"chapterName":"辅助 or 工具类","envelopePic":null,"link":"https://www.jianshu.com/p/1946bd4f3bb5","author":"Me豪","origin":null,"publishTime":1515119001000,"zan":null,"desc":null,"visible":0,"niceDate":"4小时前","courseId":13,"collect":false}],"offset":0,"size":20,"total":1,"pageCount":1,"curPage":1,"over":true}
     */

    private int errorCode;
    private Object errorMsg;
    /**
     * datas : [{"id":1839,"title":"<em class='highlight'>如何让你的ap<\/em>p在后台被干掉后优雅的重新启动","chapterId":228,"chapterName":"辅助 or 工具类","envelopePic":null,"link":"https://www.jianshu.com/p/1946bd4f3bb5","author":"Me豪","origin":null,"publishTime":1515119001000,"zan":null,"desc":null,"visible":0,"niceDate":"4小时前","courseId":13,"collect":false}]
     * offset : 0
     * size : 20
     * total : 1
     * pageCount : 1
     * curPage : 1
     * over : true
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int offset;
        private int size;
        private int total;
        private int pageCount;
        private int curPage;
        private boolean over;
        /**
         * id : 1839
         * title : <em class='highlight'>如何让你的ap</em>p在后台被干掉后优雅的重新启动
         * chapterId : 228
         * chapterName : 辅助 or 工具类
         * envelopePic : null
         * link : https://www.jianshu.com/p/1946bd4f3bb5
         * author : Me豪
         * origin : null
         * publishTime : 1515119001000
         * zan : null
         * desc : null
         * visible : 0
         * niceDate : 4小时前
         * courseId : 13
         * collect : false
         */

        private List<DatasBean> datas;

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean {
            private int id;
            private String title;
            private int chapterId;
            private String chapterName;
            private Object envelopePic;
            private String link;
            private String author;
            private Object origin;
            private long publishTime;
            private Object zan;
            private Object desc;
            private int visible;
            private String niceDate;
            private int courseId;
            private boolean collect;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public Object getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(Object envelopePic) {
                this.envelopePic = envelopePic;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public Object getOrigin() {
                return origin;
            }

            public void setOrigin(Object origin) {
                this.origin = origin;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public Object getZan() {
                return zan;
            }

            public void setZan(Object zan) {
                this.zan = zan;
            }

            public Object getDesc() {
                return desc;
            }

            public void setDesc(Object desc) {
                this.desc = desc;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public void setNiceDate(String niceDate) {
                this.niceDate = niceDate;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public boolean isCollect() {
                return collect;
            }

            public void setCollect(boolean collect) {
                this.collect = collect;
            }
        }
    }
}
