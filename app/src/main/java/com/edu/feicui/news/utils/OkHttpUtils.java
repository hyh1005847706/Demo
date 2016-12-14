package com.edu.feicui.news.utils;

import android.os.Handler;
import android.os.Message;

import com.edu.feicui.news.listener.FailListener;
import com.edu.feicui.news.listener.SuccessListener;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/5.
 */
public class OkHttpUtils {

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Handler handler = new Handler();
    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");

    //get请求
    public static void doGet(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        Call call = httpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void doGet(String url, final SuccessListener successListener, final FailListener failListener){
        Request request = new Request.Builder().url(url).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String error = e.getMessage();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        failListener.onFail(error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(response.isSuccessful()){
                    final String json = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            successListener.onSuccess(json);
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            failListener.onFail("访问失败");
                        }
                    });
                }
            }
        });
    }

    public static void uploadFile(String url, String token, File file, final SuccessListener successListener, final FailListener failListener){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token",token)
                .addFormDataPart("portrait","headImage.jpg",RequestBody.create(MEDIA_TYPE_IMAGE,file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String error = e.getMessage();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        failListener.onFail(error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String json = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            successListener.onSuccess(json);
                        }
                    });
                }
            }
        });
    }
}
