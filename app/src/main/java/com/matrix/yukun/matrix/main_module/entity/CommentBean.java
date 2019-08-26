package com.matrix.yukun.matrix.main_module.entity;

import java.util.List;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class CommentBean {

    /**
     * children : []
     * content : 脚气闷到裆
     * ctime : 2018-04-17T22:30:02
     * data_id : 27610708
     * hate_count : 0
     * id : 479161
     * like_count : 168
     * more :
     * precmts : []
     * status : 0
     * type : text
     * user : {"id":21759605,"is_vip":false,"personal_page":"","profile_image":"http://wimg.spriteapp.cn/p/jie.jpg","qq_uid":"",
     * "qzone_uid":"","room_icon":"","room_name":"","room_role":"","room_url":"","sex":"m","total_cmt_like_count":"861",
     * "username":"风盖着吹","weibo_uid":""}
     * vote : []
     */

    private String content;
    private String ctime;
    private int data_id;
    private int hate_count;
    private int id;
    private int like_count;
    private String more;
    private int status;
    private String type;
    private UserBean user;
    private List<?> children;
    private List<?> precmts;
    private List<?> vote;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }

    public int getHate_count() {
        return hate_count;
    }

    public void setHate_count(int hate_count) {
        this.hate_count = hate_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    public List<?> getPrecmts() {
        return precmts;
    }

    public void setPrecmts(List<?> precmts) {
        this.precmts = precmts;
    }

    public List<?> getVote() {
        return vote;
    }

    public void setVote(List<?> vote) {
        this.vote = vote;
    }

    public static class UserBean {
        /**
         * id : 21759605
         * is_vip : false
         * personal_page :
         * profile_image : http://wimg.spriteapp.cn/p/jie.jpg
         * qq_uid :
         * qzone_uid :
         * room_icon :
         * room_name :
         * room_role :
         * room_url :
         * sex : m
         * total_cmt_like_count : 861
         * username : 风盖着吹
         * weibo_uid :
         */

        private int id;
        private boolean is_vip;
        private String personal_page;
        private String profile_image;
        private String qq_uid;
        private String qzone_uid;
        private String room_icon;
        private String room_name;
        private String room_role;
        private String room_url;
        private String sex;
        private String total_cmt_like_count;
        private String username;
        private String weibo_uid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIs_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }

        public String getPersonal_page() {
            return personal_page;
        }

        public void setPersonal_page(String personal_page) {
            this.personal_page = personal_page;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getQq_uid() {
            return qq_uid;
        }

        public void setQq_uid(String qq_uid) {
            this.qq_uid = qq_uid;
        }

        public String getQzone_uid() {
            return qzone_uid;
        }

        public void setQzone_uid(String qzone_uid) {
            this.qzone_uid = qzone_uid;
        }

        public String getRoom_icon() {
            return room_icon;
        }

        public void setRoom_icon(String room_icon) {
            this.room_icon = room_icon;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_role() {
            return room_role;
        }

        public void setRoom_role(String room_role) {
            this.room_role = room_role;
        }

        public String getRoom_url() {
            return room_url;
        }

        public void setRoom_url(String room_url) {
            this.room_url = room_url;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTotal_cmt_like_count() {
            return total_cmt_like_count;
        }

        public void setTotal_cmt_like_count(String total_cmt_like_count) {
            this.total_cmt_like_count = total_cmt_like_count;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getWeibo_uid() {
            return weibo_uid;
        }

        public void setWeibo_uid(String weibo_uid) {
            this.weibo_uid = weibo_uid;
        }
    }
}
