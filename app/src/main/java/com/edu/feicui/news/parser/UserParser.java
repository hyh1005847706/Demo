package com.edu.feicui.news.parser;

import com.edu.feicui.news.entity.BaseEntity;

import com.edu.feicui.news.entity.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Administrator on 2016/12/7.
 */
public class UserParser {
    public static BaseEntity<Users> parseUser(String json){
        Gson gson = new Gson();
        BaseEntity<Users> baseEntity = gson.fromJson(json,new TypeToken<BaseEntity<Users>>(){}.getType());
        return baseEntity;
    }
}
