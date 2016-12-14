package com.edu.feicui.news.entity;

/**
 * Created by Administrator on 2016/12/5.
 */
//封装了有关用户登陆注册等响应信息
public class UserResponse {
    private int result;
    private String explain;
    private String token;

    public UserResponse() {
    }

    public UserResponse(int result, String explain, String token) {
        this.result = result;
        this.explain = explain;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
