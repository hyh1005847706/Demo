package com.edu.feicui.news.entity;

/**
 * Created by Administrator on 2016/11/28.
 */
public class SubType {
    //子分类编号
    private int subid;
    //子分类名
    private String subgroup;

    public int getSubid() {
        return subid;
    }

    public void setSubid(int subid) {
        this.subid = subid;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public SubType(int subid, String subgroup) {
        this.subgroup = subgroup;
        this.subid = subid;
    }

    public SubType() {
    }
}
