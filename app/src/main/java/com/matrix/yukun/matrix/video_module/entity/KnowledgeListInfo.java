package com.matrix.yukun.matrix.video_module.entity;

import java.util.List;

/**
 * Created by yukun on 18-1-8.
 */

public class KnowledgeListInfo {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : {"datas":[{"id":987,"title":"Android Vector曲折的兼容之路","chapterId":151,"chapterName":"Vector","envelopePic":"","link":"http://blog.csdn.net/eclipsexys/article/details/51838119","author":"x359981514","origin":"CSDN","publishTime":1472975105000,"zan":0,"desc":"两年前写书的时候，就在研究Android L提出的Vector，可研究下来发现，完全不具备兼容性，相信这也是它没有被广泛使用的一个原因，经过Google的不懈努力，现在Vector终于迎来了它的春天。","visible":1,"niceDate":"2016-09-04","courseId":13,"collect":false}],"offset":0,"size":20,"total":1,"pageCount":1,"curPage":1,"over":true}
     */

    private int errorCode;
    private Object errorMsg;
    /**
     * datas : [{"id":987,"title":"Android Vector曲折的兼容之路","chapterId":151,"chapterName":"Vector","envelopePic":"","link":"http://blog.csdn.net/eclipsexys/article/details/51838119","author":"x359981514","origin":"CSDN","publishTime":1472975105000,"zan":0,"desc":"两年前写书的时候，就在研究Android L提出的Vector，可研究下来发现，完全不具备兼容性，相信这也是它没有被广泛使用的一个原因，经过Google的不懈努力，现在Vector终于迎来了它的春天。","visible":1,"niceDate":"2016-09-04","courseId":13,"collect":false}]
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
         * id : 987
         * title : Android Vector曲折的兼容之路
         * chapterId : 151
         * chapterName : Vector
         * envelopePic :
         * link : http://blog.csdn.net/eclipsexys/article/details/51838119
         * author : x359981514
         * origin : CSDN
         * publishTime : 1472975105000
         * zan : 0
         * desc : 两年前写书的时候，就在研究Android L提出的Vector，可研究下来发现，完全不具备兼容性，相信这也是它没有被广泛使用的一个原因，经过Google的不懈努力，现在Vector终于迎来了它的春天。
         * visible : 1
         * niceDate : 2016-09-04
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
            private String envelopePic;
            private String link;
            private String author;
            private String origin;
            private long publishTime;
            private int zan;
            private String desc;
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

            public String getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(String envelopePic) {
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

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public int getZan() {
                return zan;
            }

            public void setZan(int zan) {
                this.zan = zan;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
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
