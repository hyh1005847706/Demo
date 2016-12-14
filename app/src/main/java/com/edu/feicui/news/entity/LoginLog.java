package com.edu.feicui.news.entity;

/**
 * Created by Administrator on 2016/12/7.
 */
public class LoginLog {
    private String time;
    private String address;
    private int device;

    public LoginLog() {
    }

    public LoginLog(String time, String address, int device) {

        this.time = time;
        this.address = address;
        this.device = device;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }
}
