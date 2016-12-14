package com.edu.feicui.news.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edu.feicui.news.R;
import com.edu.feicui.news.base.BaseActivity;
import com.edu.feicui.news.biz.CommentManager;
import com.edu.feicui.news.db.NewsDBManager;
import com.edu.feicui.news.entity.BaseEntity;
import com.edu.feicui.news.entity.News;
import com.edu.feicui.news.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Administrator on 2016/12/1.
 */
public class ShowNewsActivity extends BaseActivity {
    private ImageView ivBack;
    private ImageView ivMenu;
    private TextView tvCommentNum;
    private TextView tvTitle;
    private ProgressBar progressBar;
    private WebView webView;

    private News news;
    private PopupWindow popupWindow;
    private NewsDBManager newsDBManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!CommonUtils.isNetConnect(this)){
            setContentView(R.layout.loading_fail);
        }else{
            setContentView(R.layout.activity_show_news);
            ivBack = (ImageView) findViewById(R.id.iv_back);
            ivMenu = (ImageView) findViewById(R.id.iv_menu);
            tvCommentNum = (TextView) findViewById(R.id.tv_comment_num);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            progressBar = (ProgressBar) findViewById(R.id.progressbar);
            webView = (WebView) findViewById(R.id.webView);
            newsDBManager = new NewsDBManager(this);

            news = (News) getIntent().getSerializableExtra("news");
            //设置启用Javascript
            webView.getSettings().setJavaScriptEnabled(true);
            //设置缓存模式
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setAllowContentAccess(true);
            webView.setWebChromeClient(webChromeClient);
            webView.setWebViewClient(new WebViewClient(){
                //通过重写该方法，可以防止游览器跳转到网页所在的游览器
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(news.getLink());
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });

            webView.loadUrl(news.getLink());
            CommentManager.getCommentNum(this,news.getNid(),listener,errorListener);
            intiPopupWindow();

            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(popupWindow != null && popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }else if(popupWindow != null){
                        //后面2个参数用来设置与组件的距离
                        popupWindow.showAsDropDown(ivMenu,0,12);
                    }
                }
            });

            //退出
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            //跳转至新闻评论界面
            tvCommentNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("nid",news.getNid());
                    startActivity(CommentActivity.class,bundle);
                }
            });
        }
    }

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if(newProgress >= 100){
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    //初始化PopupWindow
    public void intiPopupWindow(){
        View view = getLayoutInflater().inflate(R.layout.popup_window,null);
        TextView tvCollectNews = (TextView) view.findViewById(R.id.tv_collect_news);
        tvCollectNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(!newsDBManager.isNewsCollection(news)){
                    newsDBManager.save(news);
                    CommonUtils.showShortToast(ShowNewsActivity.this,"收藏成功");
                }else{
                    CommonUtils.showShortToast(ShowNewsActivity.this,"已收藏，可以从收藏选项中查看");
                }
            }
        });

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        //只要不为空，则点击PopupWindow最外层布局以及点击返回键PopupWindow消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Gson gson = new Gson();
            BaseEntity<Integer> baseEntity = gson.fromJson(s,new TypeToken<BaseEntity<Integer>>(){}.getType());
            int commentNum = baseEntity.getData();
            tvCommentNum.setText(commentNum + "跟帖");
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            CommonUtils.showShortToast(ShowNewsActivity.this,"加载评论失败");
        }
    };

}
