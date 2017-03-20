package com.matrix.yukun.matrix.movie_module.util;

/**
 * Created by yukun on 17-2-20.
 */
public class ApiException extends RuntimeException {

        public ApiException(int status) {
            super(getErrorDesc(status));
        }
        public static String getErrorDesc(int status){
            return "没有更多数据了 !_!";
        }
}
