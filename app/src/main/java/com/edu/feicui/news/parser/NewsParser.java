package com.edu.feicui.news.parser;

import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.News;
import com.edu.feicui.news.entity.NewsType;
import com.edu.feicui.news.entity.SubType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class NewsParser {

    public static List<SubType> parseNewsYype(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<NewsType>>>(){}.getType();
        BaseEntity<List<NewsType>> baseEntity = gson.fromJson(json, type);
        if(baseEntity != null){
            NewsType newsType = baseEntity.getData().get(0);
            return newsType.getSubgrp();
        }

        return null;
    }

    //解析新闻列表数据
    public static List<News> parseNews(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<News>>>(){}.getType();
        BaseEntity<List<News>> baseEntity = gson.fromJson(json, type);
        if(baseEntity != null){
            return baseEntity.getData();
        }

        return null;
    }
}
