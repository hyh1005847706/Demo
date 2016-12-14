package com.edu.feicui.news.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class BaseEntityList<T> {
    private List<T> data;
    private String status;
    private String message;

    public BaseEntityList() {
    }

    public BaseEntityList(List<T> data, String status, String message) {

        this.data = data;
        this.status = status;
        this.message = message;
    }

    public List<T> getData() {

        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
