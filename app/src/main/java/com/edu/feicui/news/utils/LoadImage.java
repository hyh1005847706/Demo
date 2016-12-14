package com.edu.feicui.news.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/11/29.
 */
public class LoadImage {
    private Context context;
    private LruCache<String,Bitmap> cache = new LruCache<>(4 * 1024 * 1024);//4M大小

    public LoadImage(Context context){
        this.context = context;
    }

    //url为图片路径，imageView用于将该路径的图片进行显示的
    public void displayBitmap(String url, ImageView imageView){
        //判断路径是否合法
        if(TextUtils.isEmpty(url)){
            return;
        }

        //先从内存中的缓存中查看是否有缓存该图片，如果有，则使用缓存中的图片，如果没有则继续后面的操作
        Bitmap bitmap = cache.get(url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        //如果内存中没有缓存该图片，则去缓存的文件中查找是否有改图片，如果有，则缓存该图片，如果没有，则继续后面的操作
        bitmap = getBitmapFromFileCache(url);
        if(bitmap != null){
            cache.put(url, bitmap);
            imageView.setImageBitmap(bitmap);
        }

        //如果文件存中也没有对应的图片，则从网络中加载图片，并缓存至文件和内存中
        VolleyHttp volleyHttp = new VolleyHttp(context);
        volleyHttp.sendImageRequest(url,imageCache,imageView);
    }

    private ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
        @Override
        public Bitmap getBitmap(String s) {
            return  cache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            //将该图片缓存至文件与内存中
            cache.put(s, bitmap);
            saveBitmapToFileCache(s, bitmap);
        }
    };

    private void saveBitmapToFileCache(String url,Bitmap bitmap){
        String filename = url.substring(url.lastIndexOf("/") + 1);
        File cachaDir = context.getExternalCacheDir();
        //如果返回为null，则不进行缓存
        if(cachaDir == null){
            return;
        }

        //如果不为空，但目录还没创建，则创建该目录
        if(!cachaDir.exists()){
            cachaDir.mkdirs();
        }

        try {
            OutputStream stream = new FileOutputStream(new File(cachaDir, filename));
            //保存第一个参数为图片的类型，第二个参数为图片的质量，第三个参数为输出流，图片的数据会通过输出流保存至文件中
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromFileCache(String url){
        String filename = url.substring(url.lastIndexOf("/") + 1);
        //获取缓存文件目录
        File cacheDir = context.getExternalCacheDir();
        if(cacheDir != null && cacheDir.exists()){
            File bitmapFile = new File(cacheDir,filename);
            //判断文件是否存在，如果存在，则进行加载，并返回，如果不存在，则返回null
            if(bitmapFile != null && bitmapFile.exists()){
                return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
            }
        }
        return  null;
    }

}
