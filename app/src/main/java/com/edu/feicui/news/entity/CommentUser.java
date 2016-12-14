package com.edu.feicui.news.entity;

/**
 * Created by Administrator on 2016/12/7.
 */
public class CommentUser {
    private int result;
    private String explain;

    public CommentUser() {
    }

    public CommentUser(int result, String explain) {

        this.result = result;
        this.explain = explain;
    }

    public int getResult() {

        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
