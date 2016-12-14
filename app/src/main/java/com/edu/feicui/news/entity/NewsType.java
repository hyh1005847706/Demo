package com.edu.feicui.news.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class NewsType {
    //分类编号
    private int gid;
    //分类名
    private String group;
    //子分类
    List<SubType> subgrp;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<SubType> getSubgrp() {
        return subgrp;
    }

    public void setSubgrp(List<SubType> subgrp) {
        this.subgrp = subgrp;
    }


}
