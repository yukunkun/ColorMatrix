package com.matrix.yukun.matrix.main_module.entity;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/9/5
 */
public class TouBean {


    private List<ImageListBean> image_list;

    public List<ImageListBean> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<ImageListBean> image_list) {
        this.image_list = image_list;
    }

    public static class ImageListBean {
        /**
         * height : 564
         * uri : list/pgc-image/3e0e4ab490c54754a1c895c01e08df9b
         * url : http://p3-tt.byteimg.com/img/pgc-image/3e0e4ab490c54754a1c895c01e08df9b~tplv-tt-cs0:1003:677.jpg
         * url_list : [{"url":"http://p3-tt.byteimg.com/img/pgc-image/3e0e4ab490c54754a1c895c01e08df9b~tplv-tt-cs0:1003:677.jpg"},{"url":"http://p9-tt.byteimg.com/img/pgc-image/3e0e4ab490c54754a1c895c01e08df9b~tplv-tt-cs0:1003:677.jpg"},{"url":"http://p3-tt.byteimg.com/img/pgc-image/3e0e4ab490c54754a1c895c01e08df9b~tplv-tt-cs0:1003:677.jpg"}]
         * width : 1003
         */

        private int height;
        private String uri;
        private String url;
        private int width;
        private List<UrlListBean> url_list;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<UrlListBean> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlListBean> url_list) {
            this.url_list = url_list;
        }

        public static class UrlListBean {
            /**
             * url : http://p3-tt.byteimg.com/img/pgc-image/3e0e4ab490c54754a1c895c01e08df9b~tplv-tt-cs0:1003:677.jpg
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
