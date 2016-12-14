package com.edu.feicui.news.entity;

/**
 * Created by Administrator on 2016/12/6.
 */
public class ShowComments {
    private int cid;
    private String uid;
    private String portrait;
    private String stamp;
    private String content;

    public ShowComments() {
    }

    public ShowComments(int cid, String uid, String portrait, String stamp, String content) {

        this.cid = cid;
        this.uid = uid;
        this.portrait = portrait;
        this.stamp = stamp;
        this.content = content;
    }

    public int getCid() {

        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
