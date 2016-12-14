package com.edu.feicui.news.biz;

import android.content.Context;

import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.OkHttpUtils;
import com.edu.feicui.news.utils.SharedPreferencesUtils;
import com.edu.feicui.news.utils.Url;

import java.io.File;

/**
 * Created by Administrator on 2016/12/7.
 */
public class UserManager {
    public static void getUserInfo(Context context, SuccessListener successListener, FailListener failListener){
        String imei = CommonUtils.getIMEI(context);
        String token = SharedPreferencesUtils.getToken(context);
        OkHttpUtils.doGet(Url.USER_HOME + "?ver=0&imei=" + imei + "&token=" + token,successListener,failListener);
    }

    public static void uploadUserImage(Context context,String url,File file,SuccessListener successListener,FailListener failListener){
        String token = SharedPreferencesUtils.getToken(context);
        OkHttpUtils.uploadFile(url,token,file,successListener,failListener);
    }
}
