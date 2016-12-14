package com.edu.feicui.news.biz;

import android.content.Context;
import android.content.pm.PackageManager;

import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;
import com.edu.feicui.news.utils.CommonUtils;
import com.edu.feicui.news.utils.OkHttpUtils;
import com.edu.feicui.news.utils.Url;

/**
 * Created by Administrator on 2016/12/8.
 */
public class VersionManager {
    public static void getUpdateVersion(Context context, SuccessListener successListener, FailListener failListener){
        String imei = CommonUtils.getIMEI(context);
        String packageName = context.getPackageName();
        OkHttpUtils.doGet(Url.UPDATE_VERSION + "?ver=0&imei=" + imei + "&pkg=" + packageName,successListener,failListener);
    }
}
