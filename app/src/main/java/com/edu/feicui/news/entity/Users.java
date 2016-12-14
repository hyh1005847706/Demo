package com.edu.feicui.news.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class Users {
    private String uid;
    private String portrait;
    private int integration;
    private int comnum;
    private List<LoginLog> loginlog;

    public Users() {
    }

    public Users(String uid, String portrait, int integration, int comnum, List<LoginLog> loginlog) {

        this.uid = uid;
        this.portrait = portrait;
        this.integration = integration;
        this.comnum = comnum;
        this.loginlog = loginlog;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public int getComnum() {
        return comnum;
    }

    public void setComnum(int comnum) {
        this.comnum = comnum;
    }

    public List<LoginLog> getLoginlog() {
        return loginlog;
    }

    public void setLoginlog(List<LoginLog> loginlog) {
        this.loginlog = loginlog;
    }
}
