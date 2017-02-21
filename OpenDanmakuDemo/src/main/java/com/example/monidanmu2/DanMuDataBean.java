package com.example.monidanmu2;

import java.util.List;

/**
 * Created by BGFVG on 2017/2/16.
 */

public class DanMuDataBean {

    private List<ConBean> con;

    public List<ConBean> getCon() {
        return con;
    }

    public void setCon(List<ConBean> con) {
        this.con = con;
    }

    public static class ConBean {
        private String content;
        private String avatar;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
