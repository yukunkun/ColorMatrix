package com.matrix.yukun.matrix.chat_module;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatInfo {


    /**
     * reason : 成功的返回
     * result : {"code":100000,"text":"你好啊，希望你今天过的快乐"}
     * error_code : 0
     */

    private String reason;
    /**
     * code : 100000
     * text : 你好啊，希望你今天过的快乐
     */

    private ResultBean result;
    private long error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public long getError_code() {
        return error_code;
    }

    public void setError_code(long error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        private int code;
        private String text;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "code=" + code +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                '}';
    }

}
